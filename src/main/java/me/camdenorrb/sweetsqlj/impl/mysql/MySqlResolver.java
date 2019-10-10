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

		final StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table.getName() + '(');
		final List<SqlValue> sqlValues = table.getSqlValues();

		for (int i = 0; i < sqlValues.size(); i++) {

			final SqlValue value = sqlValues.get(i);

			builder.append(typers.get(value.getClass()).pushTyped(value));

			if (i + 1 < sqlValues.size()) {
				builder.append(", ");
			}
		}

		return builder.append(");").toString();
	}

	@Override
	public String deleteTable(Sql.Table<?> table) {
		return "DROP TABLE IF EXISTS " + table.getName() + ';';
	}

	@Override
	public String deleteTable(Sql.Table<?> table, Where where) {
		return "DELETE FROM " + table.getName() + ' ' + pushWhere(where);
	}

	@Override
	public String insertTable(Sql.Table<?> table, SqlValue... values) {


		final StringBuilder builder = new StringBuilder();

		for (int i = 0; i < values.length; i++) {

			builder.append('?');

			if (i + 1 < values.length) {
				builder.append(", ");
			}
		}

		"INSERT INTO " + table.getName() + " (" + builder + "); VALUES (" + builder + ")";
		return null;
	}

	@Override
	public String selectTable(Sql.Table<?> table) {
		return null;
	}

	@Override
	public String queryTable(Sql.FilteredTable<?> table) {
		return null;
	}

	@Override
	public String queryTableDistinct(Sql.Table<?> table, String row) {
		return null;
	}

	@Override
	public String clearTable(Sql.Table<?> table) {
		return "TRUNCATE TABLE " + table.getName() + ';';
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends SqlValue> void addTyper(Class<T> clazz, SqlTyper<T> typer) {
		typers.put(clazz, (SqlTyper<SqlValue>) typer);
	}

	@Override
	public void remTyper(Class<? extends SqlValue> clazz) {
		typers.remove(clazz);
	}

}
