/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.classes;

import com.redazz.openeyz.beans.WsUserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class ScheduledTasks {
    @Autowired
    WsUserMap wsUserMap;

    // used to clean up wsUserMap value from disconnected users
    @Scheduled(fixedRate = 5000)
//    @Scheduled(fixedRate = 3600000)
    public void reportCurrentTime() {
        wsUserMap.deleteClosed();
    }
}
