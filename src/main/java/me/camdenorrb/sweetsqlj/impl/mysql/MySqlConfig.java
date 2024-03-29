package me.camdenorrb.sweetsqlj.impl.mysql;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import me.camdenorrb.jcommons.utils.GsonUtils;
import me.camdenorrb.sweetsqlj.base.SqlConfigBase;

import java.io.File;


public final class MySqlConfig implements SqlConfigBase {

	private final int port;

	private final String user, pass, host, base;


	public MySqlConfig() {
		this("username", "password", "127.0.0.1", 3306, "databaseName");
	}

	public MySqlConfig(final String user, final String pass, final String host, final int port, final String base) {
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.port = port;
		this.base = base;
	}


	@Override
	public HikariConfig toHikari() {

		final HikariConfig hikariConfig = new HikariConfig();

		hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ':' + port + '/' + base + "?useSSL=false");
		hikariConfig.setUsername(user);
		hikariConfig.setPassword(pass);
		hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		return hikariConfig;
	}


	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPass() {
		return pass;
	}

	public String getHost() {
		return host;
	}

	// Gets the database name
	public String getBase() {
		return base;
	}


	public static MySqlConfig fromOrMake(final File file, final Gson gson) {
		return GsonUtils.fromJsonOrMake(gson, file, MySqlConfig.class, MySqlConfig::new);
	}

}
