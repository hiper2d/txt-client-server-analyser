package com.hiper2d.controller;

import com.hiper2d.controller.future.ScanListenableFuture;
import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.scan.ScanRequest;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.exception.ServerException;
import com.hiper2d.service.ScanService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.net.URISyntaxException;

@Log4j2
@Controller
public class ScanController extends BaseStompController {
	private ScanService scanService;
	private String sessionId;

	@Autowired
	public ScanController(SimpMessageSendingOperations messagingTemplate, ScanService scanService) {
		this.messagingTemplate = messagingTemplate;
		this.scanService = scanService;
	}

	@MessageMapping("/scan/{sessionId}")
	public void scan(@DestinationVariable String sessionId, ScanRequest request) {
		this.sessionId = sessionId;
		ListenableFuture<Directory> future =  scanService.scan(request.getPath(), createEmitter(sessionId));
		future.addCallback(new ScanListenableFuture(sessionId, messagingTemplate));
	}

	@MessageExceptionHandler(ServerException.class)
	public void handleException(ServerException exception) {
		send(sessionId, ScanResult.builder().error(true).message(exception.getMessage()).completed(true).build());
	}
}
