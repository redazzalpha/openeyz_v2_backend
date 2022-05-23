/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Likes;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface LikesRepo extends JpaRepository<Likes, Long> {
    @Query("select count(l) from Likes l where l.post.id = :postId")
    public int getcountFromPost(long postId);
    
    @Query("select l.id, l.author.username from Likes l where l.post.id = :postId")
    public List<Tuple> getAuthorFromPost(long postId);
    
}
