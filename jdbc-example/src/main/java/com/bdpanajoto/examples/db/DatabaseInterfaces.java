package com.bdpanajoto.examples.db;

import java.sql.SQLException;

public final class DatabaseInterfaces {

	public static DatabaseInterface getSimpleDatabaseInterface(String driver, String serverName, String databaseName,
			int port, String user, char[] password) throws SQLException {
		return new OracleDatabaseInterfaceImpl(driver, serverName, databaseName, port, user, password);
	}
}
