package com.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Metodo per criptare la password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Metodo per verificare la password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

