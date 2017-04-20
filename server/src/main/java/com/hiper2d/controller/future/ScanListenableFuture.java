package com.hiper2d.controller.future;

import com.hiper2d.domain.analyze.Directory;
import com.hiper2d.domain.scan.ScanResult;
import com.hiper2d.util.ApiConstants;
import com.hiper2d.util.ReportGenerator;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class ScanListenableFuture implements ListenableFutureCallback<Directory> {
	private SimpMessageSendingOperations messagingTemplate;
	private String sessionId;

	public ScanListenableFuture(String sessionId, SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.sessionId = sessionId;
	}

	@Override
	public void onFailure(Throwable ex) {
		messagingTemplate.convertAndSend(
				ApiConstants.WEBSOCKET_RESPONSE_TOPIC + sessionId,
				ScanResult.builder().error(true).message("Error occurred: " + ex.getMessage()).completed(true).build()
		);
	}

	@Override
	public void onSuccess(Directory root) {
		ScanResult res = ScanResult.builder().message("Completed scanning").report(ReportGenerator.generate(root)).completed(true).build();
		messagingTemplate.convertAndSend(ApiConstants.WEBSOCKET_RESPONSE_TOPIC + sessionId, res);
	}
}
