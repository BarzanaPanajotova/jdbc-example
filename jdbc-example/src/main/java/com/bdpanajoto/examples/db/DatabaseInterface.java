package com.bdpanajoto.examples.db;

import java.util.List;

public interface DatabaseInterface {

	/**
	 * Just fire an sql statement to the db. Don't expect to handle the result or an
	 * error in execution.
	 * 
	 * @param sql
	 */
	void executeSQL(String sql);

	/**
	 * Send an ordered list of sql statements to the db. Don't expect to handle the
	 * result or an error in execution.
	 * 
	 * @param sqlList
	 */
	void executeBatchSQL(List<String> sqlList);
}
