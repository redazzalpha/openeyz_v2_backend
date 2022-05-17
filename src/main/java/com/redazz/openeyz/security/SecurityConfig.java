/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.security;

import com.redazz.openeyz.beans.Encoder;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 *
 * @author kyzer
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired UserService us;
    @Autowired DataSource dataSource;
    @Autowired Encoder encoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin()
        .loginProcessingUrl(Define.SERVER_ACCESS_URL)
        .defaultSuccessUrl(Define.SERVER_BASE_URL)
        .and()
        .cors().configurationSource((request) -> {
            CorsConfiguration cors = new CorsConfiguration();
            cors.setAllowCredentials(true);
            cors.setAllowedHeaders(List.of("*"));
            cors.setAllowedOrigins(List.of("http://localhost:8080", "http://192.168.0.20:8080"));
            cors.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            return cors;
        });
        http.authorizeHttpRequests().antMatchers("/login").permitAll()
        .anyRequest().authenticated()
        .and();
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
    
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(Define.MULTIPART_UPLOAD_SIZE);
        return multipartResolver;
    }

    class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
        public AuthenticationFilter() throws Exception {
            setUsernameParameter("username");
            setPasswordParameter("password");
            setAuthenticationManager(authenticationManager());
            setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    response.sendRedirect(Define.SERVER_BASE_URL + "?username=" + request.getParameter("username"));
                }
            });
            setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                    System.out.println("Login error: " + exception.getMessage());
                    super.setDefaultFailureUrl("/login?error");
                    super.onAuthenticationFailure(request, response, exception);
                }
            });
            super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(Define.SERVER_ACCESS_URL, "POST"));
        }
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

            String username, lname, name, password;
            username = request.getParameter("username");
            password = request.getParameter("password");
            lname = request.getParameter("lname");
            name = request.getParameter("name");

            try {
                us.save(new Users(username, lname, name, password));
                us.addRoleToUser(username, RoleEnum.USER);
            }
            catch (Exception e) {
            }
            return super.attemptAuthentication(request, response);
        }
    }
}
