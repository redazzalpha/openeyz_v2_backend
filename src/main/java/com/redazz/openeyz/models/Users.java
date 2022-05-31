/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author kyzer
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Users  implements Serializable {
    // properties
    @Id
    @NonNull
    private String username;
    
    @NonNull
    @Column(nullable = false)
    private String lname;
    
    @NonNull
    @Column(nullable = false)
    private String name;
    
    @NonNull
    @Length(min = 8)
    @Column(nullable = false)
    private String password;
    
    private boolean  state = true;
    
    @Column(nullable = true, columnDefinition = "varchar(255) default 'no description for the moment'")
    private String description;
    
    @Column(nullable = true)
    private String avatarSrc;
    
    // relationships
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
        inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "roleName")
    )
    private List<Role> roles = new ArrayList<>();
}
