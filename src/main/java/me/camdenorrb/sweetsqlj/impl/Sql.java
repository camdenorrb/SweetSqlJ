package me.camdenorrb.sweetsqlj.impl;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.camdenorrb.jcommons.base.tryblock.TryCloseBlock;
import me.camdenorrb.jcommons.utils.TryUtils;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;
import me.camdenorrb.sweetsqlj.base.Connectable;
import me.camdenorrb.sweetsqlj.base.SqlResolverBase;
import me.camdenorrb.sweetsqlj.impl.mysql.MySqlConfig;
import me.camdenorrb.sweetsqlj.impl.mysql.MySqlFieldResolver;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static me.camdenorrb.jcommons.utils.TryUtils.attemptOrPrintErr;


// A SQL client
public final class Sql implements Connectable {

	private boolean isConnected;

	private HikariDataSource dataSource;

	private final HikariConfig hikariConfig;

	private final SqlResolverBase sqlResolver;


	public Sql(final HikariConfig config) {
		this(config, new MySqlFieldResolver());
	}

	public Sql(final MySqlConfig config) {
		this(config, new MySqlFieldResolver());
	}

	public Sql(final HikariConfig config, final MySqlFieldResolver sqlFieldResolver) {
		this.hikariConfig = config;
		this.sqlResolver = sqlFieldResolver;
	}

	public Sql(final MySqlConfig config, final MySqlFieldResolver sqlFieldResolver) {

		final HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ':' + config.getPort() + '/' + config.getBase() + "?useSSL=false");
		hikariConfig.setUsername(config.getUser());
		hikariConfig.setPassword(config.getPass());
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		this.hikariConfig = hikariConfig;
		this.sqlFieldResolver = sqlFieldResolver;
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

	public void use(final String statement, final TryCloseBlock<PreparedStatement> block) {
		attemptOrPrintErr(dataSource::getConnection, (connection) ->
			attemptOrPrintErr(() -> connection.prepareStatement(statement), block)
		);
	}


	public HikariDataSource getDataSource() {
		return dataSource;
	}


	public static Sql fromOrMake(final File configFile, final Gson gson) {
		return new Sql(MySqlConfig.fromOrMake(configFile, gson));
	}


	class Table<T> {

		private final Class<T> clazz;

		private final Field primaryKey;

		private final String tableName;

		private final List<Field> variables;

		//CREATE TABLE IF NOT EXISTS UUIDForName (uuid CHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(255) NOT NULL)
		//"INSERT INTO UUIDForName (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=name";


		private Table(final Class<T> clazz) {
			this(clazz, clazz.getSimpleName() + 's');
		}

		private Table(final Class<T> clazz, final String tableName) {

			this.clazz = clazz;
			this.tableName = tableName;

			variables = Arrays.stream(clazz.getDeclaredFields())
				.filter(it -> !Modifier.isTransient(it.getModifiers()))
				.collect(Collectors.toList());

			primaryKey = variables.stream()
				.filter(it -> it.isAnnotationPresent(PrimaryKey.class))
				.findFirst().orElse(null);

			use("CREATE TABLE IF NOT EXISTS " + tableName + typedValues() + ';', PreparedStatement::execute);
		}


		public void add(final T value) {
			SqlUtils.useStatement(dataSource, "INSERT INTO " + tableName);
		}

		public FilteredTable<T> filter() {

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

		//CREATE TABLE IF NOT EXISTS UUIDForName (uuid CHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(255) NOT NULL)

		private void create() {
			SqlUtils.useStatement(dataSource, "CREATE TABLE IF NOT EXISTS " + tableName + typedValues() + ';');
		}


		// Example: (uuid, name) VALUES (?, ?)
		private String blankValues() {
			"(uuid, name) VALUES (?, ?)"
		}

		private String typedValues() {
			return variables.stream()
				.map(sqlFieldResolver::typeFor)
				.collect(Collectors.joining(", ", "(", ")"));
		}

	}


	class FilteredTable<T> {

		private final Table<T> table;

		private final WhereClause[] whereClauses;


		public FilteredTable(final Table<T> table, final WhereClause... whereClauses) {
			this.table = table;
			this.whereClauses = whereClauses;
		}


		public Table<T> getNormalTable() {
			return table;
		}


	}


}
