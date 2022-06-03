/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.redazz.openeyz.beans.Encoder;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.models.Comment;
import com.redazz.openeyz.models.Likes;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.CommentService;
import com.redazz.openeyz.services.LikesService;
import com.redazz.openeyz.services.PostService;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    LikesService ls;
    @Autowired
    JwTokenUtils jwt;
    @Autowired
    Encoder encoder;

    @GetMapping
    public ResponseEntity<Map<String, Object>> authSuccess(@CookieValue(required = true) Cookie USERID) {

        // TODO: try to find solution to block this root on try to access when authentified
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
    public ResponseEntity<List<Object>> getAllPost(@CookieValue(required = true) Cookie USERID) {
        List<Object> list = new ArrayList<>();
        Post post;
        boolean userLike;
        HttpStatus status;

        try {
            for (Tuple t : ps.getAll()) {
                Map<String, Object> json = new HashMap<>();
                post = (Post) (t.get(0));
                userLike = ls.getUserlikePost(post.getId(), USERID.getValue());

                json.put("post", post);
                json.put("creation", t.get(1));
                json.put("commentCount", t.get(2));
                json.put("likeCount", t.get(3));
                json.put("userLike", userLike);

                list.add(json);
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

    @Transactional
    @PostMapping("like")
    public ResponseEntity<String> postLike(@RequestParam(required = true) long postId, @CookieValue(required = true) Cookie USERID) {
        Users author = us.findById(USERID.getValue()).get();
        Post post = ps.findById(postId).get();
        String message;
        HttpStatus status;

        if (!ls.getUserlikePost(postId, author.getUsername())) {

            Likes like = new Likes(post, author);
            ls.save(like);
            message = "Like successfully added";
            status = HttpStatus.CREATED;
        }
        else {
            Likes like = ls.findByAuthorAndPost(author, post).get();
            ls.delete(like);
            message = "Like successfully removed";
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(message, status);
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
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam(required = true) MultipartFile file) {
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

    @PatchMapping("user/lname")
    public ResponseEntity<String> patchLname(@RequestParam(required = true, name = "data") String lname, @CookieValue(required = true) Cookie USERID) {
        us.updateLname(lname, USERID.getValue());
        return new ResponseEntity<>("Last name successfully modified", HttpStatus.OK);
    }

    @PatchMapping("user/name")
    public ResponseEntity<String> patchName(@RequestParam(required = true, name = "data") String name, @CookieValue(required = true) Cookie USERID) {
        us.updateName(name, USERID.getValue());
        return new ResponseEntity<>("Name successfully modified", HttpStatus.OK);
    }

    @PatchMapping("description")
    public ResponseEntity<String> patchDescription(@RequestParam(required = true) String description, @CookieValue(required = true) Cookie USERID) {
        us.updateDescription(description, USERID.getValue());
        return new ResponseEntity<>("Description successfully modified", HttpStatus.OK);
    }

    @PatchMapping("user/password")
    public ResponseEntity<String> patchPassword(@RequestParam(required = true) String password, @RequestParam(required = true) String password1, @CookieValue(required = true) Cookie USERID) {

        String hash, message = "Invalid. Password does not match with the current";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Users user = us.findById(USERID.getValue()).get();

        if (user == null) {
            message = "User was not found!";
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(message, status);
        }

        boolean match = encoder.matches(password, user.getPassword());

        if (match) {
            hash = encoder.encode(password);
            us.updatePassword(hash, USERID.getValue());
            message = "Password was successfully modified";
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(message, status);
    }

    @PostMapping("user/img")
    public ResponseEntity<String> posthUserImg(@RequestParam(required = true) MultipartFile file, @CookieValue(required = true) Cookie USERID) {
        HttpStatus status;
        String message;
        try {
            String filename = file.getOriginalFilename();
            File dest = new File(Define.ASSETS_USER_DIRECTORY + "/" + filename);

            file.transferTo(dest);
            us.updateImg(Define.DOWNLOAD_IMAGE_URL + filename, USERID.getValue());

            message = Define.DOWNLOAD_IMAGE_URL + filename;
            status = HttpStatus.CREATED;
        }
        catch (IOException | IllegalStateException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = e.getMessage();
        }
        return new ResponseEntity<>(message, status);
    }

    // TODO: got to check better security here and everywhere when perform any action
    @DeleteMapping("user/delete")
    public ResponseEntity<Map<String, Object>> deleteAccount(@CookieValue(required = true) Cookie USERID, @CookieValue(required = true) Cookie JSESSIONID, HttpServletResponse response) {
        String username = USERID.getValue();
        ResponseCookie jsessionCookie = ResponseCookie.from(JSESSIONID.getName(), JSESSIONID.getValue())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .domain("localhost")
                .build();
        ResponseCookie userIdCookie = ResponseCookie.from(USERID.getName(), USERID.getValue())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .domain("localhost")
                .build();
        
        Map<String, Object> json = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        
        String token = jwt.encode();
        json.put("token", token);

        headers.add(HttpHeaders.SET_COOKIE, jsessionCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, userIdCookie.toString());
        
        us.deleteById(username);
        
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    // TODO: got to check for username modification cause need change cookie from server according the new username, does not work for the moment
    @PatchMapping("user/username")
    public ResponseEntity<String> patchUsername(@RequestParam(required = true, name = "data") String username, @CookieValue(required = true) Cookie USERID, HttpServletResponse response) {
        us.updateUsername(username, USERID.getValue());
        USERID.setValue(username);
        return new ResponseEntity<>("Username successfully modified", HttpStatus.OK);
    }

}
