/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.security;

import com.redazz.openeyz.handlers.AuthFailureHandler;
import com.redazz.openeyz.handlers.AuthSuccessHandler;
import com.redazz.openeyz.beans.Encoder;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author kyzer
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService us;
    @Autowired
    DataSource dataSource;
    @Autowired
    Encoder encoder;
    @Autowired
    AuthSuccessHandler authSuccessHandler;
    @Autowired
    AuthFailureHandler authFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin()
                .loginPage(Define.LOGIN_PAGE_URL)
                .and()
                .logout()
                .and()
                .addFilter(new AuthHandler())
                .cors().configurationSource(request -> corsConfiguration(request));
        http.authorizeHttpRequests()
                .antMatchers(Define.AUTH_FAILURE_URL).permitAll()
                .antMatchers("*", "/api/**").hasAnyRole("SUPERADMIN", "ADMIN", "USER")
                .antMatchers("*", "/admin/**").hasRole("SUPERADMIN");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(Define.AUTH_USER_QUERY)
                .authoritiesByUsernameQuery(Define.AUTH_AUTHORITIES_QUERY)
                .rolePrefix(Define.AUTH_ROLE_PREFIX)
                .passwordEncoder(encoder);
    }
    public CorsConfiguration corsConfiguration(HttpServletRequest request) {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowedOrigins(List.of(Define.ALLOWED_ORIGIN_URL));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        return cors;
    }

    class AuthHandler extends UsernamePasswordAuthenticationFilter {
        public AuthHandler() throws Exception {
            setUsernameParameter("username");
            setPasswordParameter("password");
            setAuthenticationManager(authenticationManager());
            setAuthenticationSuccessHandler(authSuccessHandler);
            setAuthenticationFailureHandler(authFailureHandler);
            super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(Define.ACCESS_URL, "POST"));
        }
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
            String username, lname, name, password, description;
            username = request.getParameter("username");
            password = request.getParameter("password");
            lname = request.getParameter("lname");
            name = request.getParameter("name");
            description = request.getParameter("description");

            try {
                us.save(new Users(username, lname, name, password, description));
                us.addRoleToUser(username, RoleEnum.USER);
            }
            catch (Exception e) {
            }
            return super.attemptAuthentication(request, response);
        }
    }

}
