package com.bdpanajoto.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

public class Application {

	private static final String GOODBYE = "Goodbye!";
	private static final String INTRO_MSG = "\nPlease setup a db connection!";
	private static final String Y = "y";
	private static final String EMPTY_STRING = "";
	private static final String DEFAULT_PORT = "1521";
	private static final String DEFAULT_DB_NAME = "xe";
	private static final String DEFAULT_SERVER = "localhost";
	private static final String DEFAULT_DRIVER = "thin";
	private static final String ENTER_PASSWORD_MSG = "Enter password:";
	private static final String ENTER_USERNAME_MSG = "Enter username:";
	private static final String ENTER_PORT_NO_MSG = "Enter port No(default:1521):";
	private static final String ENTER_DB_NAME_MSG = "Enter db name(default:xe):";
	private static final String ENTER_SERVER_NAME_MSG = "Enter server name/address(default:localhost):";
	private static final String ENTER_DRIVER_MSG = "Enter driver(default:thin):";
	private static final String NOT_A_VALID_OPTION_MSG = "{0} is not a valid option!\n";
	private static final String VALID_PROGRAM_OPTIONS_MSG = "Valid program options are:\n";
	private static final String NOT_EXECUTING_INIT_MSG = "Not executing init!";
	private static final String ARE_YOU_SURE_MSG = "Are you sure you want to initalize(existing objects in the db schema will be dropped!)? Y/N";

	public static void main(String[] args) throws IOException {
		init();
	}

	private static void init() throws IOException {
		printOptions();

		handleInput();
	}

	private static void printOptions() {
		System.out.println(VALID_PROGRAM_OPTIONS_MSG);
		Consumer<ProgramOption> printOptionInfo = opt -> System.out.println(opt.getName() + "   " + opt.getText());

		Arrays.asList(ProgramOption.values()).stream()

				.filter(x -> !x.equals(ProgramOption.INVALID))

				.forEach(printOptionInfo);
	}

	private static void handleInput() throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println(INTRO_MSG);
			EmployeesApplication employeesApplication = null;

			while (Objects.isNull(employeesApplication)) {
				employeesApplication = getNewEmployeeApplication(reader);
			}

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
					employeesApplication = getNewEmployeeApplication(reader);
					break;
				case INIT:
					initialDBSetup(reader, employeesApplication);
					break;
				case GET_EMPLOYEES:
					employeesApplication.listEmployees();
					break;
				default:
					if (!optionStr.isEmpty()) {
						System.out.println(MessageFormat.format(NOT_A_VALID_OPTION_MSG, optionStr));
						printOptions();
					}
				}
			}
		}
	}

	private static void exit() {
		System.out.println(GOODBYE);
		System.exit(0);
	}

	private static EmployeesApplication getNewEmployeeApplication(BufferedReader reader) throws IOException {
		String driver = parseDBConnectionInput(reader, ENTER_DRIVER_MSG, DEFAULT_DRIVER);
		String serverName = parseDBConnectionInput(reader, ENTER_SERVER_NAME_MSG, DEFAULT_SERVER);
		String databaseName = parseDBConnectionInput(reader, ENTER_DB_NAME_MSG, DEFAULT_DB_NAME);
		String portString = parseDBConnectionInput(reader, ENTER_PORT_NO_MSG, DEFAULT_PORT);
		int port = Integer.parseInt(portString);
		String username = parseDBConnectionInput(reader, ENTER_USERNAME_MSG, EMPTY_STRING);
		char[] password = parseDBConnectionInput(reader, ENTER_PASSWORD_MSG, EMPTY_STRING).toCharArray();

		return EmployeesApplication.createInstance(driver, serverName, databaseName, port, username, password);
	}

	private static String parseDBConnectionInput(BufferedReader reader, String queryString, String defaultValue)
			throws IOException {
		System.out.println(queryString);
		String input = reader.readLine();
		return (input.isEmpty()) ? defaultValue : input;
	}

	private static void initialDBSetup(BufferedReader reader, EmployeesApplication employeesApplication)
			throws IOException {
		System.out.println(ARE_YOU_SURE_MSG);
		if (Y.equalsIgnoreCase(reader.readLine())) {
			employeesApplication.initialSetup();
		} else {
			System.out.println(NOT_EXECUTING_INIT_MSG);
		}
	}

	private enum ProgramOption {
		USAGE("usage", "Prints usage options."),

		CONFIG_DB_CONN("conn", "Configures DB connection."),

		INIT("init", "Creates initial objects in the database."),

		EXIT("exit", "Terminates the application!"),

		INVALID("invalid", "This is an invalid option!"),

		GET_EMPLOYEES("employees", "Displays data for all employees availavle in employee DB.");

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
			return Arrays.asList(ProgramOption.values()).stream()

					.filter(x -> x.name.equalsIgnoreCase(str)).findFirst()

					.orElse(INVALID);
		}
	}
}
