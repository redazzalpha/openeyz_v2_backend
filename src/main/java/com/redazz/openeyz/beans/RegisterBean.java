/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.filters.ActionFilter;
import com.redazz.openeyz.filters.ActionTestFilter;
import com.redazz.openeyz.filters.RequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 *
 * @author kyzer
 */
@Configuration
public class RegisterBean {
    @Bean
    public FilterRegistrationBean<RequestFilter> requestFilter() {
        FilterRegistrationBean<RequestFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RequestFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<ActionFilter> actionFilter() {
        FilterRegistrationBean<ActionFilter> registrationBean = new FilterRegistrationBean<>();

        
        
        // publication delete 
        // comment delete
        // notif patch & delete
        // user patch 
        
        registrationBean.setFilter(new ActionFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(2);

        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<ActionTestFilter> actionTestFilter() {
        FilterRegistrationBean<ActionTestFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new ActionTestFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(3);

        return registrationBean;
    }
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(Define.MULTIPART_UPLOAD_SIZE);
        return cmr;
    }
}
