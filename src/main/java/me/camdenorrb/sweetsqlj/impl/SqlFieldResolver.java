package me.camdenorrb.sweetsqlj.impl;

import me.camdenorrb.jcommons.anno.Nullable;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;
import me.camdenorrb.sweetsqlj.base.codec.SqlFieldCodec;
import me.camdenorrb.sweetsqlj.base.resolver.SqlFieldResolverBase;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class SqlFieldResolver implements SqlFieldResolverBase {

	private final Map<Class<?>, SqlFieldCodec<?>> codecs = new HashMap<>() {{
		// TODO: Add initial codecs
	}};


	// TODO: this can be moved to [SqlFieldResolver]
	public SqlValue typeFor(final Field field) {

		final boolean isNullable = field.getAnnotation(Nullable.class) != null;
		final boolean isPrimaryK = field.getAnnotation(PrimaryKey.class) != null;

		final Class<?> type = field.getType();

		if (type.equals(String.class)) {

		}
		else

	}

}
