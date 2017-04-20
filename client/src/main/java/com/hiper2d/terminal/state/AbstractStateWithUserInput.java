package com.hiper2d.terminal.state;

import java.util.Scanner;

public abstract class AbstractStateWithUserInput extends AbstractBasicState {

	@Override
	public State process() {
		printOptions();
		String input = waitForUserInput();
		while (!validateUserInput(input)) {
			printInputValidationWarning();
			input = waitForUserInput();
		}
		return processUserInput(input);
	}

	protected abstract void printOptions();

	protected abstract boolean validateUserInput(String input);

	protected abstract State processUserInput(String input);

	protected String waitForUserInput() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	protected void printInputValidationWarning() {
		System.out.println("Input is invalid, try again\n");
	}
}