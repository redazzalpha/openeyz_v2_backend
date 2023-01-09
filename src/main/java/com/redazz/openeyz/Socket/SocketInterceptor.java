/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.Socket;

import java.util.List;
import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author kyzer
 */
public class SocketInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String headervalue = accessor.getNativeHeader("Authorization").get(0);
        String token = headervalue.split("Bearer ")[1];
        System.out.println("------------------------------------------------------------- token: " + token);
        
        
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ preSend");

//        MessageHeaders headers = message.getHeaders();
//        MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
//        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
//            if (entry.getKey().equals("Authorization")) {
//                token = entry.getValue().toArray()[0].toString().split("Bearer ")[1];
//            }
//        }

        if (token != null) {

            // token check here //
            System.out.println("- token: " + token);
            return ChannelInterceptor.super.preSend(message, channel);
        }
        else {
            return null;
        }

    }

}
