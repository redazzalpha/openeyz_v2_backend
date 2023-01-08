/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.Socket;

import com.redazz.openeyz.defines.Define;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 *
 * @author kyzer
 */
@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
//        registry.enableSimpleBroker("/client");
//        registry.setApplicationDestinationPrefixes("/api");

        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");

    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
//        registry.addEndpoint("/websocket").withSockJS();

        registry.addEndpoint("/gs-guide-websocket").setAllowedOriginPatterns(Define.CLIENT_DOMAIN).withSockJS().setSessionCookieNeeded(false);

    }
    
    
}