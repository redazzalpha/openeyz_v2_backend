/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.redazz.openeyz.enums.RoleEnum;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author kyzer
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    // properties
    @Id
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
