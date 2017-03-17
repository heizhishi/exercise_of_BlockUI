package com.example.app.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

public class BaseDao {
	private static final QueryRunner queryRunner = new QueryRunner();

	public <T> List<T> getForList(String sql, Class<T> clazz, Object... args) {
		List<T> list = null;
		Connection connection = null;
		try {
			connection = DBManager.getInstance().getConnetion();
			list = queryRunner.query(connection, sql, new BeanListHandler<T>(
					clazz), args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(connection);
		}
		return list;

	}

	public <T> T get(String sql, Class<T> clazz, Object... args) {
		T result = null;
		Connection connection = null;
		try {
			connection = DBManager.getInstance().getConnetion();
			result = queryRunner.query(connection, sql, new BeanHandler<T>(
					clazz), args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(connection);
		}
		return result;
	}
}
