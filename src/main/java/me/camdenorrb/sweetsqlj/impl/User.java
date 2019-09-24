package me.camdenorrb.sweetsqlj.impl;

import me.camdenorrb.sweetsqlj.anno.NonSql;
import me.camdenorrb.sweetsqlj.anno.PrimaryKey;

import java.util.UUID;


public class User {

	@PrimaryKey
	private final UUID uuid;

	@NonSql
	private final String secret;


	private final String name;


	public User(UUID uuid, String secret, String name) {
		this.uuid = uuid;
		this.secret = secret;
		this.name = name;
	}


	public UUID getUuid() {
		return uuid;
	}

	public String getSecret() {
		return secret;
	}

	public String getName() {
		return name;
	}

}
