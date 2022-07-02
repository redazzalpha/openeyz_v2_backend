/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.repos.PostRepo;
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
public class PostService implements Services<Post, Long> {
    @Autowired
    PostRepo pr;

    @PersistenceContext
    private EntityManager entityManager;

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

    // custom services
    public List<Tuple> getAll() {
        return pr.getAll();
    }
    public List<Tuple> getAllLimit(int limit, String creation) {

        if (creation == null) {
            return entityManager.createQuery("select p, count(c), count(distinct l) from Post p left join Comment c on c.post.id = p.id left join Likes l on l.post.id = p.id where p.creation < current_timestamp group by p.id order by p.creation desc",
                    Tuple.class).setMaxResults(limit).getResultList();
        }
        else {

            return entityManager.createQuery("select p, count(c), count(distinct l) from Post p left join Comment c on c.post.id = p.id left join Likes l on l.post.id = p.id where p.creation < '" + creation + "' group by p.id order by p.creation desc",
                    Tuple.class).setMaxResults(limit).getResultList();
        }
    }
    public List<Tuple> getAllFromUser(String username) {
        return pr.getAllFromUser(username);
    }
    public List<Tuple> getAllFromUserLimit(String username, int limit, String creation) {
        if (creation == null) {
            return entityManager.createQuery("select p, count(c), count(distinct l) from Post p left join Comment c on c.post.id = p.id left join Likes l on l.post.id = p.id where p.author.username = '" + username + "' and p.creation < current_timestamp group by p.id order by p.creation desc",
                    Tuple.class).setMaxResults(limit).getResultList();
        }
        else {

            return entityManager.createQuery("select p, count(c), count(distinct l) from Post p left join Comment c on c.post.id = p.id left join Likes l on l.post.id = p.id where p.author.username = '" + username + "' and p.creation < '" + creation + "' group by p.id order by p.creation desc",
                    Tuple.class).setMaxResults(limit).getResultList();
        }
    }
}
