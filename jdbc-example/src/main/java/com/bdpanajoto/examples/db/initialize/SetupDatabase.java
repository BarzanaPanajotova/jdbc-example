package com.bdpanajoto.examples.db.initialize;

import com.bdpanajoto.examples.db.DatabaseInterface;

public final class SetupDatabase {

	private SetupDatabase() {
	}

	public static void init(DatabaseInterface db) {
		db.executeSQL("DROP TABLE app_version");
		boolean result = db
				.executeSQL("CREATE TABLE app_version (app_version VARCHAR2(100), valid_from DATE, valid_to DATE)");

		if (result) {
			System.out.println("Initialize finished successfully!");
		} else {
			System.out.println("There were errors while initializing db! Consider cleaning the schema manually!");
		}
	}
}
