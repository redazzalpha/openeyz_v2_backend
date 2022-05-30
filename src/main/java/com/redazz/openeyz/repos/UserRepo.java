/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Users;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface UserRepo extends JpaRepository<Users, String> {

    //important to make a update query must add @transactional and @modifying
    @Modifying
    @Transactional
    @Query("update Users set lname = :lname where username = :userId")
    public void updateLname(String lname, String userId);

    @Modifying
    @Transactional
    @Query("update Users set name = :name where username = :userId")
    public void updateName(String name, String userId);

    @Modifying
    @Transactional
    @Query("update Users set username = :username where username = :userId")
    public void updateUsername(String username, String userId);

    @Modifying
    @Transactional
    @Query("update Users set description = :description where username = :userId")
    public void updateDescription(String description, String userId);

}
