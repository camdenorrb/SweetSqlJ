package me.camdenorrb.sweetsqlj;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.camdenorrb.sweetsqlj.impl.Sql;
import me.camdenorrb.sweetsqlj.impl.Where;
import me.camdenorrb.sweetsqlj.test.User;

import java.io.File;
import java.sql.Connection;
import java.util.Map;


public class Test {

	public static void main(String[] args) {

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final Sql sql = Sql.fromOrMake(new File("test.json"), gson);

		final Sql.Table<User> userTable = sql.table(User.class);

		userTable.contains(user);

		userTable.row("");

		userTable.filter("name", "=", "coolCat69").forEach();

		userTable.filter(new Where("name", "=", "coolCat69")).contains(user);

		userTable.filter(new Where("name", "=", "coolCat69")).forEach(it ->
			System.out.println(it.getUuid())
		);
	}


	/*
	public ResultSet getData(final String from, final String where, final String equals){

		final String query = "SELECT * FROM " + from + " WHERE " + where + " = '" + equals +"';";

		try(final Statement statement = connection.createStatement()) {
			try(final ResultSet result = statement.executeQuery(query)) {
				while(result.next()) {
					data.put("PLAYERNAME", result.getString("PLAYERNAME"));
					data.put("COINS", result.getString("COINS"));
				}

				return result;
			}
		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}

		return null;
	}*/

	/*
	public boolean exists(final String table, final String row, final String value) {

		final String query = "SELECT EXISTS (SELECT 1 FROM " + table + " WHERE " + row + " = ?);";

		try(final PreparedStatement statement = connection.prepareStatement(query)) {

			statement.setString(1, value);

			try (final ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					return false;
				}

				return resultSet.getBoolean(1);
			}
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	 */

}
