package me.camdenorrb.sweetsqlj.base.codec.parts;

import me.camdenorrb.sweetsqlj.impl.value.base.SqlValue;

import java.lang.reflect.Field;
import java.util.List;


public interface SqlFieldPusher<T> {

	List<SqlValue> push(final Field input);

}