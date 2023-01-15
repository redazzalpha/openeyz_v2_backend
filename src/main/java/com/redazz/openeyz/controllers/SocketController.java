/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
public class SocketController {

    /** THIS CODE HAS BEEN DISABLED CAUSE  CODE IS USELESS **/
    /** CLIENT APP DON'T NEED TO SEND MESSAGE TO WEBSOCKET SERVER **/
    /** FOR MORE SECURITY ONLY THE SERVER CAN SEND MESSAGE TO CLIENT**/
    /** TO WARN CLIENT APP THAT UPDATE IS AVAILABLE**/
    
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/signal/update")
//    public void messageHandler(SimpMessageHeaderAccessor sha, @Payload String username) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        String message = "Hello from " + sha.getUser().getName();
//
//        simpMessagingTemplate.convertAndSendToUser(sha.getUser().getName(), Define.WEBSOCKET_URL + "/signal", message);
//    }
}
