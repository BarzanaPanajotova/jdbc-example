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
				String optionStr = reader.readLine();
				ProgramOption option = ProgramOption.resolve(optionStr);

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
					if (!optionStr.isEmpty()) {
						System.out.println(optionStr + " is not a valid option!\n");
						printOptions();
					}
				}
			}
		}
	}

	private static void printOptions() {
		System.out.println("Valid program options are:\n");
		Consumer<ProgramOption> printOptionInfo = opt -> System.out.println(opt.getName() + "   " + opt.getText());

		Arrays.asList(ProgramOption.values()).stream()

				.filter(x -> !x.equals(ProgramOption.INVALID))

				.forEach(printOptionInfo);
	}

	private static void exit() {
		System.out.println("Goodbye!");
		System.exit(0);
	}

	private static DatabaseInterface initializeDBConnection(BufferedReader reader) throws IOException {

		String driver = parseInput(reader, "Enter driver(default:thin):", "thin");
		String serverName = parseInput(reader, "Enter server name/address(default:localhost):", "localhost");
		String databaseName = parseInput(reader, "Enter db name(default:xe):", "xe");
		String portString = parseInput(reader, "Enter port No(default:1521):", "1521");
		int port = Integer.parseInt(portString);
		String username = parseInput(reader, "Enter username:", "");
		char[] password = parseInput(reader, "Enter password:", "").toCharArray();

		return DatabaseInterfaces.getSimpleDatabaseInterface(driver, serverName, databaseName, port, username,
				password);
	}

	private static String parseInput(BufferedReader reader, String queryString, String defaultValue)
			throws IOException {
		System.out.println(queryString);
		String input = reader.readLine();
		return (input.isEmpty()) ? defaultValue : input;
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

	private enum ProgramOption {
		USAGE("usage", "Prints usage options."),

		CONFIG_DB_CONN("conn", "Configures DB connection."),

		INIT("init", "Creates initial objects in the database."),

		EXIT("exit", "Terminates the application!"),

		INVALID("invalid", "This is an invalid option!");

		private String name;
		private String text;

		private ProgramOption(String name, String text) {
			this.name = name;
			this.text = text;
		}

		public String getName() {
			return name;
		}

		public String getText() {
			return text;
		}

		public static ProgramOption resolve(String str) {
			return Arrays.asList(ProgramOption.values()).stream().filter(x -> x.name.equalsIgnoreCase(str)).findFirst()
					.orElse(INVALID);
		}
	}
}
