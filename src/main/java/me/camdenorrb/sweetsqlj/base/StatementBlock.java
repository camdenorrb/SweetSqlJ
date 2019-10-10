package me.camdenorrb.sweetsqlj.base;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementBlock<R> {

	R attempt(final PreparedStatement statement) throws SQLException;

}