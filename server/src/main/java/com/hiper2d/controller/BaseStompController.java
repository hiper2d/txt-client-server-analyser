package com.hiper2d.controller;

import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.util.ApiConstants;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

abstract class BaseStompController {
	SimpMessageSendingOperations messagingTemplate;

	EmitterProcessor<String> createEmitter(String sessionId) {
		EmitterProcessor<String> stream = EmitterProcessor.<String>create().connect();
		Flux<String> flux = stream.doOnNext(s -> send(sessionId, ScanResult.builder().message(s).completed(false).build()));
		flux.subscribe();
		return stream;
	}

	void send(String sessionId, ScanResult result) {
		messagingTemplate.convertAndSend(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + sessionId, result);
	}
}
