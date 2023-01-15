/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.Socket;

import com.redazz.openeyz.Exceptions.ForbiddenException;
import com.redazz.openeyz.Exceptions.NoUserFoundException;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.WsUserMap;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Optional;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 *
 * @author kyzer
 */
public class SocketInterceptor implements ChannelInterceptor {
    private final UserService us;
    private final Initiator initiator;
    private final JwTokenUtils jwt;
    private final String secret;
    private final WsUserMap wsUserMap;

    public SocketInterceptor(UserService us, Initiator initiator, JwTokenUtils jwt, String secret, WsUserMap wsUserMap) {
        this.us = us;
        this.initiator = initiator;
        this.jwt = jwt;
        this.secret = secret;
        this.wsUserMap = wsUserMap;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            String authHeaderValue = accessor.getNativeHeader("Authorization").get(0);
            String token = authHeaderValue.split("Bearer ")[1];

            Jws<Claims> jws = jwt.decode(token, secret);
            String usernameToken = jws.getBody().get("username").toString();
            Optional<Users> optUser = us.findById(usernameToken);
            
            if (optUser.isEmpty()) {
                throw new NoUserFoundException("user value is not present");
            }

            Users currentUser = optUser.get();
            initiator.init(currentUser);
            boolean isBanned = !initiator.getState();

            if (isBanned) {
                throw new ForbiddenException("your account has been disabled");
            }

            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                wsUserMap.addUser(currentUser.getUsername(), accessor.getUser().getName());
                wsUserMap.showList();
            }
            
            return ChannelInterceptor.super.preSend(message, channel);
        }
        catch (ForbiddenException | NoUserFoundException | NullPointerException e) {
            return null;

        }
    }

}
