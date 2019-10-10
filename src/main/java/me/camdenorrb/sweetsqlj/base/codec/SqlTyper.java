package me.camdenorrb.sweetsqlj.base.codec;

import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;

@FunctionalInterface
public interface SqlTyper<T extends SqlValue> {

	String pushTyped(final T value);

	//String pushBlank(final T value);

}
