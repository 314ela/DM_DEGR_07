package com.upiiz.dm_degr_07.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda el usuario logueado en SharedPreferences.
 * Así cada pantalla sabe de quién son las mascotas que debe mostrar.
 */
public class SessionManager {

    private static final String PREFS = "upiiz_pet_session";
    private static final String KEY_ID = "usuario_id";
    private static final String KEY_NOMBRE = "usuario_nombre";
    private static final String KEY_EMAIL = "usuario_email";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void iniciarSesion(long id, String nombre, String email) {
        prefs.edit()
                .putLong(KEY_ID, id)
                .putString(KEY_NOMBRE, nombre)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public boolean haySesion() {
        return prefs.getLong(KEY_ID, -1) != -1;
    }

    public long getUsuarioId() {
        return prefs.getLong(KEY_ID, -1);
    }

    public String getNombre() {
        return prefs.getString(KEY_NOMBRE, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }
}
