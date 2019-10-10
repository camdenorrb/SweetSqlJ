package me.camdenorrb.sweetsqlj.impl.mysql;

import me.camdenorrb.sweetsqlj.base.codec.SqlTyper;
import me.camdenorrb.sweetsqlj.base.resolver.SqlResolverBase;
import me.camdenorrb.sweetsqlj.impl.Sql;
import me.camdenorrb.sweetsqlj.impl.Where;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO: Make this write full statements such as createTable
// TODO: Make this settable in [Sql] class in order to add better support to change
public class MySqlResolver implements SqlResolverBase {

	private final Map<Class<? extends SqlValue>, SqlTyper<SqlValue>> typers = new HashMap<>();


	@Override
	public String createTable(Sql.Table<?> table) {

		final StringBuilder builder = new StringBuilder();
		final List<SqlValue> sqlValues = table.getSqlValues();

		for (int i = 0; i < sqlValues.size(); i++) {

			final SqlValue value = sqlValues.get(i);

			builder.append(typers.get(value.getClass()).pushTyped(value));

			if (i + 1 < sqlValues.size()) {
				builder.append(", ");
			}
		}

		return "CREATE TABLE IF NOT EXISTS " + table.getName() + '(' + builder.append(");");
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

		final StringBuilder valueNamesBuilder = new StringBuilder();

		for (int i = 0; i < values.length; i++) {

			valueNamesBuilder.append(values[i].getName());

			if (i + 1 < values.length) {
				valueNamesBuilder.append(", ");
			}
		}

		final StringBuilder valueBuilder = new StringBuilder();

		for (int i = 0; i < values.length; i++) {

			valueBuilder.append('?');

			if (i + 1 < values.length) {
				valueBuilder.append(", ");
			}
		}

		return "INSERT INTO " + table.getName() + '(' + valueNamesBuilder + ')' + " VALUES (" + valueBuilder + ");";
	}

	@Override
	public String queryTable(final Sql.Table<?> table) {
		return null;
	}

	@Override
	public String queryTable(final Sql.FilteredTable<?> table) {

		return "SELECT * FROM " + table.getNormalTable() + ' ' + pushWhere(table.getWhereClause());
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
	public String pushWhere(final Where where) {

		final String[] values = where.getValues();
		final StringBuilder builder = new StringBuilder("(");

		for (int i = 0; i < values.length; i++) {

			builder.append('\'').append(values[i]).append('\'');

			if (i + 1 < values.length) {
				builder.append(", ");
			}
			else {
				builder.append(')');
			}
		}

		return "WHERE " + where.getName() + ' ' + where.getCompareOperator() + ' ' + builder;
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

}
