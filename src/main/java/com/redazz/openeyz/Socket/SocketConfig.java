/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.Socket;

import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
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
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    UserService us;
    @Autowired
    Initiator initiator;
    @Autowired
    JwTokenUtils jwt;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker(Define.WS_SUBSCRIBE_URL);
        registry.setApplicationDestinationPrefixes(Define.ROOT_URL);

    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint(Define.WS_END_POINT_URL).setAllowedOriginPatterns("*");
        registry.addEndpoint(Define.WS_END_POINT_URL).setAllowedOriginPatterns("*").withSockJS().setSessionCookieNeeded(false);
////        registry.addEndpoint("/gs-guide-websocket").setAllowedOriginPatterns(Define.CLIENT_DOMAIN).withSockJS().setSessionCookieNeeded(false);
//        // Handle exceptions in interceptors and Spring library itself.
//        // Will terminate a connection and send ERROR frame to the client.
//        registry.setErrorHandler(new StompSubProtocolErrorHandler() {
//
//        });

    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(new SocketInterceptor(us, initiator, jwt, secret));
    }

}
