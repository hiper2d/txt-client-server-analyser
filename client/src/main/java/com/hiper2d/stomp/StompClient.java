package com.hiper2d.stomp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiper2d.domain.scan.ScanReport;
import com.hiper2d.domain.scan.ScanRequest;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.exception.ClientException;
import com.hiper2d.util.ApiConstants;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Log4j2
@Service
public class StompClient {
	@Getter
	private StompSession currentSession;
	private ServerResponseHandler handler;

	@PostConstruct
	public void initSession() {
		handler = new ServerResponseHandler();
	}

	public void connect() {
		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);
		SockJsClient sockJsClient = getSockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		try {
			currentSession = connect(stompClient).get();
		} catch (InterruptedException | ExecutionException e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			log.error("Cannot establish WebSocket connection");
		}
	}

	public void disconnect() {
		if (currentSession != null) {
			currentSession.disconnect();
		}
	}

	public void sendRequest(String path) {
		ObjectMapper mapper = new ObjectMapper();
		ScanRequest message = new ScanRequest(path);
		try {
			currentSession.send(ApiConstants.WEBSOCKET_REQUEST_DESTINATION + currentSession.getSessionId(), mapper.writeValueAsBytes(message));
		} catch (JsonProcessingException e) {
			if (log.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new ClientException("Cannot convert request message to JSON format");
		}
	}

	public StompSession.Subscription subscribeToResponse() {
		return currentSession.subscribe(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + currentSession.getSessionId(), handler);
	}

	public ScanResult readResponseWithTimeout() throws InterruptedException {
		return handler.getBlockingQueue().poll(ApiConstants.WEBSOCKET_TIMEOUT, TimeUnit.SECONDS);
	}

	private ListenableFuture<StompSession> connect(WebSocketStompClient stompClient) {
		return stompClient.connect(
				ApiConstants.WEBSOCKET_URL,
				new WebSocketHttpHeaders(),
				new LoggableStompSessionHandlerAdapter(),
				ApiConstants.SERVER_HOST,
				ApiConstants.SERVER_PORT
		);
	}

	private SockJsClient getSockJsClient(List<Transport> transports) {
		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
		return sockJsClient;
	}
}
