/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.filters;

import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kyzer
 */
public class RequestFilter implements Filter {
    private final JwTokenUtils jwt = new JwTokenUtils();
    
    UserService us;
    Initiator initiator;
    
    public RequestFilter(UserService us, Initiator initiator) {
        this.us = us;
        this.initiator = initiator;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        boolean isAccessDownloadImg = req.getRequestURI().split("\\?")[0].equals(Define.UPLOAD_IMAGE_URL) && req.getMethod().equals("GET");
        boolean isAccessRefresh = req.getRequestURI().equals(Define.REFRESH_URL);
        boolean isCheckToken = !(isAccessDownloadImg || isAccessRefresh);
        boolean isSupervisorRoute = req.getRequestURI().equals(Define.ADMIN_URL);

        if (isCheckToken) {
            if (req.getHeader("Authorization") != null) {
                try {
                    String token = req.getHeader("Authorization").split("Bearer ")[1];
                    Jws<Claims> jws = jwt.decode(token);
                    String usernameToken = jws.getBody().get("username").toString();
                    Optional<Users> user = us.findById(usernameToken);
                    if (user.isPresent()) {
                        Users currentUser = user.get();
                        initiator.init(currentUser);
                        String roleToken = jws.getBody().get("role").toString();
                        boolean isAuthorized = roleToken.equals("SUPERADMIN") || roleToken.equals("ADMIN");
                        if (isSupervisorRoute && !isAuthorized) {
                            res.sendError(403, "user is not authorized to access");
                        }
                    }
                    else {
                        res.sendError(400, "user was not found");
                    }
                }
                catch (ExpiredJwtException | MalformedJwtException | IOException ex) {
                    res.sendError(401, ex.getMessage());
                }
            }
            else {
                res.sendError(401, "bearer token was not found");
            }
        }
        chain.doFilter(request, response);
    }
}
