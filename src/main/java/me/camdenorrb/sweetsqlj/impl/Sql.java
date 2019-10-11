package me.camdenorrb.sweetsqlj.impl;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;
import me.camdenorrb.sweetsqlj.base.*;
import me.camdenorrb.sweetsqlj.base.resolver.SqlFieldResolverBase;
import me.camdenorrb.sweetsqlj.base.resolver.SqlResolverBase;
import me.camdenorrb.sweetsqlj.impl.mysql.MySqlConfig;
import me.camdenorrb.sweetsqlj.impl.mysql.MySqlResolver;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;
import sun.misc.Unsafe;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


// A SQL client
// TODO: Add a debug to config
public class Sql implements Connectable {

	private boolean isConnected;

	private HikariDataSource dataSource;


	private final HikariConfig hikariConfig;

	private final SqlResolverBase sqlResolver;

	private final SqlFieldResolverBase sqlFieldResolver;


	// SqlConfig

	public Sql(final SqlConfigBase config) {
		this(config.toHikari());
	}

	public Sql(final SqlConfigBase config, final SqlResolverBase sqlResolver) {
		this(config.toHikari(), sqlResolver);
	}

	public Sql(final SqlConfigBase config, final SqlFieldResolverBase sqlFieldResolver) {
		this(config.toHikari(), sqlFieldResolver);
	}

	public Sql(final SqlConfigBase config, final SqlResolverBase sqlResolver, final SqlFieldResolverBase sqlFieldResolver) {
		this(config.toHikari(), sqlResolver, sqlFieldResolver);
	}


	// HikariConfig

	public Sql(final HikariConfig config) {
		this(config, new MySqlResolver());
	}

	public Sql(final HikariConfig config, final SqlResolverBase sqlResolver) {
		this(config, sqlResolver, new SqlFieldResolver());
	}

	public Sql(final HikariConfig config, final SqlFieldResolverBase sqlFieldResolver) {
		this(config, new MySqlResolver(), sqlFieldResolver);
	}

