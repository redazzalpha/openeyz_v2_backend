/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.repos.PostRepo;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class PostService implements Services<Post, Long> {
    @Autowired PostRepo pr;
    
    // crud services
    @Override
    public Post save(Post entity) {
        return pr.save(entity);
    }
    @Override
    public List<Post> findAll() {
        return pr.findAll();
    }
    @Override
    public Optional<Post> findById(Long id) {
        return pr.findById(id);
    }
    @Override
    public void deleteAll() {
        pr.deleteAll();
    }
    @Override
    public void deleteById(Long id) {
        pr.deleteById(id);
    }
    @Override
    public void delete(Post entity) {
        pr.delete(entity);
    }
    
    
    
    
    
    
    
    
    
    
    public List<Tuple> getAllPost() {
        return pr.getAllPost();
    }
    public Optional<Tuple> getPost(long postId) {
        return pr.getPost(postId);
    }
}
