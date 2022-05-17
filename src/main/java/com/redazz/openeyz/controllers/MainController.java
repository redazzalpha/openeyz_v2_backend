/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kyzer
 */
@RestController
@RequestMapping("api")
public class MainController {
    
    @GetMapping
    public ResponseEntity<Map<String, String>> getAll(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        map.put("Content", "this is content here");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
