/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.classes.InputMessage;
import com.redazz.openeyz.classes.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
public class SocketController {

//    @MessageMapping("/socket")
//    @SendTo("/client")
    @MessageMapping("/signal/update")
    @SendTo("/signal-update")
    public OutputMessage messageHandler(InputMessage message) throws Exception {
//        return new OutputMessage("Salut los ami de gauche");
        Thread.sleep(1000); // simulated delay
        return new OutputMessage("Hello, zizoupop comment va tu ?!");
//        return new OutputMessage("Hello, " + HtmlUtils.htmlEscape(message.getContent()) + "!");

    }

}
