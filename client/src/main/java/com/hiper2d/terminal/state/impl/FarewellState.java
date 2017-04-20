package com.hiper2d.terminal.state.impl;

import com.hiper2d.terminal.state.AbstractBasicState;
import com.hiper2d.terminal.state.State;
import org.springframework.stereotype.Service;

@Service
public class FarewellState extends AbstractBasicState {
	@Override
	public State getState() {
		return State.FAREWELL;
	}

	@Override
	public State process() {
		System.out.println("-- Hope you enjoyed, goodbye");
		return null;
	}
}
