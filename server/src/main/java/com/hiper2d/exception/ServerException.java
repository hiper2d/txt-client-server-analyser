package com.hiper2d.exception;

public class ServerException extends RuntimeException {
	public ServerException(Throwable exception) {
		super(exception);
	}
}
