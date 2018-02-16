package com.bdpanajoto.examples.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

public class DatabaseInterfaceImpl implements DatabaseInterface {

	private DataSource ds;

	DatabaseInterfaceImpl(String driver, String serverName, String databaseName, int port, String user,
			char[] password) {

		try {
			OracleDataSource ds = new OracleDataSource();
			ds.setDriverType(driver);
			ds.setServerName(serverName);
			ds.setDatabaseName(databaseName);
			ds.setPortNumber(port);
			ds.setUser(user);
			ds.setPassword(String.copyValueOf(password));
			this.ds = ds;

			try (Connection conn = ds.getConnection()) {
				System.out.println(
						"Connection to : " + serverName + ":" + port + "/" + databaseName + " tested successfully");
			}
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	@Override
	public void executeSQL(String sql) {
		System.out.println("Executing: " + sql);
		try (Connection conn = ds.getConnection(); PreparedStatement psmt = conn.prepareStatement(sql)) {
			psmt.executeUpdate();
			psmt.close();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	@Override
	public void executeBatchSQL(List<String> sqlList) {
		try (Connection conn = ds.getConnection(); Statement smt = conn.createStatement()) {
			conn.setAutoCommit(false);
			for (String sql : sqlList) {
				System.out.println("Adding to batch executor: " + sql);
				smt.addBatch(sql);
			}
			smt.executeBatch();
			conn.commit();
			System.out.println("Batch executed!");
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
