/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.PostService;
import com.redazz.openeyz.services.UserService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kyzer
 */
@RestController
@RequestMapping("api")
public class MainController {

    @Autowired UserService us;
    @Autowired PostService ps;

    @GetMapping
    public ResponseEntity<String> authSuccess(HttpServletResponse response) {
        Map<String, String> json = new HashMap<>();
        return new ResponseEntity<>(Define.MESSAGE_AUTH_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("auth-failure")
    public ResponseEntity<String> authError(HttpServletRequest request) {
        String username = request.getParameter("username");
        String message;
        try {
            us.findById(username).get();
            message = Define.MESSAGE_ERROR_PASSWORD;
        }
        catch (Exception e) {
            message = Define.MESSAGE_ERROR_USERNAME;
        }

        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("publication")
    public ResponseEntity<List<Post>> getAllPost() {

        return new ResponseEntity<>(ps.findAll(), HttpStatus.OK);
    }
    @PostMapping("publication")
    public ResponseEntity<String> test(@RequestParam String data, @CookieValue Cookie USERID) {
        String username = USERID.getValue();
        String message;
        try {
            Users user = us.findById(username).get();
            ps.save(new Post(data, user));
            message = Define.MESSAGE_POST_SUCCESS;
        }
        catch (Exception e) {
            message = Define.MESSAGE_ERROR_POST;
//            json.put("message", e.getMessage());
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    @GetMapping("image")
    public ResponseEntity<ByteArrayResource> downloadImage(@RequestParam(required = true) String img) throws IOException {
        final ByteArrayResource image = new ByteArrayResource(Files.readAllBytes(Paths.get(Define.SERVER_ROOT_ASSETS_DIRECTORY + "/" + img)));
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
    @PostMapping("image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam(name = "file", required = true) MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        File dest = new File(Define.SERVER_ROOT_ASSETS_DIRECTORY + "/" + filename);
        Map<String, String> json = new HashMap<>();

        file.transferTo(dest);
        //must return json object type with url according CKEditor
        json.put("url", Define.SERVER_DOWNLOAD_IMAGE_URL + filename);

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
