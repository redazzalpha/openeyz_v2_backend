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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.setUseForward(true);

        if (exception.getMessage().equals("user already exists")) {
            super.setDefaultFailureUrl(Define.REGISTER_FAILURE_URL);
        }
        else {
            super.setDefaultFailureUrl(Define.AUTH_FAILURE_URL);
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
