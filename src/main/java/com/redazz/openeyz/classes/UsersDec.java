/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.classes;

import com.redazz.openeyz.beans.Encryptor;
import com.redazz.openeyz.models.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author kyzer
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UsersDec extends AbstractUsers {
    private String lname;
    
    public UsersDec (Users user, Encryptor encryptor) {
        this.username = user.username;
        this.lname = new String(encryptor.decrypt(user.getLname()));
        this.name = user.name;
        this.password = user.password;
        this.roles = user.roles;
        this.state = user.state;
        this.description = user.description;
        this.avatarSrc = user.avatarSrc;
        this.dark = user.dark;   
    }    
}
