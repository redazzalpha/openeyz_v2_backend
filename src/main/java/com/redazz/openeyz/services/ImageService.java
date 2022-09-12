/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import com.redazz.openeyz.models.Image;
import com.redazz.openeyz.repos.ImageRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kyzer
 */
@Service
public class ImageService implements Services<Image, Long>{
    @Autowired ImageRepo ir;
    @Override
    public Image save(Image entity) {
        return ir.save(entity);
    }
    @Override
    public List<Image> findAll() {
        return ir.findAll();
    }
    @Override
    public Optional<Image> findById(Long id) {
        return ir.findById(id);
    }
    @Override
    public void deleteAll() {
        ir.deleteAll();
    }
    @Override
    public void deleteById(Long id) {
        ir.deleteById(id);
    }
    @Override
    public void delete(Image entity) {
        ir.delete(entity);
    }
}
