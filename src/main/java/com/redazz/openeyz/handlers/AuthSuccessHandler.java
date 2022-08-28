/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.handlers;

import com.redazz.openeyz.defines.Define;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = request.getParameter("username");

        ResponseCookie cookie = ResponseCookie.from(Define.COOKIE_USERID_NAME, username)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(Define.ROOT_URL);
    }
}
