package com.hiper2d.terminal.state.impl;

import com.hiper2d.stomp.StompClient;
import com.hiper2d.terminal.state.AbstractStateWithUserInput;
import com.hiper2d.terminal.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WelcomeState extends AbstractStateWithUserInput {
	@Autowired
	StompClient stompClient;

	@Override
	public State getState() {
		return State.WELCOME;
	}

	@Override
	public void printOptions() {
		System.out.println("-- Welcome to text analyzer application");
		System.out.println("-- Please chose one of the following options:");
		System.out.println("-> 1 or 'c' for establishing connection with Server");
		System.out.println("-> any other input for closing the application");
	}

	@Override
	protected boolean validateUserInput(String input) {
		return true;
	}

	@Override
	public State processUserInput(String input) {
		State nextState;
		switch (input) {
			case "1":
			case "c":
				stompClient.connect();
				System.out.println("-- You was connected\n");
				nextState = State.CONNECTED;
				break;
			default:
				nextState = State.FAREWELL;
		}
		return nextState;
	}
}
