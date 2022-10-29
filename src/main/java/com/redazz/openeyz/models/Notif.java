/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redazz.openeyz.classes.PublicationComponent;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Notif extends PublicationComponent implements Serializable {
    // properties
    @NonNull
    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean read;

    public Notif(boolean read, Users owner, Comment comment, Users author) {
        this.read = read;
        this.owner = owner;
        this.comment = comment;
        this.author = author;
    }

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
