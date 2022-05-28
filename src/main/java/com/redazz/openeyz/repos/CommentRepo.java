/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Comment;
import java.util.List;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */@Repository
public interface CommentRepo extends JpaRepository<Comment, Long>{
    @Query("select c, to_char(c.creation, 'DD/MM/YYYY at HH24:MI') from Comment c where c.post.id = :postId order by c.creation desc")
    public List<Tuple> getAllFromPost(long postId);
    
//    @Query("select count(c) from Comment c where c.post.id = :postId")
//    public int getcount(long postId);
}
