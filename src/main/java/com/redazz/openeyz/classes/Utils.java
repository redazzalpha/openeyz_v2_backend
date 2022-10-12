/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
