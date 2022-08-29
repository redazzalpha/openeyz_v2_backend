/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.models.Role;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Data
@Component
public class Initiator {
    private String username;
    private String lname;
    private String name;
    private String password;
    private boolean state = true;
    private String description;
    private String avatarSrc;
    private boolean dark;
    private List<Role> roles = new ArrayList<>();
    private static Initiator user = null;

    private Initiator() {
    }

    public static Initiator get() {
        if (user == null) {
            user = new Initiator();
        }
        return user;
    }

    public boolean getState() {
        return state;
    }
    public boolean getDark() {
        return dark;
    }
}
