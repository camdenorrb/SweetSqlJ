package me.camdenorrb.sweetsqlj.base;

import com.zaxxer.hikari.HikariConfig;

public interface SqlConfigBase {

	HikariConfig toHikari();

}
