/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    UserService us;
    @PatchMapping("state")
    public ResponseEntity<Users> updateState(@CookieValue(required = true) Cookie USERID, @RequestParam(required = true) boolean state, @RequestParam(required = true) String username) {
        us.updateState(state, username);
        return new ResponseEntity<>(us.findById(username).get(), HttpStatus.OK);
    }
    @PatchMapping("role")
    public ResponseEntity<Users> updateRole(@CookieValue(required = true) Cookie USERID, @RequestParam(required = true) String roleName, @RequestParam(required = true) String username) {
        us.updateRole(roleName, username);
        return new ResponseEntity<>(us.findById(username).get(), HttpStatus.OK);
    }

}
