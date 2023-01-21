/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.models.Role;
import com.redazz.openeyz.models.Users;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
@Data
public class Initiator {
    private String username;
    private byte[] lname;
    private String name;
    private String password;
    private String description;
    private String avatarSrc;
    private boolean dark;
    private boolean state = true;
    private List<Role> roles = new ArrayList<>();
    private Users user = new Users();

    public void init(Users user) {
        this.user = user;
        this.username = user.getUsername();
        this.lname = user.getLname();
        this.name  = user.getName();
        this.password = user.getPassword();
        this.description = user.getDescription();
        this.avatarSrc = user.getAvatarSrc();
        this.dark = user.getDark();
        this.state = user.getState();
        this.roles = user.getRoles();
    }
    public Users getUser() {
        return user;
    }
    public boolean getDark() {
        return dark;
    }
    public boolean getState() {
        return state;
    }
    public String getRole() {
        return roles.get(0).getRoleName().toString();
    }
}
