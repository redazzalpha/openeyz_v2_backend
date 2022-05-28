    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Post;
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
public interface PostRepo extends JpaRepository<Post, Long> {
    @Query("select p, to_char(p.creation, 'DD/MM/YYYY at HH24:MI'), count(c), count(l) from Post p left join Comment c on c.post.id = p.id left join Likes l on l.post.id = p.id  group by p.id order by p.creation desc")
    public List<Tuple> getAll();    
}
