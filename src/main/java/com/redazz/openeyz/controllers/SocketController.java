/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.classes.InputMessage;
import com.redazz.openeyz.classes.OutputMessage;
import com.redazz.openeyz.defines.Define;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
public class SocketController {

    @MessageMapping("/signal/update")
    @SendTo(Define.WS_SUBSCRIBE_URL)
    public OutputMessage messageHandler(InputMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new OutputMessage(message.getContent());

    }

}
