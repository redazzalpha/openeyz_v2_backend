/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redazz.openeyz.classes.PublicationBase;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author kyzer
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(value = {"id", "creation"}, allowGetters = true)
@EqualsAndHashCode(callSuper = false)
public class Post extends PublicationBase implements Serializable {
    // properties    
    @NonNull
    @Column(nullable = false, columnDefinition = "text")
    private String content;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date creation;
    
    //constructors
    public Post(String content, Users author) {
        this.content = content;
        this.author = author;
    }
}
