/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.filters;

import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kyzer
 */
public class RequestFilter implements Filter {
    private final JwTokenUtils jwt = new JwTokenUtils();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean isAccessAuth = req.getRequestURI().equals(Define.ROOT_URL);
        boolean isAccessDownloadImg = req.getRequestURI().split("\\?")[0].equals(Define.UPLOAD_IMAGE_URL) && req.getMethod().equals("GET");
        boolean isAccessRefresh = req.getRequestURI().equals(Define.REFRESH_URL);
        boolean isCheckToken = !(isAccessAuth || isAccessDownloadImg || isAccessRefresh);
        boolean isSupervisorRoute = req.getRequestURI().equals(Define.ADMIN_URL);
        Cookie JSESSIONID = null;

        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("JSESSIONID")) {
                JSESSIONID = cookie;
            }
        }

        if (JSESSIONID != null) {
            if (isCheckToken) {
                if (req.getHeader("Authorization") != null) {

                    String token = req.getHeader("Authorization").split("Bearer ")[1];
                    try {
                        Jws<Claims> jws = jwt.decode(token);
                        String role = jws.getBody().get("role").toString();
                        String jsessionid = jws.getBody().get("JSESSIONID").toString();
                        boolean isAuthorized = role.equals("SUPERADMIN") || role.equals("ADMIN");
                        boolean matchTokenCookie = JSESSIONID.getValue().equals(jsessionid);
                        if (!matchTokenCookie) {
                            res.sendError(403, "token mismatch with cookie");
                        }
                        if (isSupervisorRoute && !isAuthorized) {
                            res.sendError(403, "user is not authorized to access");
                        }
                    }
                    catch (Exception e) {
                        res.sendError(401, e.getMessage());
                    }
                }
                else {
                    res.sendError(401, "bearer token was not found");
                }
            }
        }
        else {
            res.sendError(401, "JSESSIONID was not found");
        }

        chain.doFilter(request, response);
    }
}
