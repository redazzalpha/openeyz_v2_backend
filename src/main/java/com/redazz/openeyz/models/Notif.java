/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author kyzer
 */
@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = "id", allowGetters = true)
public class Notif implements Serializable {
    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    //relationships
    @NonNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "username")
    private Users owner;
    
    @NonNull
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    private Comment comment;

    
}
