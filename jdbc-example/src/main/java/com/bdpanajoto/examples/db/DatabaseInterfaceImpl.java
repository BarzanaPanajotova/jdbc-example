package com.bdpanajoto.examples.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

public class DatabaseInterfaceImpl implements DatabaseInterface {

	private DataSource ds;

	public DatabaseInterfaceImpl(String driver, String serverName, String databaseName, int port, String user,
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
		try (Connection conn = ds.getConnection()) {
			PreparedStatement psmt = conn.prepareStatement(sql);
			psmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
}
