/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Comment;
import com.redazz.openeyz.repos.CommentRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class CommentService implements Services<Comment, Long>{
    @Autowired CommentRepo cr;
    
    // crud services
    @Override
    public Comment save(Comment entity) {
        return cr.save(entity);
    }
    @Override
    public List<Comment> findAll() {
        return cr.findAll();
    }
    @Override
    public Optional<Comment> findById(Long id) {
        return cr.findById(id);
    }
    @Override
    public void deleteAll() {
        cr.deleteAll();
    }
    @Override
    public void deleteById(Long id) {
        cr.deleteById(id);
    }
    @Override
    public void delete(Comment entity) {
        cr.delete(entity);
    }
    
    // custom services
    public List<Comment> getAllFromPost(long postId) {
        return cr.getAllFromPost(postId);
    }
    
//    public int getcount(long postId) {
//        return cr.getcount(postId);
//    }


}
