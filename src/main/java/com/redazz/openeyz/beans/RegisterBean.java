/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import com.redazz.openeyz.defines.Define;
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
        registrationBean.addUrlPatterns("*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(Define.MULTIPART_UPLOAD_SIZE);
        return cmr;
    }
}
