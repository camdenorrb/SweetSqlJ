package me.camdenorrb.sweetsqlj.impl.mysql;

import me.camdenorrb.sweetsqlj.base.codec.SqlTyper;
import me.camdenorrb.sweetsqlj.base.resolver.SqlResolverBase;
import me.camdenorrb.sweetsqlj.impl.Sql;
import me.camdenorrb.sweetsqlj.impl.Where;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;
import me.camdenorrb.sweetsqlj.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MySqlResolver implements SqlResolverBase {

	private final Map<Class<? extends SqlValue>, SqlTyper<SqlValue>> typers = new HashMap<>();

	// TODO: Make a method to join a list of strings

	@Override
	public String createTable(final Sql.Table<?> table) {

		final List<SqlValue> sqlValues = table.getSqlValues();
		final String typedValues = StringUtils.build(sqlValues, ", ", val -> typers.get(val).pushTyped(val));

		return "CREATE TABLE IF NOT EXISTS " + table.getName() + '(' + typedValues + ");";
	}

	@Override
	public String deleteTable(final Sql.Table<?> table) {
		return "DROP TABLE IF EXISTS " + table.getName() + ';';
	}

	@Override
	public String deleteTable(final Sql.Table<?> table, final Where where) {
		return "DELETE FROM " + table.getName() + ' ' + pushWhere(where);
	}

	@Override
	public String insertTable(final Sql.Table<?> table, final SqlValue... values) {

		final String valueNames = StringUtils.build(values, ", ", SqlValue::getName);
		final String valueHolder = StringUtils.build('?', ", ", values.length);

		return "INSERT INTO " + table.getName() + '(' + valueNames + ')' + " VALUES (" + valueHolder + ");";
	}

	@Override
	public String queryTable(final Sql.Table<?> table) {
		// TODO: Check if it has any filters
		return "SELECT * FROM " + table.getName() + ' ' + pushWhere(table.getWhereFilter());
	}


	@Override
	public String queryTableDistinct(final Sql.Table<?> table, final String row) {
		return null;
	}

	@Override
	public String clearTable(final Sql.Table<?> table) {
		return "TRUNCATE TABLE " + table.getName() + ';';
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends SqlValue> void addTyper(final Class<T> clazz, SqlTyper<T> typer) {
		typers.put(clazz, (SqlTyper<SqlValue>) typer);
	}

	@Override
	public void remTyper(Class<? extends SqlValue> clazz) {
		typers.remove(clazz);
	}


	public String pushWhere(final Where where) {

		final String[] values = where.getValues();

		final String valueNames = StringUtils.build(values, ", ", it ->
			'\'' + it + '\''
		);

		// TODO: Append Where relations

		final String operator;

		switch (where.getComparison()) {
			case IN:
				operator = "IN";
				break;
			case LIKE:
				operator = "LIKE";
				break;
			case EQUALS:
				operator = "=";
				break;
			case BETWEEN:
				operator = ""
				break;
			case LESSER_THAN:
				break;
			case BIGGER_THAN:
				break;
			case EQUAL_OR_BIGGER_THAN:
				break;
			case EQUAL_OR_LESSER_THAN:
				break;
		}

		return "WHERE " + where.getName() + ' ' + where.getCompareOperator() + " (" + valueNames + ')';
	}


}
