/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.services;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author kyzer
 * @param <T>
 * @param <ID>
 */
public interface Services <T, ID> {
    // crud services
    public T save(T entity) ;
    public List<T> findAll() ;
    public Optional<T> findById(ID id) ;
    public void deleteAll() ;
    public void deleteById(ID id) ;
    public void delete(T entity) ;
}
