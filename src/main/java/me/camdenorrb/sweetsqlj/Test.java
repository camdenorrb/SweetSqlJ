package me.camdenorrb.sweetsqlj;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.camdenorrb.sweetsqlj.impl.Sql;
import me.camdenorrb.sweetsqlj.impl.mysql.MySqlConfig;
import me.camdenorrb.sweetsqlj.test.User;

import java.io.File;


public class Test {

	public static void main(String[] args) {

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final Sql sql = new Sql(MySqlConfig.fromOrMake(new File("test.json"), gson));

		sql.table(User.class).;
	}

}
