/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
<<<<<<< HEAD
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
=======
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> slave
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
    private final Initiator initiator = Initiator.get();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");
<<<<<<< HEAD

        ResponseCookie cookie = ResponseCookie.from(Define.COOKIE_USERID_NAME, username)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(Define.ROOT_URL);
=======
        Optional<Users> user = us.findById(username);

        if (user.isPresent()) {
            jwt.setExpiration(4);
            String token = jwt.encode(username);
            jwt.setExpiration(3600);
            String refreshToken = jwt.encode(username);

            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(user.get());

            Users currentUser = user.get();

            initiator.setUsername(currentUser.getUsername());
            initiator.setLname(currentUser.getLname());
            initiator.setName(currentUser.getName());
            initiator.setPassword(currentUser.getPassword());
            initiator.setState(currentUser.getState());
            initiator.setDescription(currentUser.getDescription());
            initiator.setAvatarSrc(currentUser.getAvatarSrc());
            initiator.setDark(currentUser.getDark());
            initiator.setRoles(currentUser.getRoles());

            response.addHeader("x-auth-token", token);
            response.addHeader("x-refresh-token", refreshToken);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            response.getWriter().write(userJson);
            response.flushBuffer(); // flush buffer commit end send to client the response
        }
>>>>>>> slave
    }
}
