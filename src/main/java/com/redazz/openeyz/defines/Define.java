/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.defines;

/**
 *
 * @author kyzer
 */
public class Define {
    public static final String SERVER_ROOT_URL = "/";
    public static final String SERVER_BASE_URL = "/api";
    public static final String SERVER_ACCESS_URL = "/api/access";
    public static final String SERVER_AUTH_FAILURE_URL = "/api/auth-failure";
    public static final String SERVER_UPLOAD_IMAGE_URL = "/api/image";
    public static final String SERVER_DOWNLOAD_IMAGE_URL = "http://localhost:8081/api/image?img=";
    public static final String SERVER_ROOT_ASSETS_DIRECTORY = "assets";
    
    public static final String ALLOWED_ORIGIN_URL = "http://localhost:8080";
    public static final String LOGIN_PAGE_URL = "http://localhost:8080/#/access";
    
    public static final String AUTH_USER_QUERY = "select username, password, state from users where username = ?";
    public static final String AUTH_AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";
    public static final String AUTH_ROLE_PREFIX = "ROLE_";

    public static final String COOKIE_SESSIONID_NAME = "JSESSIONID";
    public static final String COOKIE_USERID_NAME = "USERID";

    public static final int MULTIPART_UPLOAD_SIZE = 1000000;
    
    public static final String PAGE_ACCESS_URL = "http://localhost:8080/app/access";
    
    public static final String MESSAGE_ERROR_USERNAME = "bad credentials user was not found";
    public static final String MESSAGE_ERROR_PASSWORD = "bad credentials check password";
    public static final String MESSAGE_ERROR_POST = "An error has occurred when try to post publication";

    public static final String MESSAGE_AUTH_SUCCESS = "Auth success";
    public static final String MESSAGE_POST_SUCCESS = "Publication successfully posted";
    
    
}
