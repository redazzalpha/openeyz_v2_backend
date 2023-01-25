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
public class WsUserMap {
    private ListMultimap<String, String> multimap = ArrayListMultimap.create();

    public ListMultimap<String, String> getMap() {
        return multimap;
    }
    public List<String> getValues(String key) {
        return multimap.get(key);
    }
    public boolean addUser(String username, String wsUsername) {
        boolean added = false;
        if (username != null && wsUsername != null) {
            added = multimap.put(username, wsUsername);
        }
        return added;
    }
    public boolean contains(String key) {
        return multimap.containsKey(key);
    }
    public boolean remove(String key, String value) {
        boolean removed = false;
        if (multimap.containsEntry(key, value)) {
            removed = multimap.remove(key, value);
        }
        return removed;
    }
    public boolean removeUser(String key, String value) {
        boolean removed = false;
        if (multimap.containsEntry(key, value)) {
            removed = multimap.remove(key, value);
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
            System.out.println("-- key: " + s.getKey() + " -- value: " + s.getValue());
        }
    }
}
