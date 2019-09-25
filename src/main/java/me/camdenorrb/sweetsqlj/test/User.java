package me.camdenorrb.sweetsqlj.test;

import me.camdenorrb.sweetsqlj.anno.PrimaryKey;

import java.util.UUID;


public class User {

	// This is a placeholder and shouldn't be accessed in Java
	@PrimaryKey(autoIncrement = true)
	private int id = 100; // Set starting increment value to 100

	private final String name;

	private final UUID uuid;

	private transient final String secret;


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
