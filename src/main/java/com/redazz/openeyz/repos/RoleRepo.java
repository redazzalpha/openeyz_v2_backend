/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface RoleRepo extends JpaRepository<Role, RoleEnum> {
    
}
