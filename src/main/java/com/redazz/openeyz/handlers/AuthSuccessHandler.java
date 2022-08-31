/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserService us;
    @Autowired
    JwTokenUtils jwt;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
        Optional<Users> user = us.findById(username);

        if (user.isPresent()) {
            jwt.setExpiration(1);
            String token = jwt.encode(username);
            jwt.setExpiration(7);
            String refreshToken = jwt.encode(username);

            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(user.get());

            response.addHeader("x-auth-token", token);
            response.addHeader("x-refresh-token", refreshToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            response.getWriter().write(userJson);
            response.flushBuffer(); // flush buffer commit end send to client the response
        }
    }
}
