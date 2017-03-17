package com.example.app.dao;

import java.sql.Connection;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBManager {
	private DBManager() {

	}

	private static DBManager instance = new DBManager();

	public static DBManager getInstance() {
		return instance;
	}

	private static DataSource dataSource;
	static {
		dataSource = new ComboPooledDataSource("c3p0");
	}

	public Connection getConnetion() {
		Connection connection = null;
		if (dataSource != null) {
			try {
				connection = dataSource.getConnection();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return connection;
	}
}
