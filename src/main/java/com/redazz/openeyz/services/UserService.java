/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.beans.Encoder;
import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Role;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.repos.RoleRepo;
import com.redazz.openeyz.repos.UserRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class UserService implements Services<Users, String> {

    @Autowired
    UserRepo ur;
    @Autowired
    RoleRepo rr;
    @Autowired
    Encoder encoder;

    // crud services
    @Override
    public Users save(Users entity) {
        String hash = encoder.encode(entity.getPassword());
        entity.setPassword(hash);
        return ur.save(entity);
    }
    @Override
    public List<Users> findAll() {
        return ur.findAll();
    }
    @Override
    public Optional<Users> findById(String id) {
        return ur.findById(id);
    }
    @Override
    public void deleteAll() {
        ur.deleteAll();
    }
    @Override
    public void deleteById(String id) {
        ur.deleteById(id);
    }
    @Override
    public void delete(Users entity) {
        ur.delete(entity);
    }

    // custom services
    public List<Object> getAllSimple() {
        return ur.getAllSimple();
    }
    public void updateDark(boolean dark, String userId) {
        this.ur.updateDark(dark, userId);
    }
    public void updateLname(String lname, String userId) {
        ur.updateLname(lname, userId);
    }
    public void updateName(String name, String userId) {
        ur.updateName(name, userId);
    }
    public void updateDescription(String description, String userId) {
        ur.updateDescription(description, userId);
    }
    public void updatePassword(String password, String userId) {
        ur.updatePassword(password, userId);
    }
    public void updateImg(String avatarSrc, String userId) {
        ur.updateImg(avatarSrc, userId);
    }
    public void addRoleToUser(String username, RoleEnum roleName) {
        Users user = ur.findById(username).get();
        Role role = rr.findById(roleName).get();

        user.getRoles().add(role);
        ur.save(user);
    }

    public void updateUsername(String username, String userId) {
        ur.updateUsername(username, userId);
    }

}
