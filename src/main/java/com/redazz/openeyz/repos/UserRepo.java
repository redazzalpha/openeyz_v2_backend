/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Users;
import java.util.List;
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
    @Query(value = "select name, avatar_src, role, users.username from users left join user_roles on users.username = user_roles.username order by name", nativeQuery = true)
    public List<Object> getAllSimple();

    //important to make a update query must add @transactional and @modifying
    @Modifying
    @Transactional
    @Query("update Users set dark = :dark where username = :userId")
    public void updateDark(boolean dark, String userId);

    @Modifying
    @Transactional
    @Query("update Users set lname = :lname where username = :userId")
    public void updateLname(byte[] lname, String userId);

    @Modifying
    @Transactional
    @Query("update Users set name = :name where username = :userId")
    public void updateName(String name, String userId);

    @Modifying
    @Transactional
    @Query("update Users set description = :description where username = :userId")
    public void updateDescription(String description, String userId);

    @Modifying
    @Transactional
    @Query("update Users set password = :password where username = :userId")
    public void updatePassword(String password, String userId);

    @Modifying
    @Transactional
    @Query("update Users set avatarSrc = :avatarSrc where username = :userId")
    public void updateImg(String avatarSrc, String userId);

    @Modifying
    @Transactional
    @Query("update Users set state = :state where username = :userId")
    public void updateState(boolean state, String userId);
    
    @Modifying
    @Transactional
    @Query(value = "update user_roles set role = :roleName where username = :userId", nativeQuery = true)
    public void updateRole(String roleName, String userId);
    
    // experimental
    @Modifying
    @Transactional
    @Query("update Users set username = :username where username = :userId")
    public void updateUsername(String username, String userId);

}
