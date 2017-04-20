package com.hiper2d.config;

import com.hiper2d.util.ApiConstants;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@Profile("test")
@EnableWebSocketMessageBroker
public class WebSocketTestConfig extends AbstractWebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(ApiConstants.WEBSOCKET_ENDPOINT_NAME)
				.setAllowedOrigins("*")
				.withSockJS();
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> container.setPort(ApiConstants.SERVER_PORT);
	}
}
