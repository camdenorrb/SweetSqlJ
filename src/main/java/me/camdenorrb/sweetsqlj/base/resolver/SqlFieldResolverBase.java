package me.camdenorrb.sweetsqlj.base.resolver;

import me.camdenorrb.sweetsqlj.base.codec.SqlFieldCodec;
import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;

import java.lang.reflect.Field;

public interface SqlFieldResolverBase {

	SqlValue valueFor(final Field field);


	<T extends SqlValue> void addCodec(final Class<T> clazz, final SqlFieldCodec<T> codec);

	void remCodec(final Class<? extends SqlValue> clazz);

}
