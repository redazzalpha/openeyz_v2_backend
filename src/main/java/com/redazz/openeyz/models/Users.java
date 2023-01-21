/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.redazz.openeyz.classes.AbstractUsers;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author kyzer
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RequiredArgsConstructor
public class Users extends AbstractUsers implements Serializable {
    // properties
    @NonNull
    @Column(columnDefinition = "BYTEA", nullable = false)
    private byte[] lname;

    // constructor
    public Users(String username, byte[] lname, String name, String password, String description) {
        super(username, name, password, description);
        this.lname = lname;
    }
}
