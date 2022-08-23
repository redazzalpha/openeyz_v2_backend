/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redazz.openeyz.classes.PublicationComponent;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public class Likes extends PublicationComponent implements Serializable {
    
    // construcors
    public Likes(Post post, Users author) {
        this.post = post;
        this.author = author;
    }
    
    //relationships
    @NonNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
