    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.services.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
@RequestMapping("api")
public class MainController {
    
    @Autowired UserService us;
    
    @GetMapping
    public ResponseEntity<Map<String, String>> authSuccess(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("content", "this is content here");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    @PostMapping("auth-failure")
    public ResponseEntity<Map<String, String>>  authError(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        String username = request.getParameter("username");
        
        try {
            us.findById(username).get();
            map.put("message", Define.MESSAGE_ERROR_PASSWORD);
        }
        catch(Exception e) { map.put("message", Define.MESSAGE_ERROR_USERNAME); }
        
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
    }
}
