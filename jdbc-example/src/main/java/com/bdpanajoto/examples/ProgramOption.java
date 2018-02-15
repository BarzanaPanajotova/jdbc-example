package com.bdpanajoto.examples;

import java.util.Arrays;
import java.util.Optional;

public enum ProgramOption {
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

	public static Optional<ProgramOption> resolve(String str) {
		return Arrays.asList(ProgramOption.values()).stream()
				.filter(x -> x.name.equalsIgnoreCase(str) && !x.equals(INVALID)).findFirst();
	}
}