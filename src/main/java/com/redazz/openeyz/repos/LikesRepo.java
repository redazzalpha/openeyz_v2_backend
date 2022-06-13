/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Likes;
import com.redazz.openeyz.models.Post;
import com.redazz.openeyz.models.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface LikesRepo extends JpaRepository<Likes, Long> {
    @Query("select count(distinct l) from Post p left join Likes l on l.post.id = :postId where l.author.id = :userId")
    public int getUserlikePost(long postId, String userId);
    
    @Query("select count(l) from Likes l where l.post.id = :postId")
    public int getCount(long postId);
    
    public Optional<Likes> findByAuthorAndPost(Users author, Post post);
    
    
}
