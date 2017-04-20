package com.hiper2d.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiper2d.domain.scan.ScanRequest;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.exception.ServerException;
import com.hiper2d.util.ApiConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScanControllerTest {
	private static final String DATA_PATH = "src/test/resources/data";

	private BlockingQueue<ScanResult> responseQueue;
	private WebSocketStompClient stompClient;
	private ObjectMapper mapper;

	@LocalServerPort
	int port;

	@Before
	public void setup() {
		mapper = new ObjectMapper();
		responseQueue = new LinkedBlockingQueue<>();
		stompClient = new WebSocketStompClient(
				new SockJsClient(
						Collections.singletonList(
								new WebSocketTransport(
										new StandardWebSocketClient()
								)
						)
				)
		);
	}

	@Test
	public void messageWithBigReport() throws Exception {
		StompSession session = connect().get();
		session.subscribe(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + session.getSessionId(), new DefaultStompFrameHandler());
		session.send(ApiConstants.WEBSOCKET_REQUEST_DESTINATION + session.getSessionId(), createRequestMessage(""));
		ScanResult response = waitForFinalResponse();
		assertTrue(response.isCompleted());
	}

	@Test
	public void receiveValidMessageFromServer() throws Exception {
		StompSession session = connect().get();
		session.subscribe(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + session.getSessionId(), new DefaultStompFrameHandler());
		session.send(ApiConstants.WEBSOCKET_REQUEST_DESTINATION + session.getSessionId(), createRequestMessage());
		ScanResult response = waitForFinalResponse();
		assertTrue(response.isCompleted());
		assertNotNull(response.getReport().getBigFilesRoot());
		assertNotNull(response.getReport().getSmallFilesRoot());
	}

	@Test
	public void invalidPathExceptionIsHandled() throws Exception {
		StompSession session = connect().get();
		session.subscribe(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + session.getSessionId(), new DefaultStompFrameHandler());
		session.send(ApiConstants.WEBSOCKET_REQUEST_DESTINATION + session.getSessionId(), createRequestMessage("completely/invalid/path"));
		ScanResult response = waitForFinalResponse();
		assertTrue(response.isCompleted());
		assertTrue(response.isError());
		assertTrue(response.getMessage().contains("NoSuchFileException"));
	}

	private ListenableFuture<StompSession> connect() throws InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
		return stompClient.connect(
				ApiConstants.WEBSOCKET_URL,
				new WebSocketHttpHeaders(),
				new StompSessionHandlerAdapter() {
				},
				ApiConstants.SERVER_HOST,
				port
		);
	}

	private byte[] createRequestMessage() throws JsonProcessingException {
		return createRequestMessage(DATA_PATH);
	}

	private byte[] createRequestMessage(String path) throws JsonProcessingException {
		ScanRequest request = new ScanRequest(path);
		return mapper.writeValueAsBytes(request);
	}

	private ScanResult waitForFinalResponse() throws InterruptedException {
		ScanResult response = responseQueue.poll(1, SECONDS);
		while (!response.isCompleted()) {
			response = responseQueue.poll(1, SECONDS);
		}
		return response;
	}

	class DefaultStompFrameHandler implements StompFrameHandler {
		@Override
		public Type getPayloadType(StompHeaders stompHeaders) {
			return byte[].class;
		}

		@Override
		public void handleFrame(StompHeaders stompHeaders, Object payload) {
			try {
				ScanResult response = mapper.readValue((byte[]) payload, ScanResult.class);
				responseQueue.offer(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}