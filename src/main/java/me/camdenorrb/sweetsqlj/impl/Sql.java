package me.camdenorrb.sweetsqlj.impl;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.camdenorrb.jcommons.utils.TryUtils;
import me.camdenorrb.sweetsqlj.anno.NonSql;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;
import me.camdenorrb.sweetsqlj.base.Connectable;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


// A SQL client
public final class Sql implements Connectable {

	private boolean isConnected;

	private HikariDataSource dataSource;

	private final HikariConfig hikariConfig;


	public Sql(final HikariConfig config) {
		this.hikariConfig = config;
	}

	public Sql(final SqlConfig config) {

		final HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ':' + config.getPort() + '/' + config.getBase() + "?useSSL=false");
		hikariConfig.setUsername(config.getUser());
		hikariConfig.setPassword(config.getPass());
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		this.hikariConfig = hikariConfig;
	}


	@Override
	public void attach() {

		assert !isConnected;

		this.dataSource = new HikariDataSource(hikariConfig);

		isConnected = true;
	}

	@Override
	public void detach() {

		assert isConnected;

		this.dataSource = null;

		isConnected = false;
	}


	@Override
	public boolean isConnected() {
		return isConnected;
	}

	public <T> Table<T> table(final Class<T> clazz) {
		return new Table<>(clazz);
	}


	public HikariDataSource getDataSource() {
		return dataSource;
	}


	public static Sql fromOrMake(final File configFile, final Gson gson) {
		return new Sql(SqlConfig.fromOrMake(configFile, gson));
	}


	class Table<T> {

		private final Class<T> clazz;

		private final Field primaryKey;

		private final List<Field> variables;


		private Table(final Class<T> clazz) {

			this.clazz = clazz;

		    variables = Arrays.stream(clazz.getDeclaredFields())
				.filter(it -> !it.isAnnotationPresent(NonSql.class))
				.collect(Collectors.toList());

		    primaryKey = variables.stream()
			    .filter(it -> it.isAnnotationPresent(PrimaryKey.class))
			    .findFirst().orElse(null);
		}


		public Class<T> getClazz() {
			return clazz;
		}

		public Field getPrimaryKey() {
			return primaryKey;
		}

		public List<Field> getVariables() {
			return variables;
		}


		@SuppressWarnings("unchecked")
		private T createInst(final Class<T> clazz) {
			return (T) TryUtils.attemptOrNull(() -> Unsafe.getUnsafe().allocateInstance(clazz));
		}

	}


}
