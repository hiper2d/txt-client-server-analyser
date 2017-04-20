package com.hiper2d.terminal.state.impl;

import com.hiper2d.domain.scan.ScanReport;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.stomp.StompClient;
import com.hiper2d.terminal.state.AbstractBasicState;
import com.hiper2d.terminal.state.State;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PendingState extends AbstractBasicState {
	private StompClient stompClient;

	@Autowired
	public PendingState(StompClient stompClient) {
		this.stompClient = stompClient;
	}

	@Override
	public State getState() {
		return State.PENDING;
	}

	@Override
	public State process() {
		State nextState = State.WELCOME;
		StompSession.Subscription subscr = stompClient.subscribeToResponse();
		try {
			ScanResult resultMessage = stompClient.readResponseWithTimeout();
			if (resultMessage == null) {
				System.out.println("-- Error occurred: Didn't manage to get a response with unknown reason\n");
			} else {
				if (resultMessage.isError()) {
					log.error(resultMessage.getMessage());
				}
				System.out.println("\n-- Report is generated:");
				ScanReport report = resultMessage.getReport();
				System.out.println("**** LONG FILES **** ");
				System.out.println(report.getBigFilesRoot());
				System.out.println("**** SHORT FILES ****");
				System.out.println(report.getSmallFilesRoot());
				System.out.println();
				nextState = State.CONNECTED;
			}
		} catch (InterruptedException e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			System.out.println("-- Error occurred: Server is responding too long");
		} finally {
			subscr.unsubscribe();
		}
		return nextState;
	}
}
