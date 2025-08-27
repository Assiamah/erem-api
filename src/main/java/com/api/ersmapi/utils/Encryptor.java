package com.api.ersmapi.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import io.github.cdimascio.dotenv.Dotenv;

public class Encryptor {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String masterPassword = dotenv.get("JASYPT_ENCRYPTOR_PASSWORD");
        // Your API key or value to encrypt
        String plainText = dotenv.get("JASYPT_ENCRYPTOR_KEY"); 

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(masterPassword);
        encryptor.setAlgorithm(dotenv.get("JASYPT_ENCRYPTOR_ALGORITHM"));

        String encrypted = encryptor.encrypt(plainText);
        System.out.println("Encrypted value: " + encrypted);
    }
}
