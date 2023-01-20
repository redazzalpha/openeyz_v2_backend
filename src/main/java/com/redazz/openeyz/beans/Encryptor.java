/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import java.io.UnsupportedEncodingException;
import javax.xml.bind.DatatypeConverter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

/**
 *
 * @author kyzer
 */
@Data
@Component
public class Encryptor {
    // properties 
    private BytesEncryptor encryptor;

    // constructor
    @Autowired
    public Encryptor(@Value("${enc.secret}") String encKey, @Value("${salt.secret}") String saltKey) throws UnsupportedEncodingException {
        encryptor = Encryptors.stronger(encKey, DatatypeConverter.printHexBinary(saltKey.getBytes("UTF-8")));
    }
    
    // methods
    public byte[] encrypt(byte[] input) {
        return encryptor.encrypt(input);
    }
    public byte[] decrypt(byte[] input) {
        return encryptor.decrypt(input);
    }
}
