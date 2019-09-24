package me.camdenorrb.sweetsqlj.base;

public interface Connectable {

	// Connect
	void attach();

	// Disconnect
	void detach();


	boolean isConnected();

}

