package com.upiiz.dm_degr_07.utils;

import android.util.Patterns;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Utilidades de validación de campos y hash de contraseñas. */
public final class Validaciones {

    public static final int MIN_PASSWORD = 6;
    public static final int EDAD_MAX = 50;

    private Validaciones() { }

    public static boolean emailValido(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /** Hash SHA-256 de la contraseña: en la BD nunca se guarda en texto plano. */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    /** Pone en mayúscula la primera letra: "firulais" -> "Firulais". */
    public static String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
