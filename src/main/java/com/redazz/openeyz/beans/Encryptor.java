/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.beans;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
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
    /**
     * FUNCTION ADD SALT TO INPUT BYTE ARRAY AND ENCRYPT
     * @param inputByteArray
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public byte[] encrypt(byte[] inputByteArray) throws UnsupportedEncodingException {
        byte[] saltByteArray = KeyGenerators.string().generateKey().getBytes("utf-8");
        byte[] resByteArray = new byte[saltByteArray.length + inputByteArray.length];
        int saltLength = saltByteArray.length;
        int inputLength = inputByteArray.length;

        System.arraycopy(saltByteArray, 0, resByteArray, 0, saltLength);
        System.arraycopy(inputByteArray, 0, resByteArray, saltLength, inputLength);

        return encryptor.encrypt(resByteArray);
    }
    /**
     * FUNCTION DECRYPT BYTE ARRAY AND REMOVE SALT
     * @param encryptByteArray
     * @return 
     */
    public byte[] decrypt(byte[] encryptByteArray) {
        byte[] decryptByteArray = encryptor.decrypt(encryptByteArray);
        return Arrays.copyOfRange(decryptByteArray, 16, decryptByteArray.length);
    }
}
