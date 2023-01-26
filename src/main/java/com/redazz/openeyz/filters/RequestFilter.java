/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.filters;

import com.redazz.openeyz.Exceptions.NoUserFoundException;
import com.redazz.openeyz.Exceptions.DataNotFoundException;
import com.redazz.openeyz.Exceptions.ForbiddenException;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.classes.Utils;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import javax.naming.SizeLimitExceededException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kyzer
 */
public class RequestFilter implements Filter {
    private final JwTokenUtils jwt = new JwTokenUtils();

    private final UserService us;
    private final Initiator initiator;
    private final String secret;

    public RequestFilter(UserService us, Initiator initiator, String secret) {
        this.us = us;
        this.initiator = initiator;
        this.secret = secret;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            Utils.checkToken(request, initiator, us, jwt, secret);
            chain.doFilter(request, response);
        }
        catch (DataNotFoundException | ForbiddenException | NoUserFoundException | ExpiredJwtException | MalformedJwtException | IOException ex) {
            String exceptionClassName = ex.getClass().getSimpleName();
            switch (exceptionClassName) {

                case "DataNotFoundException" ->
                    res.sendError(400, ex.getMessage());
                case "ExpiredJwtException" ->
                    res.sendError(401, ex.getMessage());
                case "NoUserFoundException" ->
                    res.sendError(401, ex.getMessage());
                case "MalformedJwtException" ->
                    res.sendError(401, ex.getMessage());
                case "IOException" ->
                    res.sendError(401, ex.getMessage());
                case "ForbiddenException" ->
                    res.sendError(403, ex.getMessage());
                case "SizeLimitExceededException" ->
                    res.sendError(500, "content exceeds max size 20mb");
                default ->
                    res.sendError(500, ex.getMessage());
            }
        }
    }
}
