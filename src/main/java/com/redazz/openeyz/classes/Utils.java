/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.classes;

import com.redazz.openeyz.Exceptions.DataNotFoundException;
import com.redazz.openeyz.Exceptions.ForbiddenException;
import com.redazz.openeyz.Exceptions.NoUserFoundException;
import com.redazz.openeyz.beans.Initiator;
import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import com.redazz.openeyz.enums.RoleEnum;
import com.redazz.openeyz.models.Users;
import com.redazz.openeyz.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author kyzer
 */
public class Utils {
    public static boolean isFieldValid(String arg) {

        Pattern pattern = Pattern.compile("^\\s*[0-9a-zA-ZÀ-ÿ']{2,}([\\s|-]?[0-9a-zA-ZÀ-ÿ']{1,})*\\s*$");
        Matcher matcher = pattern.matcher(arg);
        return matcher.find();
    }
    public static boolean isPasswdValid(String arg) {

        Pattern digit = Pattern.compile(".*\\d.*");
        Pattern lowercase = Pattern.compile(".*[a-z].*");
        Pattern uppercase = Pattern.compile(".*[A-Z].*");
        Pattern specialChar = Pattern.compile(".*[*.!@#$%^&(){}\\[\\]:\";'<>,.?/~`_+\\-=|\\\\].*");
        Pattern space = Pattern.compile(".*\\s.*");

        boolean isDigit = digit.matcher(arg).find();
        boolean isLowercase = lowercase.matcher(arg).find();
        boolean isUppercase = uppercase.matcher(arg).find();
        boolean isSpecialChar = specialChar.matcher(arg).find();
        boolean isSpace = space.matcher(arg).find();
        boolean isMin8 = arg.length() >= 8;

        return isDigit
                && isLowercase
                && isUppercase
                && isSpecialChar
                && !isSpace
                && isMin8;
    }

    public static void checkToken(ServletRequest request, Initiator initiator, UserService us, JwTokenUtils jwt, String secret)
            throws DataNotFoundException, NoUserFoundException, ForbiddenException {

        HttpServletRequest req = (HttpServletRequest) request;

        Pattern pattern = Pattern.compile(Define.SOCKET_END_POINT_URL);
        Matcher matcher = pattern.matcher(req.getRequestURI());
        boolean isWebSocketMessage = matcher.find();

        boolean isAccessDownloadImg = req.getRequestURI().split("\\?")[0].equals(Define.LOCAL_IMAGE_URL) && req.getMethod().equals("GET");
        boolean isAccessDownloadAvatar = req.getRequestURI().split("\\?")[0].equals(Define.LOCAL_AVATAR_URL) && req.getMethod().equals("GET");
        boolean isAccessRefresh = req.getRequestURI().equals(Define.REFRESH_URL);
        boolean isAccessLogout = req.getRequestURI().equals(Define.LOGOUT_URL);

        boolean isCheckToken = !(isAccessDownloadImg || isAccessDownloadAvatar || isAccessRefresh || isAccessLogout || isWebSocketMessage);
        boolean isSupervisorRoute = req.getRequestURI().equals(Define.ADMIN_URL);

        if (isCheckToken) {
            if (req.getHeader("Authorization") == null) {
                throw new DataNotFoundException("authorization header is not present");
            }

            String token = req.getHeader("Authorization").split("Bearer ")[1];

            if (token == null) {
                throw new DataNotFoundException("bearer token is not present");
            }

            Jws<Claims> jws = jwt.decode(token, secret);
            String usernameToken = jws.getBody().get("username").toString();
            Optional<Users> optUser = us.findById(usernameToken);

            if (optUser.isEmpty()) {
                throw new NoUserFoundException("user value is not present");
            }

            Users currentUser = optUser.get();
            initiator.init(currentUser);
            String roleToken = jws.getBody().get("role").toString();
            boolean isSupervisor = roleToken.equals(RoleEnum.SUPERADMIN.toString()) || roleToken.equals(RoleEnum.ADMIN.toString());
            boolean isBanned = !initiator.getState();

            if (isBanned) {
                throw new ForbiddenException("your account has been disabled");
            }

            if (isSupervisorRoute && !isSupervisor) {
                throw new ForbiddenException("user is not authorized to access");
            }
        }

    }
}
