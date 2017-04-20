package com.hiper2d.terminal;

import com.hiper2d.exception.ClientException;
import com.hiper2d.terminal.state.AbstractBasicState;
import com.hiper2d.terminal.state.State;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class StateManager {
	@Getter
	private State currentState;
	private List<AbstractBasicState> states;
	private AbstractBasicState currentStateService;

	@Autowired
	public StateManager(List<AbstractBasicState> states) {
		this.states = states;
	}

	void processCurrentState() {
		try {
			updateCurrentState(currentStateService.process());
		} catch (ClientException ex) {
			log.error(ex.getMessage());
		}
	}

	void updateCurrentState(State state) {
		this.currentState = state;
		states.stream().filter(s -> s.getState() == currentState)
				.findFirst()
				.ifPresent(s -> currentStateService = s);
		//todo: what if it's not present?
	}

	boolean isActive() {
		return getCurrentState() != null;
	}
}
