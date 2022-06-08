/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Notif;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kyzer
 */
@Repository
public interface NotifRepo extends JpaRepository<Notif, Long> {
    
    @Query("select n from Notif n where n.owner.username = :owner order by n.id desc")
    public List<Notif> getNotifsFromOwner(String owner);
}
