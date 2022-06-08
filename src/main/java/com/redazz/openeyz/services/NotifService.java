/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Notif;
import com.redazz.openeyz.repos.NotifRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class NotifService implements Services<Notif, Long> {
    @Autowired
    NotifRepo nr;

    // crud services
    @Override
    public Notif save(Notif entity) {
        return nr.save(entity);
    }
    @Override
    public List<Notif> findAll() {
        return nr.findAll();
    }
    @Override
    public Optional<Notif> findById(Long id) {
        return nr.findById(id);
    }
    @Override
    public void deleteAll() {
        nr.deleteAll();
    }
    @Override
    public void deleteById(Long id) {
        nr.deleteById(id);
    }
    @Override
    public void delete(Notif entity) {
        nr.delete(entity);
    }

    // custom service
    public List<Notif> getNotifsFromOwner(String owner) {
        return nr.getNotifsFromOwner(owner);
    }

}
