package com.mssecurity.mssecurity.Services;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptionService {
    // CLASE 07/09/2023 (7) Se crea originalmente en UsersControllers
    // CLASE 12/09/2023 (8) Se cambia de ubicacioón
    // Implementación de cifrado SHA-256 para la contraseña de los usuarios.
    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
