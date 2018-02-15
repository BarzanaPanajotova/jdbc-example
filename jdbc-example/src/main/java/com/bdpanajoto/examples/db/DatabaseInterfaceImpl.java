package com.bdpanajoto.examples.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class DatabaseInterfaceImpl implements DatabaseInterface {

	private Connection conn;

	public DatabaseInterfaceImpl(String driver, String serverName, String databaseName, int port, String user,
			char[] password) {
		try {
			OracleDataSource ds = new OracleDataSource();
			ds.setDriverType(driver);
			ds.setServerName(serverName);
			ds.setDatabaseName(databaseName);
			ds.setPortNumber(port);

			conn = ds.getConnection(user, String.copyValueOf(password));

			System.out.println("Connected to: " + serverName + ":" + port + "/" + databaseName);
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean executeSQL(String sql) {
		boolean result = false;
		try {
			PreparedStatement psmt = conn.prepareStatement(sql);
			result = psmt.executeUpdate() == 0;
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return result;
	}

}
