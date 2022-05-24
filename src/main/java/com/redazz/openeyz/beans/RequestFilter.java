/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.defines.Define;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class RequestFilter implements Filter {
    @Autowired JwTokenUtils jwt;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz " + req.getMethod());
        if(!req.getRequestURI().equals(Define.SERVER_BASE_URL)) {
            String token = req.getHeader("Authorization").split("Bearer ")[1];
            
            
            try {
                jwt.decode(token);
            }
            catch(Exception e) {
                res.sendError(401, e.getMessage());
            }    
        }
        chain.doFilter(request, response);
    }
}
