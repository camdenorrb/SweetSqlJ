package me.camdenorrb.sweetsqlj.base.codec.parts;

import java.sql.ResultSet;


public interface SqlFieldPuller<T> {

	T pull(final ResultSet input);

}