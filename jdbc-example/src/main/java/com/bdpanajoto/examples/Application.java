package com.bdpanajoto.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Consumer;

import com.bdpanajoto.examples.db.DatabaseInterface;
import com.bdpanajoto.examples.db.DatabaseInterfaces;
import com.bdpanajoto.examples.db.initialize.SetupDatabase;

public class Application {

	public static void main(String[] args) throws IOException {
		init();
	}

	private static void init() throws IOException {
		printOptions();

		handleInput();
	}

	private static void handleInput() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			DatabaseInterface dbInterface = null;
			while (true) {
				String str = reader.readLine();
				if (!str.isEmpty()) {
					ProgramOption option = ProgramOption.resolve(str).orElse(ProgramOption.INVALID);

					switch (option) {
					case USAGE:
						printOptions();
						break;
					case EXIT:
						exit();
						break;
					case CONFIG_DB_CONN:
						dbInterface = initializeDBConnection(reader);
						break;
					case INIT:
						initializeDBObjects(reader, dbInterface);
						break;
					default:
						System.out.println(str + " is not a valid option!\n");
						printOptions();
					}
				}
			}
		}
	}

	private static void printOptions() {
		System.out.println("Valid program options are:\n");
		Consumer<ProgramOption> printFormatedOption = opt -> System.out.println(opt.getName() + "   " + opt.getText());
		Arrays.asList(ProgramOption.values()).stream().filter(x -> !x.equals(ProgramOption.INVALID))
				.forEach(printFormatedOption);
	}

	private static void exit() {
		System.out.println("Goodbye!");
		System.exit(0);
	}

	private static DatabaseInterface initializeDBConnection(BufferedReader reader) throws IOException {
		DatabaseInterface dbInterface;
		System.out.println("Enter driver(default:thin):");
		String driver = reader.readLine();
		driver = (driver.isEmpty()) ? "thin" : driver;

		System.out.println("Enter server name/address(default:localhost):");
		String serverName = reader.readLine();
		serverName = (serverName.isEmpty()) ? "localhost" : serverName;

		System.out.println("Enter db name(default:xe):");
		String databaseName = reader.readLine();
		databaseName = (databaseName.isEmpty()) ? "xe" : databaseName;

		System.out.println("Enter port No(default:1521):");
		String portString = reader.readLine();
		int port = (portString.isEmpty()) ? 1521 : Integer.parseInt(portString);

		System.out.println("Enter username:");
		String username = reader.readLine();

		System.out.println("Enter password:");
		char[] password = reader.readLine().toCharArray();

		dbInterface = DatabaseInterfaces.getSimpleDatabaseInterface(driver, serverName, databaseName, port, username,
				password);
		return dbInterface;
	}

	private static void initializeDBObjects(BufferedReader reader, DatabaseInterface dbInterface) throws IOException {
		if (dbInterface != null) {
			System.out.println(
					"Are you sure you want to initalize(existing objects in the db schema will be dropped!)? Y/N");
			if ("y".equalsIgnoreCase(reader.readLine())) {
				SetupDatabase.init(dbInterface);
			} else {
				System.out.println("Not executing init!");
			}
		} else {
			System.out.println("There is no connection to a db!");
		}
	}

}