/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Likes;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.repos.LikesRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class LikesService implements Services<Likes, Long> {
    @Autowired
    LikesRepo lr;

    // crud services
    @Override
    public Likes save(Likes entity) {
        return lr.save(entity);
    }
    @Override
    public List<Likes> findAll() {
        return lr.findAll();
    }
    @Override
    public Optional<Likes> findById(Long id) {
        return lr.findById(id);
    }
    @Override
    public void deleteAll() {
        lr.deleteAll();
    }
    @Override
    public void deleteById(Long id) {
        lr.deleteById(id);
    }
    @Override
    public void delete(Likes entity) {
        lr.delete(entity);
    }

    // custom services
     
    public Optional<Likes> findByAuthorAndPost(Users author, Post post) {
        return lr.findByAuthorAndPost(author, post);
    }

    
    public boolean getUserlikePost(long postId, String userId) {
        boolean like = false;
        if(lr.getUserlikePost(postId, userId) > 0)
            like = true;
        return like;
    }
}
