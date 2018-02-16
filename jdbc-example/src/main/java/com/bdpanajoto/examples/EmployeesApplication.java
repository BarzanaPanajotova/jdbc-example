package com.bdpanajoto.examples;

import java.sql.SQLException;
import java.util.List;

import com.bdpanajoto.examples.db.DatabaseInterface;
import com.bdpanajoto.examples.db.DatabaseInterfaces;
import com.bdpanajoto.examples.db.initialize.SetupDatabase;
import com.bdpanajoto.examples.dto.EmployeeDTO;

class EmployeesApplication {

	private DatabaseInterface dbInterface;

	private EmployeesApplication(DatabaseInterface dbInterface) {
		this.dbInterface = dbInterface;
	}

	public static EmployeesApplication createInstance(String driver, String serverName, String databaseName, int port,
			String user, char[] password) {
		try {
			DatabaseInterface dbInterface = DatabaseInterfaces.getSimpleDatabaseInterface(driver, serverName,
					databaseName, port, user, password);
			return new EmployeesApplication(dbInterface);
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage());
		}

		return null;
	}

	public void listEmployees() {
		List<EmployeeDTO> employees = dbInterface.getEmployees();
		employees.forEach(System.out::println);
	}

	public void initialSetup() {
		SetupDatabase.init(dbInterface);
	}

}
