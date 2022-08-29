/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redazz.openeyz.beans.Encoder;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.handlers.ActionHandler;
import com.redazz.openeyz.models.Comment;
import com.redazz.openeyz.models.Likes;
import com.redazz.openeyz.models.Notif;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.CommentService;
import com.redazz.openeyz.services.LikesService;
import com.redazz.openeyz.services.NotifService;
import com.redazz.openeyz.services.PostService;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    NotifService ns;
    @Autowired
    JwTokenUtils jwt;
    @Autowired
    Encoder encoder;
    @Autowired
    ActionHandler actionHandler;

<<<<<<< HEAD
    @GetMapping
    public ResponseEntity<Map<String, Object>> authSuccess(HttpServletResponse response, @CookieValue(required = true) Cookie USERID, @CookieValue(required = true) Cookie JSESSIONID) {
        String username = USERID.getValue();
        Map<String, Object> json = new HashMap<>();
        Optional<Users> user = us.findById(username);

        if (user.isPresent()) {
            String token = jwt.encode(username, JSESSIONID.getValue());
            jwt.setExpiration(7);
            String refreshToken = jwt.encode(username, JSESSIONID.getValue());

            json.put("token", token);
            json.put("refreshToken", refreshToken);
            json.put("user", user.get());
        }

        return new ResponseEntity<>(json, HttpStatus.OK);
    }
=======
    private final Initiator initiator = Initiator.get();
