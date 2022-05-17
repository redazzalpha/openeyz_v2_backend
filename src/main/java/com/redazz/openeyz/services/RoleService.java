/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Role;
import com.redazz.openeyz.repos.RoleRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class RoleService implements Services<Role, RoleEnum> {
    @Autowired RoleRepo rr;
    
    // crud services
    @Override
    public Role save(Role entity) {
        return rr.save(entity);
    }
    @Override
    public List<Role> findAll() {
        return rr.findAll();
    }
    @Override
    public Optional<Role> findById(RoleEnum roleName) {
        return rr.findById(roleName);
    }
    @Override
    public void deleteAll() {
        rr.deleteAll();
    }
    @Override
    public void deleteById(RoleEnum roleName) {
        rr.deleteById(roleName);
    }
    @Override
    public void delete(Role entity) {
        rr.delete(entity);
    }

}
