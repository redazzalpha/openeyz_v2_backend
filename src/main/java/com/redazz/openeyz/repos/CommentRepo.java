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
//    @Query(value="select name, avatar_src, role, content, to_char(creation, 'DD/MM/YYYY at HH24:MI'), comment.author_id, comment.id from comment  left join user_roles on user_roles.username = author_id  left join users on users.username = author_id where post_id = :postId order by comment.creation desc", nativeQuery = true)
    @Query("select c, to_char(c.creation, 'DD/MM/YYYY at HH24:MI') from Comment c where c.post.id = :postId order by c.creation desc")
    public List<Tuple> getAllFromPost(long postId);
    
    @Query("select count(c) from Comment c where c.post.id = :postId")
    public int getcount(long postId);
}
