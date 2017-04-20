package com.hiper2d.stomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiper2d.domain.scan.ScanResult;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Log4j2
public class ServerResponseHandler implements StompFrameHandler {
	@Getter
	private boolean isCompleted = false;
	@Getter
	private BlockingQueue<ScanResult> blockingQueue = new LinkedBlockingQueue<>();

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return byte[].class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			ScanResult response = mapper.readValue((byte[]) payload, ScanResult.class);
			if (response.isCompleted()) {
				isCompleted = true;
				blockingQueue.offer(response);
			} else {
				System.out.println("-- Received a message from the server: " + response.getMessage());
			}
		} catch (Exception e) {
			log.error("Cannot convert response payload to EchoResponse object");
		}
	}
}
