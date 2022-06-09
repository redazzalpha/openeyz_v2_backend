/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.repos;

import com.redazz.openeyz.models.Notif;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    
    @Transactional
    @Modifying
    @Query("update Notif set read = true where owner.username = :owner")
    public void readAllFromUser(String owner);
    
    @Transactional
    @Modifying
    @Query("update Notif set read = true where id = :notifId")
    public void readOneFromUser(long notifId);

    @Transactional
    @Modifying
    @Query(value = "delete from Notif where owner.username = :owner")
    public void deleteAllFromUser(String owner);
    @Transactional

    @Modifying
    @Query(value = "delete from Notif where id = :notifId")
    public void deleteOneFromUser(long notifId);
}
