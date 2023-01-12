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
    
    // urls
    
    public static final String ROOT_URL = "/api";   
    public static final String ADMIN_URL = "/admin";
    
    public static final String ACCESS_URL = ROOT_URL + "/access";
    public static final String REFRESH_URL = ROOT_URL + "/refresh";
    public static final String AUTH_FAILURE_URL = ROOT_URL + "/auth-failure";
    public static final String REGISTER_FAILURE_URL = ROOT_URL + "/register-failure";
    public static final String LOCAL_IMAGE_URL = ROOT_URL + "/img";
    public static final String LOCAL_AVATAR_URL = ROOT_URL + "/avatar";
    public static final String LOGOUT_URL = ROOT_URL + "/logout";
    
    
    public static final String ASSETS_DIRECTORY = "assets";
    public static final String AVATARS_DIRECTORY = ASSETS_DIRECTORY + "/avatars";
    public static final String IMAGES_DIRECTORY = ASSETS_DIRECTORY + "/images";
    
//    public static final String SERVER_DOMAIN = "https://openeyz-v2.herokuapp.com";    
//    public static final String CLIENT_DOMAIN = "https://openeyz.netlify.app";
    
    public static final String SERVER_DOMAIN = "http://localhost:8081";
    public static final String CLIENT_DOMAIN = "http://192.168.0.20:8080";

    public static final String DOWNLOAD_IMAGE_URL = SERVER_DOMAIN + ROOT_URL + "/img?img=";
    public static final String DOWNLOAD_AVATAR_URL = SERVER_DOMAIN + ROOT_URL + "/avatar?img=";

    public static final String ALLOWED_ORIGIN_URL = CLIENT_DOMAIN;
    public static final String LOGIN_PAGE_URL = CLIENT_DOMAIN +  "/access";
    
    public static final String WS_END_POINT_URL = ROOT_URL + "/ws";
    public static final String WS_SUBSCRIBE_URL = "/signal-update";

    // query strings
    
    public static final String AUTH_USER_QUERY = "select username, password, state from users where username = ?";
    public static final String AUTH_AUTHORITIES_QUERY = "select username, role from user_roles where username = ?";
    public static final String AUTH_ROLE_PREFIX = "ROLE_";

    // messages
    
    public static final String MESSAGE_SUCCESS_AUTH = "Auth success";
    public static final String MESSAGE_SUCCESS_POST = "Publication successfully posted";

    public static final String MESSAGE_ERROR_BANNED = "your account has been disabled";
    public static final String MESSAGE_ERROR_PASSWORD = "bad credentials check password";
    public static final String MESSAGE_ERROR_POST = "An error has occurred when try to post publication";
    public static final String MESSAGE_ERROR_NOT_FOUND_USER = "user was not found";
    public static final String MESSAGE_ERROR_NOT_FOUND_POST = "post was not found";
    public static final String MESSAGE_ERROR_EMPTY_POST = "post is empty";
    public static final String MESSAGE_ERROR_EMPTY_COMMENT = "comment is empty";
    public static final String MESSAGE_ERROR_UNAVAILABLE_RESOURCES = "sources are unavailable";
    
    // others
    
    public static final int REGISTER_NUM_ARG = 4;

    public static final int MULTIPART_UPLOAD_SIZE = 10000000;
}
