package me.camdenorrb.sweetsqlj.base.resolver;

import me.camdenorrb.sweetsqlj.base.codec.SqlTyper;
import me.camdenorrb.sweetsqlj.impl.Sql;
import me.camdenorrb.sweetsqlj.impl.Where;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;


public interface SqlResolverBase {

	// TODO: Use Table#getValues

	String createTable(final Sql.Table<?> table);

	String deleteTable(final Sql.Table<?> table);

	String deleteTable(final Sql.Table<?> table, final Where where);

	String insertTable(final Sql.Table<?> table, final SqlValue... values);

	String queryTable(final Sql.Table<?> table);

	String queryTable(final Sql.FilteredTable<?> table);

	String queryTableDistinct(final Sql.Table<?> table, final String row);

	String clearTable(final Sql.Table<?> table);

	String pushWhere(final Where where);

	String existsInTable(final Sql.Table<?> table, final SqlValue... values);


	// Value --> TypedValue, BlankValue
	<T extends SqlValue> void addTyper(final Class<T> clazz, final SqlTyper<T> typer);

	void remTyper(final Class<? extends SqlValue> clazz);


	/*
	void create(final Sql.Table<?> table);

	void delete(final Sql.Table<?> table);


	void delete(final Sql.Table<?> table, final WhereClause where);

	void insert(final Sql.Table<?> table, final SqlValue... values);


	List<SqlValue> select(final Sql.Table<?> table);

	List<SqlValue> select(final Sql.FilteredTable<?> table);


	<T extends SqlValue> void addCodec(final Class<T> clazz, final SqlCodec<T> codec);

	void remCodec(final Class<? extends SqlValue> clazz);
	*/

}

