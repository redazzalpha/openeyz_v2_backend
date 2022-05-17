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
    public static final String SERVER_ACCESS_URL = "/api/access";
    public static final String SERVER_BASE_URL = "/api";
    public static final String SERVER_ROOT_ASSETS_URL = "assets";
    public static final String SERVER_UPLOAD_IMAGE_URL = "/api/image";
    public static final String SERVER_DOWNLOAD_IMAGE_URL = "/api/image?img=";
    
    public static final String AUTH_USER_QUERY = "select username, password, state from users where username = ?";
    public static final String AUTH_AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";
    public static final String AUTH_ROLE_PREFIX = "ROLE_";

    public static final String COOKIE_SESSIONID_NAME = "JSESSIONID";
    public static final String COOKIE_USERID_NAME = "USERID";

    public static final int MULTIPART_UPLOAD_SIZE = 1000000;
    
    public static final String PAGE_ROOT_URL = "http://localhost:8080/";
    public static final String PAGE_HOME_URL = "http://localhost:8080/app";
    public static final String PAGE_ACCESS_URL = "http://localhost:8080/app/access";
    public static final String PAGE_COMMENT_URL = "http://localhost:8080/app/comment";
}
