/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Comment;
import com.redazz.openeyz.repos.CommentRepo;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class CommentService implements Services<Comment, Long> {
    @Autowired
    CommentRepo cr;
    @PersistenceContext
    private EntityManager entityManager;

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
    public List<Comment> getAllFromPostLimit(long postId, int limit, String creation) {
        if (creation == null) {
            return entityManager.createQuery("select c from Comment c where c.post.id = ?1 and c.creation < current_timestamp order by c.creation desc",
                    Comment.class).setParameter(1, postId)
                    .setMaxResults(limit)
                    .getResultList();
//            return entityManager.createQuery("select c from Comment c where c.post.id = '" + postId + "' and c.creation < current_timestamp order by c.creation desc",
//                    Comment.class).setMaxResults(limit).getResultList();
        }
        else {

            return entityManager.createQuery("select c from Comment c where c.post.id = ?1 and c.creation < '" + creation + "' order by c.creation desc",
                    Comment.class)
                    .setParameter(1, postId)
                    .setMaxResults(limit)
                    .getResultList();
//            return entityManager.createQuery("select c from Comment c where c.post.id = '" + postId + "' and c.creation < '" + creation + "' order by c.creation desc",
//                    Comment.class).setMaxResults(limit).getResultList();
        }
    }
}
