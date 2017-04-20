package com.hiper2d.terminal.state.impl;

import com.hiper2d.stomp.StompClient;
import com.hiper2d.terminal.state.AbstractStateWithUserInput;
import com.hiper2d.terminal.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import static com.hiper2d.util.ApiConstants.DEFAULT_PATH;

@Service
public class ConnectedState extends AbstractStateWithUserInput {
	@Autowired
	StompClient stompClient;

	@Override
	public State getState() {
		return State.CONNECTED;
	}

	@Override
	protected void printOptions() {
		System.out.println("-- Okay, now print a directory path for scanning");
		System.out.println("-- Please notice, that the path is relative to the Server's jar file location");
		System.out.println("-- To keep the default path ('.') press Enter or 'e'");
		System.out.println("-- And of course you can disconnect by printing 'd' or 'disconnect'");
	}

	@Override
	protected boolean validateUserInput(String input) {
		return true;
	}

	@Override
	protected State processUserInput(String input) {
		State nextState;
		switch (input) {
			case "d":
			case "disconnect":
				stompClient.disconnect();
				System.out.println("-- You was disconnected\n");
				nextState = State.WELCOME;
				break;
			case "e":
			case "E":
			case "":
				stompClient.sendRequest(DEFAULT_PATH);
				System.out.println("-- Your request was sent to the Server\n");
				nextState = State.PENDING;
				break;
			default:
				stompClient.sendRequest(input);
				System.out.println("-- Your request was sent to the Server\n");
				nextState = State.PENDING;
		}
		return nextState;
	}
}
