package com.bdpanajoto.examples.db;

public interface DatabaseInterface {

	/**
	 * Just fire an sql statement to the db. Don't expect to handle the result or an
	 * error in execution.
	 * 
	 * @param sql
	 */
	void executeSQL(String sql);
}
