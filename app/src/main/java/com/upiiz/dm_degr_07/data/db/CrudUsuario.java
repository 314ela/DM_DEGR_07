package com.upiiz.dm_degr_07.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import com.upiiz.dm_degr_07.data.entities.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//crud de ususarios
public class CrudUsuario {

    private final AppDataBase db;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CrudUsuario(Context context) {
        db = AppDataBase.getInstance(context);
    }

    public void registrar(Usuario usuario, OnRegistroListener listener) {
        executorService.execute(() -> {
            Usuario existente = db.usuarioDao().obtenerPorEmail(usuario.getEmail());
            if (existente != null) {
                mainHandler.post(listener::onEmailDuplicado);
                return;
            }
            try {
                long id = db.usuarioDao().crear(usuario);
                usuario.setId(id);
                mainHandler.post(() -> listener.onExito(usuario));
            } catch (SQLiteConstraintException e) {
                mainHandler.post(listener::onEmailDuplicado);
            }
        });
    }

    // Busca un usuario con ese email y contraseña
    public void login(String email, String passwordHash, OnUsuarioLoadedListener listener) {
        executorService.execute(() -> {
            Usuario usuario = db.usuarioDao().login(email, passwordHash);
            mainHandler.post(() -> listener.onComplete(usuario));
        });
    }

    public void obtenerPorId(long id, OnUsuarioLoadedListener listener) {
        executorService.execute(() -> {
            Usuario usuario = db.usuarioDao().obtenerPorId(id);
            mainHandler.post(() -> listener.onComplete(usuario));
        });
    }

    public interface OnUsuarioLoadedListener {
        void onComplete(Usuario usuario);
    }

    public interface OnRegistroListener {
        void onExito(Usuario usuario);
        void onEmailDuplicado();
    }
}
