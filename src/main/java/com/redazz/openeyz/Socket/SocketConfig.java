/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.Socket;

import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.WsUserMap;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.handlers.WSHandshakeHandler;
import com.redazz.openeyz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

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
    @Autowired
    WsUserMap wsUserMap;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        registry.enableSimpleBroker(Define.WEBSOCKET_URL);
        registry.setApplicationDestinationPrefixes(Define.WEBSOCKET_URL);
        registry.setUserDestinationPrefix("/users");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint(Define.WS_END_POINT_URL).setAllowedOriginPatterns("*").setHandshakeHandler(new WSHandshakeHandler());
        registry.addEndpoint(Define.WS_END_POINT_URL).setAllowedOriginPatterns("*").setHandshakeHandler(new WSHandshakeHandler()).withSockJS().setSessionCookieNeeded(false);
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(new SocketInterceptor(us, initiator, jwt, secret, wsUserMap));
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
        registry.addDecoratorFactory((WebSocketHandler handler) -> new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                super.afterConnectionEstablished(session);
                
                boolean a = wsUserMap.addUser(initiator.getUsername(), session);
                System.out.println("------------------------------- after connection established -> try to add: "  + initiator.getUsername()   + " - " + session.getPrincipal().getName());
                System.out.println("------------------------------- after connection established -> add: " + a);
                System.out.println("------------------------------- after connection established -> show list");
                wsUserMap.showList();
                System.out.println("------------------------------- after connection established -> show list");
                if(!a) session.close();
            }
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                super.afterConnectionClosed(session, closeStatus);
                
                boolean r = wsUserMap.removeUser(initiator.getUsername(), session);
                System.out.println("------------------------------- after connection closed -> try to removed: "  + initiator.getUsername()   + " - " + session.getPrincipal().getName());
                System.out.println("------------------------------- after connection closed -> removed: " + r);
                System.out.println("------------------------------- after connection closed -> show list"); 
                wsUserMap.showList();
                System.out.println("------------------------------- after connection closed -> show list"); 
                session.close();
            }
        });
    }

}
