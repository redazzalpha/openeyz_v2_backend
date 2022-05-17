/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Users;
import javax.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface UserRepo extends JpaRepository<Users, String> {
    @Query(value = "select name, avatar_src, role, users.username, lname, description from users left join user_roles on user_roles.username = :id where users.username = :id", nativeQuery = true)
    public Tuple getCurrentUser(String id);
}
