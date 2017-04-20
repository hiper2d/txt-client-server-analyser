package com.hiper2d.terminal;

import com.hiper2d.terminal.state.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalManager {
	private StateManager stateManager;

	@Autowired
	public TerminalManager(StateManager stateManager) {
		this.stateManager = stateManager;
	}

	public void start() {
		System.out.println();
		stateManager.updateCurrentState(State.WELCOME);
		while (stateManager.isActive()) {
			stateManager.processCurrentState();
		}
	}
}
