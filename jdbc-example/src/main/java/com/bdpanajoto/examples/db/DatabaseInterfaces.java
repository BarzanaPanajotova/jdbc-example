package com.bdpanajoto.examples.db;

public final class DatabaseInterfaces {

	public static DatabaseInterface getSimpleDatabaseInterface(String driver, String serverName, String databaseName,
			int port, String user, char[] password) {
		return new OracleDatabaseInterfaceImpl(driver, serverName, databaseName, port, user, password);
	}
}
