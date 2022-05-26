    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Post;
import java.util.List;
import java.util.Optional;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface PostRepo extends JpaRepository<Post, Long> {
//    @Query(value = "select id, name, avatar_src, role, content, to_char(creation, 'DD/MM/YYYY at HH24:MI'), author_id from post left join user_roles on author_id = user_roles.username left join users on author_id = users.username order by creation desc", nativeQuery = true)
    @Query("select p, to_char(p.creation, 'DD/MM/YYYY at HH24:MI') from Post p order by p.creation desc")
    public List<Tuple> getAll();
    
    @Query(value = "select name, avatar_src, role, content, to_char(creation, 'DD/MM/YYYY at HH24:MI') from post left join users on users.username = author_id left join user_roles on user_roles.username = author_id where post.id = :postId", nativeQuery = true)
    public Optional<Tuple> getPost(long postId);
    
}
