/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.models.Comment;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.CommentService;
import com.redazz.openeyz.services.PostService;
import com.redazz.openeyz.services.UserService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.apache.catalina.connector.Response;
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

    @Autowired
    UserService us;
    @Autowired
    PostService ps;
    @Autowired
    CommentService cs;
    @Autowired
    JwTokenUtils jwt;

    @GetMapping
    public ResponseEntity<Map<String, Object>> authSuccess(@CookieValue(required = true) Cookie USERID) {
        String username = USERID.getValue();
        Map<String, Object> json = new HashMap<>();
        Users user = us.findById(username).get();
        String token = jwt.encode(username);

        json.put("token", token);
        json.put("user", user);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @PostMapping("auth-failure")
    public ResponseEntity<String> authError(HttpServletRequest request) {
        String username = request.getParameter("username");
        String message;
        HttpStatus status;
        try {
            us.findById(username).get();
            message = Define.MESSAGE_ERROR_PASSWORD;
            status = HttpStatus.UNAUTHORIZED;
        }
        catch (Exception e) {
            message = Define.MESSAGE_ERROR_USERNAME;
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(message, status);
    }

    @GetMapping("publication")
    public ResponseEntity<List<Object>> getAllPost() {
        Map<String, Object> json = new HashMap<>();
        List<Object> list = new ArrayList<>();
        HttpStatus status;

        try {
            for (Tuple t : ps.getAll()) {
                json.put("post", t.get(0));
                json.put("creation", t.get(1));
                list.add(json);
                json = new HashMap<>();
            }
            status = HttpStatus.OK;
        }
        catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(list, status);
    }
    @Transactional
    @PostMapping("publication")
    public ResponseEntity<String> postPublication(@RequestParam(required = true) String post, @CookieValue(required = true) Cookie USERID) {
        String username = USERID.getValue();
        String message;
        HttpStatus status;
        try {
            Users user = us.findById(username).get();
            ps.save(new Post(post, user));
            message = Define.MESSAGE_POST_SUCCESS;
            status = HttpStatus.CREATED;
        }
        catch (Exception e) {
            message = Define.MESSAGE_ERROR_POST;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(message, status);
    }

    @GetMapping("comment")
    public ResponseEntity<List<Object>> getAllComment(@RequestParam(required = true) long postId) {
        Map<String, Object> json = new HashMap<>();
        List<Object> list = new ArrayList<>();
        HttpStatus status;

        try {
            for (Tuple t : cs.getAllFromPost(postId)) {
                json.put("comment", t.get(0));
                json.put("creation", t.get(1));
                list.add(json);
                json = new HashMap<>();
            }
            status = HttpStatus.OK;
        }
        catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(list, status);
    }
    @Transactional
    @PostMapping("comment")
    public ResponseEntity<String> postComment(@RequestParam(required = true) String comment, @RequestParam(required = true) long postId, @CookieValue(required = true) Cookie USERID) {
        Post post = ps.findById(postId).get();
        Users user = us.findById(USERID.getValue()).get();
        Comment com = new Comment(comment, post, user);
        cs.save(com);
        
        return new ResponseEntity<>("Comment successfully created", HttpStatus.CREATED);
    }

    @GetMapping("image")
    public ResponseEntity<ByteArrayResource> downloadImage(@RequestParam(required = true) String img) throws IOException {
        ByteArrayResource image;
        HttpStatus status;
        try {
            image = new ByteArrayResource(Files.readAllBytes(Paths.get(Define.ASSETS_DIRECTORY + "/" + img)));
            status = HttpStatus.OK;
        }
        catch (IOException e) {
            image = null;
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(image, status);
    }
    @PostMapping("image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam(name = "upload", required = true) MultipartFile file) throws IOException {
        Map<String, String> json = new HashMap<>();
        HttpStatus status;
        try {
            String filename = file.getOriginalFilename();
            File dest = new File(Define.ASSETS_DIRECTORY + "/" + filename);

            file.transferTo(dest);
            //must return json object type with url according CKEditor config
            json.put("url", Define.DOWNLOAD_IMAGE_URL + filename);
            status = HttpStatus.CREATED;
        }
        catch (IOException | IllegalStateException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(json, status);
    }
}
