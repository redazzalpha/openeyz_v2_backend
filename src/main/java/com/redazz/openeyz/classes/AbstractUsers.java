/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.classes;

import com.redazz.openeyz.models.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
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
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractUsers {
        // properties
    @Id
    @NonNull
    protected String username;
    
    @NonNull
    @Length(min = 2, max = 20)
    @Column(nullable = false, length = 20 )
    protected String name;

    @NonNull
    @Length(min = 8)
    @Column(nullable = false)
    protected String password;

    @Column(columnDefinition = "boolean default true", nullable = false)
    protected boolean state = true;

    @NonNull
    @Column(nullable = true, columnDefinition = "varchar(255) default 'no description for the moment'")
    protected String description;

    @Column(nullable = true)
    protected String avatarSrc;

    @Column(columnDefinition = "boolean default false", nullable = false)
    protected boolean dark;

    // relationships
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "roleName")
    )
    protected List<Role> roles = new ArrayList<>();
    
    // getters
    public boolean getDark() {
        return dark;
    }
    public boolean getState() {
        return state;
    }

}