	public Sql(final HikariConfig config, final SqlResolverBase sqlResolver, final SqlFieldResolverBase sqlFieldResolver) {
		this.hikariConfig = config;
		this.sqlResolver = sqlResolver;
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

	public <T> Table<T> table(final Class<T> clazz, final String tableName) {
		return new Table<>(clazz, tableName);
	}


	// Execute
	// TODO: Make it use Quasar by default
	// TODO: Maybe add a way to set the sql executor
	// TODO: Make this multithreaded, make them use Coroutines in SweetSqlK
	public Boolean exe(final String statement) {
		return use(statement, PreparedStatement::execute);
	}

	// Query
	// TODO: Make it use Quasar by default
	// TODO: Maybe add a way to set the sql executor
	// TODO: Make this multithreaded, make them use Coroutines in SweetSqlK
	public ResultSet que(final String statement) {
		return use(statement, PreparedStatement::executeQuery);
	}

	public <R> R use(final String statement, final StatementBlock<R> block) {
		try (final Connection connection = dataSource.getConnection()) {
			try (final PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
				return block.attempt(preparedStatement);
			}
		}
		catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	public HikariDataSource getDataSource() {
		return dataSource;
	}


	public static Sql fromOrMake(final File configFile, final Gson gson) {
		return new Sql(MySqlConfig.fromOrMake(configFile, gson));
	}


	// Inner class time >:)

	public class Table<T> {

		private final Class<T> clazz;

		private final Field primaryKey;

		private final String name;

		// TODO: Maybe find a better way to link fields and values

		private final List<Field> fields;

		private final List<SqlValue> sqlValues;

		//CREATE TABLE IF NOT EXISTS UUIDForName (uuid CHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(255) NOT NULL)
		//"INSERT INTO UUIDForName (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name=name";

		private Table(final Class<T> clazz) {
			this(clazz, clazz.getSimpleName() + 's');
		}

		private Table(final Class<T> clazz, final String name) {

			this.clazz = clazz;
			this.name = name;

			fields = Stream.of(clazz.getDeclaredFields())
				.filter(it -> !Modifier.isTransient(it.getModifiers()))
				.collect(Collectors.toList());

			primaryKey = fields.stream()
				.filter(it -> it.isAnnotationPresent(PrimaryKey.class))
				.findFirst().orElse(null);

			exe(sqlResolver.createTable(this));
			//exe("CREATE TABLE IF NOT EXISTS " + name + typedValues() + ';');
		}


		public void add(final T value) {

			//SqlUtils.useStatement(dataSource, "INSERT INTO " + tableName);
		}

		public Table<T> filter(final Where where) {

		}

		/**
		 * @see Where#Where(String, String, String...)
		 */
		public Table<T> filter(final String name, final String s, String coolCat69) {

		}

		// TODO: filterNot

		public Table<T> filterNot(final Where where) {

		}

		/**
		 * @see Where#Where(String, String, String...)
		 */
		public Table<T> filterNot(final String name, final String s, String coolCat69) {

		}


		// Distinct select
		public List<T> values(final String columnName) {

		}


		public String getName() {
			return name;
		}

		public Class<T> getClazz() {
			return clazz;
		}

		public Field getPrimaryKey() {
			return primaryKey;
		}

		public List<Field> getFields() {
			return fields;
		}

		public List<SqlValue> getSqlValues() {
			return sqlValues;
		}


		@SuppressWarnings("unchecked")
		private T createInst(final Class<T> clazz) {
			try  {
				return (T) Unsafe.getUnsafe().allocateInstance(clazz);
			}
			catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			}
		}

		public Sql.CachedTable<T> cached() {

			// TODO: On construction select * immediately
			// TODO: Have a CachedTable#update method
			// TODO: Rather than using hacky filter methods, just use a normal one
			// TODO: Make it EXACTLY like interacting with a collection

			throw new RuntimeException("Implement");
		}


		// Example: (uuid, name) VALUES (?, ?)
		// TODO: This should be handled in [SqlResolverBase]
		/*
		private String blankValues() {
			"(uuid, name) VALUES (?, ?)"
		}*/

		// TODO: This should be handled in [SqlResolverBase]
		/*
		private String typedValues() {
			return fields.stream()
				.map(sqlFieldResolver::typeFor)
				.map(::getName)
				.collect(Collectors.joining(", ", "(", ")"));
		}*/

	}

	public class CachedTable<T> {
	}


	// TODO: Pass this to [SqlResolverBase] for filter statement
	/*
	public class FilteredTable<T> {

		private final Table<T> table;

		// Where(x, EQUAL_OR_GREATER_THAN, y)

		private final Where whereClause;


		public FilteredTable(final Table<T> table, final Where whereClause) {
			this.table = table;
			this.whereClause = whereClause;
		}


		public void add(T value) {
			table.add(value);
		}


		public FilteredTable<T> and(final Where where) {
			// Maybe just add a whree clause
			throw new RuntimeException("Implement");
		}

		public FilteredTable<T> and(final String name, String s, String coolCat69) {
			throw new RuntimeException("Implement");
		}

		public FilteredTable<T> or(final Where where) {
			// Maybe just add a whree clause
			throw new RuntimeException("Implement");
		}

		public FilteredTable<T> or(final String name, String s, String coolCat69) {
			throw new RuntimeException("Implement");
		}

		public List<T> row(String row) {
			throw new RuntimeException("Implement");
		}

		public String getName() {
			return table.getName();
		}

		public Class<T> getClazz() {
			return table.getClazz();
		}

		public Field getPrimaryKey() {
			return table.getPrimaryKey();
		}

		public List<Field> getFields() {
			return table.getFields();
		}

		public List<SqlValue> getSqlValues() {
			return table.getSqlValues();
		}

		public Table<T> getNormalTable() {
			return table;
		}

		public Where getWhereClause() {
			return whereClause;
		}

		public void forEach(final Consumer<T> consumer) {
			throw new RuntimeException("Implement!");
		}

	}*/

}
