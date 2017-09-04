package com.wechat.customer.database;

import org.hibernate.dialect.MySQLDialect;

public class MysqlDialect  extends MySQLDialect {
	public String getTableTypeString() {

		return " COLLATE utf8_bin";

	}
}
