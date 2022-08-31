/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.filters;

import com.redazz.openeyz.Exceptions.DataNotFoundException;
import com.redazz.openeyz.Exceptions.UnauthorizedException;
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

        boolean isAccessDownloadImg = req.getRequestURI().split("\\?")[0].equals(Define.LOCAL_IMAGE_URL) && req.getMethod().equals("GET");
        boolean isAccessRefresh = req.getRequestURI().equals(Define.REFRESH_URL);
        boolean isCheckToken = !(isAccessDownloadImg || isAccessRefresh);
        boolean isSupervisorRoute = req.getRequestURI().equals(Define.ADMIN_URL);

        try {
            if (isCheckToken) {
                if (req.getHeader("Authorization") == null) 
                    throw new DataNotFoundException("authorization header is not present");

                String token = req.getHeader("Authorization").split("Bearer ")[1];

                if (token == null) 
                    throw new DataNotFoundException("bearer token is not present");

                Jws<Claims> jws = jwt.decode(token);
                String usernameToken = jws.getBody().get("username").toString();
                Optional<Users> user = us.findById(usernameToken);

                if (user.isEmpty())
                    throw new DataNotFoundException("user value is not present");

                Users currentUser = user.get();
                initiator.init(currentUser);
                String roleToken = jws.getBody().get("role").toString();
                boolean isSupervisor = roleToken.equals("SUPERADMIN") || roleToken.equals("ADMIN");

                if (isSupervisorRoute && !isSupervisor) 
                    throw new UnauthorizedException("user is not authorized to access");
            }
            chain.doFilter(request, response);
        }
        catch (DataNotFoundException | UnauthorizedException | ExpiredJwtException | MalformedJwtException | IOException ex) {
            String exceptionClassName = ex.getClass().getSimpleName(); 
            switch(exceptionClassName) {
                case "DataNotFoundException" -> res.sendError(400, ex.getMessage());
                case "UnauthorizedException" -> res.sendError(403, ex.getMessage());
                case "ExpiredJwtException" -> res.sendError(401, ex.getMessage());
                case "MalformedJwtException" -> res.sendError(401, ex.getMessage());
                case "IOException" -> res.sendError(401, ex.getMessage());
                default -> res.sendError(500, ex.getMessage());
            }
        }
    }
}
