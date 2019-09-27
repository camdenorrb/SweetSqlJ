package me.camdenorrb.sweetsqlj.impl.mysql;

import me.camdenorrb.jcommons.anno.Nullable;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;
import me.camdenorrb.sweetsqlj.base.SqlResolverBase;
import me.camdenorrb.sweetsqlj.impl.type.base.SqlType;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


// TODO: Make this write full statements such as createTable
// TODO: Make this settable in [Sql] class in order to add better support to change
public class MySqlResolver implements SqlResolverBase {

	private final Map<> codecs


		// TODO: this can be moved to SQL or to an abstraction
	public SqlType typeFor(final Field field) {

		final boolean isNullable = field.getAnnotation(Nullable.class) != null;
		final boolean isPrimaryK = field.getAnnotation(PrimaryKey.class) != null;

		final Class<?> type = field.getType();

		if (type.equals(String.class)) {

		}
		else

	}

	interface Codec<T> {

		T pull(final ResultSet input);

		List<SqlType> push(final Field input);

	}

}
