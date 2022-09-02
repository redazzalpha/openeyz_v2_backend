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
    @Autowired
    Initiator initiator;

    @PostMapping("auth-failure")
    public ResponseEntity<String> authFailure(HttpServletRequest request, HttpServletResponse response) {

        String username = request.getParameter("username");
        String message;
        HttpStatus status;
        Optional<Users> user = us.findById(username);

        if (user.isPresent()) {
            Users currentUser = user.get();
            if (!currentUser.getState()) {
                message = Define.MESSAGE_ERROR_BANNED;
                status = HttpStatus.UNAUTHORIZED;
            }
            else {
                message = Define.MESSAGE_ERROR_PASSWORD;
                status = HttpStatus.UNAUTHORIZED;
            }
        }
        else {
            message = Define.MESSAGE_NOT_FOUND_USER;
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(message, status);
    }
    //TODO : try to see if other error can happen with yarc on bad password or empty fields
    @PostMapping("register-failure")
    public ResponseEntity<String> registerFailure(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>("user already exists", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("refresh")
    public ResponseEntity<Users> refreshToken(@RequestParam(required = true) String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (request.getHeader("Authorization") != null) {
                String token = request.getHeader("Authorization").split("Bearer ")[1];
                Jws<Claims> jws;
                try {
                    jws = jwt.decode(token);
                    String usernameToken = jws.getBody().get("username").toString();
                    Optional<Users> user = us.findById(usernameToken);

                    if (user.isPresent()) {
                        response.addHeader("x-auth-token", token);
                        response.addHeader("x-refresh-token", refreshToken);
                        return new ResponseEntity<>(user.get(), HttpStatus.OK);
                    }
                }
                catch (ExpiredJwtException e) {
                    try {
                        jws = jwt.decode(refreshToken);
                        String usernameRfrshToken = jws.getBody().get("username").toString();
                        Optional<Users> user = us.findById(usernameRfrshToken);

                        if (user.isPresent()) {
                            String username = user.get().getUsername();
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
            }
        }
        catch (IOException ex) {
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @GetMapping("publication")
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
            Optional<Users> user = us.findById(username);
            if (user.isPresent()) {
                ps.save(new Post(post, user.get()));
                message = Define.MESSAGE_POST_SUCCESS;
                status = HttpStatus.CREATED;
            }
            else {
                message = Define.MESSAGE_NOT_FOUND_USER;
                status = HttpStatus.BAD_REQUEST;
            }
        }
        catch (Exception e) {
            message = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(message, status);
    }
    @DeleteMapping("publication")
    public ResponseEntity<String> deletePublication(@RequestParam(required = true) long postId) {
        try {
            Optional<Post> post = ps.findById(postId);
            if (post.isPresent()) {
                return actionHandler.run(initiator.getUsername(), post.get(), (idPost) -> {
                    ps.deleteById(idPost);
                    return null;
                });
            }
            else {
                throw new RuntimeException("post value is not present");
            }
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
        Optional<Post> optPost = ps.findById(postId);
        Optional<Users> optAuthor = us.findById(initiator.getUsername());
        String message = "Comment successfully created";
        HttpStatus status = HttpStatus.CREATED;

        if (optPost.isPresent() && optAuthor.isPresent()) {

            Post post = optPost.get();
            Users author = optAuthor.get();

            Comment com = new Comment(comment, post, author);
            cs.save(com);

            Users owner = com.getPost().getAuthor();
            if (!author.getUsername().equals(owner.getUsername())) {
                Notif notif = new Notif(false, owner, com, author);
                ns.save(notif);
            }
        }
        else {
            message = "sources are unavailable";
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(message, status);
    }
    @DeleteMapping("comment/delete")
    public ResponseEntity<String> deleteComment(@RequestParam(required = true) long commentId) {
        try {
            Optional<Comment> comment = cs.findById(commentId);
            if (comment.isPresent()) {
                return actionHandler.run(initiator.getUsername(), comment.get(), (idComment) -> {
                    cs.deleteById(idComment);
                    return null;
                });
            }
            else {
                throw new RuntimeException("comment value is not present");
            }
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
            Optional<Notif> notif = ns.findById(notifId);
            if (notif.isPresent()) {
                return actionHandler.run(initiator.getUsername(), notif.get(), (idNotif) -> {
                    ns.readOneFromUser(idNotif);
                    return null;
                });
            }
            else {
                throw new RuntimeException("notif value is not present");
            }
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("notif/one")
    public ResponseEntity<String> deleteNotifOne(@RequestParam(required = true) long notifId) {
        try {
            Optional<Notif> notif = ns.findById(notifId);
            if (notif.isPresent()) {
                return actionHandler.run(initiator.getUsername(), notif.get(), (idNotif) -> {
                    ns.deleteById(idNotif);
                    return null;
                });
            }
            else {
                throw new RuntimeException("notif value is not present");
            }
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @PostMapping("like")
    public ResponseEntity<String> postLike(@RequestParam(required = true) long postId) {
        Optional<Users> author = us.findById(initiator.getUsername());
        Optional<Post> post = ps.findById(postId);
        String message;
        HttpStatus status;

        if (author.isPresent() && post.isPresent()) {

            if (!ls.getUserlikePost(postId, author.get().getUsername())) {

                Likes like = new Likes(post.get(), author.get());
                ls.save(like);
                message = "Like successfully added";
                status = HttpStatus.CREATED;
            }
            else {
                Likes like = ls.findByAuthorAndPost(author.get(), post.get()).get();
                ls.delete(like);
                message = "Like successfully removed";
                status = HttpStatus.OK;
            }
        }
        else {
            message = "resource values are not present";
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(message, status);
    }
    @GetMapping("like/count")
    public ResponseEntity<Integer> getCount(@RequestParam(required = true) long postId) {
        return new ResponseEntity<>(ls.getCount(postId), HttpStatus.OK);
    }

    // TODO: modify file name of image when on server to get inique image name because it may cause troubles
    // TODO: delete image from server when image removed from front end on cancel action
    @PostMapping("img")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam(required = true) MultipartFile file) {
        Map<String, String> json = new HashMap<>();
        HttpStatus status;
        try {
            String filename = file.getOriginalFilename();
            File dest = new File(Define.ASSETS_DIRECTORY + "/" + filename);

            file.transferTo(dest);
            //must return json object type with url field according CKEditor config
            json.put("url", Define.DOWNLOAD_IMAGE_URL + filename);
            status = HttpStatus.CREATED;
        }
        catch (IOException | IllegalStateException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(json, status);
    }
    @GetMapping("img")
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

    @GetMapping("user")
    public ResponseEntity<Users> getUser() {
        return new ResponseEntity<>(us.findById(initiator.getUsername()).get(), HttpStatus.OK);
    }
    @GetMapping("user/simple")
    public ResponseEntity<List<Object>> getAllUsuersSimple() {
        return new ResponseEntity<>(us.getAllSimple(), HttpStatus.OK);
    }
    @GetMapping("user/data")
    public ResponseEntity<Users> getUserData(@RequestParam(required = true) String username) {

        Users user = us.findById(username).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @Transactional
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
    @PatchMapping("user/username")
    public ResponseEntity<String> patchUsername(@RequestParam(required = true, name = "data") String username, HttpServletResponse response) {
        us.updateUsername(username, initiator.getUsername());
        return new ResponseEntity<>("Username successfully modified", HttpStatus.OK);
    }
    @PatchMapping("user/password")
    public ResponseEntity<String> modifyPassword(@RequestParam(required = true) String currentPassword, @RequestParam(required = true) String newPassword) {
        String hash, message = "Invalid. Password does not match with the current";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Optional<Users> user = us.findById(initiator.getUsername());

        try {
            if (user.isPresent()) {
                boolean match = encoder.matches(currentPassword, user.get().getPassword());
                if (match) {
                    hash = encoder.encode(newPassword);
                    us.updatePassword(hash, initiator.getUsername());
                    message = "Password was successfully modified";
                    status = HttpStatus.OK;
                }
            }
            else {
                message = "User was not found!";
                status = HttpStatus.NOT_FOUND;
            }
        }
        catch (Exception ex) {
            message = ex.getMessage();
        }

        return new ResponseEntity<>(message, status);
    }
    @PatchMapping("user/description")
    public ResponseEntity<String> modifyDescription(@RequestParam(required = true) String description) {
        us.updateDescription(description, initiator.getUsername());
        return new ResponseEntity<>("Description successfully modified", HttpStatus.OK);
    }
    // cannot send multipart file part using patch mapping cause doesn't work only work with post mapping
    @PostMapping("user/avatar")
    public ResponseEntity<String> modifyUserAvatar(@RequestParam(required = false) MultipartFile file) {
        HttpStatus status = HttpStatus.OK;
        String message = "user avatar has been successfully removed";

        if (file == null) {
            us.updateImg(null, initiator.getUsername());
            return new ResponseEntity<>(message, status);
        }
        try {
            String filename = file.getOriginalFilename();
            File dest = new File(Define.ASSETS_DIRECTORY + "/" + filename);

            file.transferTo(dest);
            us.updateImg(Define.DOWNLOAD_IMAGE_URL + filename, initiator.getUsername());

            message = Define.DOWNLOAD_IMAGE_URL + filename;
        }
        catch (IOException | IllegalStateException e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            message = e.getMessage();
        }
        return new ResponseEntity<>(message, status);
    }
    @PatchMapping("user/theme")
    public ResponseEntity<String> modifyTheme(@RequestParam(required = true) boolean dark) {
        us.updateDark(dark, initiator.getUsername());
        return new ResponseEntity<>("Dark theme has been successfully set", HttpStatus.OK);
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
    
    @GetMapping("logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>("logout successfull", HttpStatus.OK);
    }
    
}
