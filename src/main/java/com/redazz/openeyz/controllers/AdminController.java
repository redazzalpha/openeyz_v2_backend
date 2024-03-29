/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    Initiator Initiator;

    @PatchMapping("state")
    public ResponseEntity<Users> updateState(@RequestParam(required = true) boolean state, @RequestParam(required = true) String username) {
        if (!username.equals(Initiator.getUsername())) {
            us.updateState(state, username);
        }
        return new ResponseEntity<>(us.findById(username).get(), HttpStatus.OK);
    }
    @PatchMapping("role")
    public ResponseEntity<Users> updateRole(@RequestParam(required = true) String roleName, @RequestParam(required = true) String username) {
        if (!username.equals(Initiator.getUsername())) {
            us.updateRole(roleName, username);
        }
        return new ResponseEntity<>(us.findById(username).get(), HttpStatus.OK);
    }

}
