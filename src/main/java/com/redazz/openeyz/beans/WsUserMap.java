/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 *
 * @author kyzer
 */
@Component
public class WsUserMap {
    private ListMultimap<String, WebSocketSession> multimap = ArrayListMultimap.create();

    public ListMultimap<String, WebSocketSession> getMap() {
        return multimap;
    }
    public List<WebSocketSession> getValues(String key) {
        return multimap.get(key);
    }
    public boolean addUser(String username, WebSocketSession wsSession) {
        boolean added = false;
        if (username != null && wsSession != null) {
            added = multimap.put(username, wsSession);
        }
        return added;
    }
    public boolean contains(String key) {
        return multimap.containsKey(key);
    }
    public boolean remove(String key, WebSocketSession wsSession) {
        boolean removed = false;
        if (multimap.containsEntry(key, wsSession)) {
            removed = multimap.remove(key, wsSession);
        }
        return removed;
    }
    public boolean removeUser(String key, WebSocketSession wsSession) {
        boolean removed = false;
        if (multimap.containsEntry(key, wsSession)) {
            removed = multimap.remove(key, wsSession);
        }
        return removed;
    }
    public void removeAll(String key) {
        if (multimap.containsKey(key)) {
            multimap.removeAll(key);
        }
    }
    public void clear() {
        multimap.clear();
    }
    public void showList() {
        for (Map.Entry s : multimap.entries()) {
            System.out.println("-- key: " + s.getKey() + " -- wsSession: " + ((WebSocketSession)(s.getValue())).getPrincipal().getName());
        }
    }
    public void deleteClosed() {
        WebSocketSession session;
//        for (Map.Entry s : multimap.entries()) {
//            session = (WebSocketSession) s.getValue();
//            if(!(session.isOpen()))
//                multimap.remove(s.getKey(), s.getValue());
//        }
        Iterator<Map.Entry<String, WebSocketSession>> it = multimap.entries().iterator();
        while(it.hasNext()) {
            Map.Entry<String, WebSocketSession> map = it.next();
            session = map.getValue();
            if(!(session.isOpen()))
                it.remove();
        }
    }
}
