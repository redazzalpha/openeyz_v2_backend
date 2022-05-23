/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Likes;
import com.redazz.openeyz.repos.LikesRepo;
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
public class LikesService implements Services<Likes, Long> {
    @Autowired LikesRepo lr;

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
    
    public int getcount(long postId) {
        return lr.getcountFromPost(postId);
    }
    
    public List<Tuple> getAuthorFromPost(long postId) {
        return lr.getAuthorFromPost(postId);
    }

    
}
