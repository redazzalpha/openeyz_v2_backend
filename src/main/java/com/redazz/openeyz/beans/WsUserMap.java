/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class WsUserMap  {
    private ListMultimap<String, String> multimap = ArrayListMultimap.create();
    
    public ListMultimap<String, String> getMap() {
        return multimap;
    }
    public List<String> getValues(String key) {
        return multimap.get(key);
    }
    public void addUser(String username, String wsUsername) {
        multimap.put(username, wsUsername);
    }
    public boolean contains(String key) {
        return multimap.containsKey(key);
    }
    public void remove(String key, String value) {
        if(multimap.containsEntry(key, value))
        multimap.remove(key, value);
    }
    public void removeAll(String key) {
        if(multimap.containsKey(key))
        multimap.removeAll(key);
    }
    public void showList() {
        for(Map.Entry s : multimap.entries()) {
        System.out.println("-- key: " + s.getKey() + " -- value: " + s.getValue());
        }
    }
}
