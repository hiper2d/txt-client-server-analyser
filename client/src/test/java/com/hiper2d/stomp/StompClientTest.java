package com.hiper2d.stomp;

import com.hiper2d.BaseStompTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StompClientTest extends BaseStompTest {
	@Autowired
	private StompClient stompClient;

	@Test
	public void connect() throws Exception {
		assertNull(stompClient.getCurrentSession());
		stompClient.connect();
		assertNotNull(stompClient.getCurrentSession());
	}
}