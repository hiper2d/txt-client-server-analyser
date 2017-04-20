package com.hiper2d.stomp;

import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Log4j2
public class LoggableStompSessionHandlerAdapter extends StompSessionHandlerAdapter {
	public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
		log.debug("Stomp is now connected");
	}
}
