package com.hiper2d.util;

public interface ApiConstants {
	String DEFAULT_PATH = ".";
	String WEBSOCKET_ENDPOINT_NAME = "stomp";
	String WEBSOCKET_URL = "ws://{host}:{port}/stomp";
	String WEBSOCKET_REQUEST_DESTINATION = "/app/scan/";
	String WEBSOCKET_RESPONSE_TOPIC = "/topic/scan/";
	String SERVER_HOST = "localhost";
	int WEBSOCKET_TIMEOUT = 5;
	int SERVER_PORT = 9000;
}