>>>>>>> slave

    @PostMapping("auth-failure")
    public ResponseEntity<String> authFailure(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String message;
        HttpStatus status;
        try {
            Users currentUser = us.findById(username).get();
            if (!currentUser.getState()) {
                message = Define.MESSAGE_ERROR_BANNED;
                status = HttpStatus.UNAUTHORIZED;
            }
            else {
                message = Define.MESSAGE_ERROR_PASSWORD;
                status = HttpStatus.UNAUTHORIZED;
            }
        }
        catch (Exception e) {
            message = Define.MESSAGE_ERROR_USERNAME;
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(message, status);
    }
    @PostMapping("register-failure")
    public ResponseEntity<String> registerFailure(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>("user already exists", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("refresh")
<<<<<<< HEAD
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestParam(required = true) String refreshToken, @CookieValue(required = true) Cookie USERID, @CookieValue(required = true) Cookie JSESSIONID) {

        Map<String, Object> json = new HashMap<>();
        try {
            jwt.decode(refreshToken);
            String username = USERID.getValue();
            Optional<Users> user = us.findById(username);
            if (user.isPresent()) {
                String token = jwt.encode(username, JSESSIONID.getValue());
                json.put("token", token);
                json.put("refreshToken", refreshToken);
                json.put("user", user.get());
                return new ResponseEntity<>(json, HttpStatus.OK);
=======
    public ResponseEntity<Users> refreshToken(@RequestParam(required = true) String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            String token = request.getHeader("Authorization").split("Bearer ")[1];
            if (request.getHeader("Authorization") != null && token != null) {
                try {
                    jwt.decode(token);
                    String username = initiator.getUsername();
                    Optional<Users> user = us.findById(username);
                    if (user.isPresent()) {
                        response.addHeader("x-auth-token", token);
                        response.addHeader("x-refresh-token", refreshToken);
                        return new ResponseEntity<>(user.get(), HttpStatus.OK);
                    }
                }
                catch (ExpiredJwtException e) {
                    try {
                        jwt.decode(refreshToken);
                        String username = initiator.getUsername();
                        Optional<Users> user = us.findById(username);
                        if (user.isPresent()) {
                            String newToken = jwt.encode(username);
                            response.addHeader("x-auth-token", newToken);
                            response.addHeader("x-refresh-token", refreshToken);
                            return new ResponseEntity<>(user.get(), HttpStatus.OK);
                        }
                    }
                    catch (Exception ex) {
                        response.sendError(401, ex.getMessage());
                    }
                }
            }
            else {
                response.sendError(401, "bearer token was not found");
>>>>>>> slave
            }
        }
        catch (IOException ex) {
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @Transactional
    @GetMapping("publication")
    // TODO: modify return type object to json according unified convention 
    public ResponseEntity<List<Object>> getAllPost(@RequestParam(required = false) String authorId) {
        List<Object> list = new ArrayList<>();
        Post post;
        boolean userLike;
        HttpStatus status;
        List<Tuple> tuples;
        if (authorId == null) {
            tuples = ps.getAll();
        }
        else {
            tuples = ps.getAllFromUser(authorId);
        }

        try {
            for (Tuple t : tuples) {
                Map<String, Object> json = new HashMap<>();
                post = (Post) (t.get(0));
                userLike = ls.getUserlikePost(post.getId(), initiator.getUsername());

                json.put("post", post);
                json.put("commentCount", t.get(1));
                json.put("likeCount", t.get(2));
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
    @GetMapping("publication/limit")
    public ResponseEntity<List<Object>> getAllPostLimit(@RequestParam(required = false) String authorId, @RequestParam(required = true) int limit, @RequestParam(required = false) String creation) {
        List<Object> list = new ArrayList<>();
        Post post;
        boolean userLike;
        HttpStatus status;
        List<Tuple> tuples;

        if (authorId == null) {
            tuples = ps.getAllLimit(limit, creation);
        }
        else {
            tuples = ps.getAllFromUserLimit(authorId, limit, creation);
        }

        try {
            for (Tuple t : tuples) {
                Map<String, Object> json = new HashMap<>();
                post = (Post) (t.get(0));
                userLike = ls.getUserlikePost(post.getId(), initiator.getUsername());

                json.put("post", post);
                json.put("commentCount", t.get(1));
                json.put("likeCount", t.get(2));
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
    public ResponseEntity<String> postPublication(@RequestParam(required = true) String post) {
        String username = initiator.getUsername();
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
    @DeleteMapping("publication")
    public ResponseEntity<String> deletePublication(@RequestParam(required = true) long postId) {
        try {
            Post post = ps.findById(postId).get();
            return actionHandler.run(initiator.getUsername(), post, (idPost) -> {
                ps.deleteById(idPost);
                return null;
            });
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("comment")
    public ResponseEntity<List<Comment>> getAllComment(@RequestParam(required = true) long postId) {
        return new ResponseEntity<>(cs.getAllFromPost(postId), HttpStatus.OK);
    }
    @GetMapping("comment/limit")
    public ResponseEntity<List<Comment>> getAllCommentLimit(@RequestParam(required = true) long postId, @RequestParam(required = true) int limit, @RequestParam(required = false) String creation) {
        return new ResponseEntity<>(cs.getAllFromPostLimit(postId, limit, creation), HttpStatus.OK);
    }
    @Transactional
    @PostMapping("comment")
    public ResponseEntity<String> postComment(@RequestParam(required = true) String comment, @RequestParam(required = true) long postId) {
        Post post = ps.findById(postId).get();
        Users author = us.findById(initiator.getUsername()).get();
        Comment com = new Comment(comment, post, author);

        cs.save(com);

        Users owner = com.getPost().getAuthor();
        if (!author.getUsername().equals(owner.getUsername())) {
            Notif notif = new Notif(false, owner, com, author);
            ns.save(notif);
        }

        return new ResponseEntity<>("Comment successfully created", HttpStatus.CREATED);
    }
    @DeleteMapping("comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam(required = true) long commentId) {
        try {
            Comment comment = cs.findById(commentId).get();
            return actionHandler.run(initiator.getUsername(), comment, (idComment) -> {
                cs.deleteById(idComment);
                return null;
            });
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping("like")
    public ResponseEntity<String> postLike(@RequestParam(required = true) long postId) {
        Users author = us.findById(initiator.getUsername()).get();
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
    @GetMapping("like/count")
    public ResponseEntity<Integer> getCount(@RequestParam(required = true) long postId) {
        return new ResponseEntity<>(ls.getCount(postId), HttpStatus.OK);
    }

    // TODO: modify file name of image when on server to get inique image name because it may cause troubles
    // TODO: delete image from server when image removed from front end on cancel action
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

    @GetMapping("notif")
    public ResponseEntity<List<Notif>> getAllNotifs() {
        return new ResponseEntity<>(ns.getNotifsFromOwner(initiator.getUsername()), HttpStatus.OK);
    }
    @PatchMapping("notif")
    public ResponseEntity<String> readAllNotifs() {

        ns.readAllFromUser(initiator.getUsername());
        return new ResponseEntity<>("all notifications read successfully", HttpStatus.OK);
    }
    @DeleteMapping("notif")
    public ResponseEntity<String> deleteAllNotif() {
        ns.deleteAllFromUser(initiator.getUsername());
        return new ResponseEntity<>("all notifications has been deleted successfully", HttpStatus.OK);
    }
    @PatchMapping("notif/one")
    public ResponseEntity<String> readNotifOne(@RequestParam(required = true) long notifId) {

        try {
            Notif notif = ns.findById(notifId).get();
            return actionHandler.run(initiator.getUsername(), notif, (idNotif) -> {
                ns.readOneFromUser(idNotif);
                return null;
            });
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("notif/one")
    public ResponseEntity<String> deleteNotifOne(@RequestParam(required = true) long notifId) {
        try {
            Notif notif = ns.findById(notifId).get();
            return actionHandler.run(initiator.getUsername(), notif, (idNotif) -> {
                ns.deleteById(idNotif);
                return null;
            });
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("user")
    public ResponseEntity<Users> getUser() {
        return new ResponseEntity<>(us.findById(initiator.getUsername()).get(), HttpStatus.OK);
    }
    @GetMapping("user/simple")
    public ResponseEntity<List<Object>> getAllUsuersSimple() {
        return new ResponseEntity<>(us.getAllSimple(), HttpStatus.OK);
    }
    @Transactional
    @PatchMapping("user/dark")
    public ResponseEntity<String> modifyDark(@RequestParam(required = true) boolean dark) {
        us.updateDark(dark, initiator.getUsername());
        return new ResponseEntity<>("Dark theme has been successfully set", HttpStatus.OK);
    }
    @PatchMapping("user/lname")
    public ResponseEntity<String> modifyLname(@RequestParam(required = true, name = "data") String lname) {
        us.updateLname(lname, initiator.getUsername());
        return new ResponseEntity<>("Last name successfully modified", HttpStatus.OK);
    }
    @PatchMapping("user/name")
    public ResponseEntity<String> modifyName(@RequestParam(required = true, name = "data") String name) {
        us.updateName(name, initiator.getUsername());
        return new ResponseEntity<>("Name successfully modified", HttpStatus.OK);
    }
    @PatchMapping("user/description")
    public ResponseEntity<String> modifyDescription(@RequestParam(required = true) String description) {
        us.updateDescription(description, initiator.getUsername());
        return new ResponseEntity<>("Description successfully modified", HttpStatus.OK);
    }
    @PatchMapping("user/password")
    public ResponseEntity<String> modifyPassword(@RequestParam(required = true) String currentPassword, @RequestParam(required = true) String newPassword) {

        String hash, message = "Invalid. Password does not match with the current";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Users user = us.findById(initiator.getUsername()).get();

        if (user == null) {
            message = "User was not found!";
            status = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(message, status);
        }

        boolean match = encoder.matches(currentPassword, user.getPassword());

        if (match) {
            hash = encoder.encode(newPassword);
            us.updatePassword(hash, initiator.getUsername());
            message = "Password was successfully modified";
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(message, status);
    }
    @PostMapping("user/img")
    public ResponseEntity<String> modifyUserImg(@RequestParam(required = true) MultipartFile file) {
        HttpStatus status;
        String message;
        try {
            String filename = file.getOriginalFilename();
            File dest = new File(Define.ASSETS_DIRECTORY + "/" + filename);
//            File dest = new File(Define.ASSETS_USER_DIRECTORY + "/" + filename);

            file.transferTo(dest);
            us.updateImg(Define.DOWNLOAD_IMAGE_URL + filename, initiator.getUsername());

            message = Define.DOWNLOAD_IMAGE_URL + filename;
            status = HttpStatus.CREATED;
        }
        catch (IOException | IllegalStateException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = e.getMessage();
        }
        return new ResponseEntity<>(message, status);
    }
    @GetMapping("user/data")
    public ResponseEntity<Users> getUserData(@RequestParam(required = true) String username) {

        Users user = us.findById(username).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // TODO: got to check for username modification cause need change cookie from server according the new username, does not work for the moment
    @PatchMapping("user/username")
    public ResponseEntity<String> patchUsername(@RequestParam(required = true, name = "data") String username, HttpServletResponse response) {
        us.updateUsername(username, initiator.getUsername());
        return new ResponseEntity<>("Username successfully modified", HttpStatus.OK);
    }
    @DeleteMapping("user/delete")
    public void deleteAccount(HttpServletResponse response) throws IOException {
        jwt.setExpiration(0);
        String token = jwt.encode();
        jwt.setExpiration(0);
        String refreshToken = jwt.encode();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString("");

        us.deleteById(initiator.getUsername());

        response.addHeader("x-auth-token", token);
        response.addHeader("x-refresh-token", refreshToken);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().write(json);
        response.flushBuffer();
    }
}
