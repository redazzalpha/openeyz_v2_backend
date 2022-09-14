/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kyzer
 */
public interface ImageRepo extends JpaRepository<Image, Long> {
    @Query("select i from Image i where i.post.id = :postId")
    public List<Image> getImagefromPost(long postId);
    
    @Query("select i.filename from Image i left join Post p on i.post.id = p.id left join Users u on p.author.username = u.username where u.username = :username")
    public List<String> getAllImageFromUserPosts(String username);
    
}
