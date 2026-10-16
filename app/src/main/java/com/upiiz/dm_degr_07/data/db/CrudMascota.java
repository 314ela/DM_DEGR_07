package com.upiiz.dm_degr_07.data.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.upiiz.dm_degr_07.data.entities.Mascota;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// CRUD
public class CrudMascota {

    private final AppDataBase db;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CrudMascota(Context context) {
        db = AppDataBase.getInstance(context);
    }

    // c
    public void agregar(Mascota mascota, OnOperacionListener listener) {
        executorService.execute(() -> {
            long id = db.mascotaDao().crear(mascota);
            mascota.setId(id);
            mainHandler.post(() -> listener.onComplete(id > 0));
        });
    }

    //R
    public void obtenerPorUsuario(long usuarioId, OnMascotasLoadListener listener) {
        executorService.execute(() -> {
            List<Mascota> mascotas = db.mascotaDao().obtenerPorUsuario(usuarioId);
            mainHandler.post(() -> listener.onComplete(mascotas));
        });
    }

    public void obtenerTodas(OnMascotasLoadListener listener) {
        executorService.execute(() -> {
            List<Mascota> mascotas = db.mascotaDao().obtenerTodos();
            mainHandler.post(() -> listener.onComplete(mascotas));
        });
    }

    public void obtenerPorId(Long id, OnMascotaLoadedListener listener) {
        executorService.execute(() -> {
            // Si no la encuentra, Room regresa null
            Mascota mascota = db.mascotaDao().obtenerPorId(id);
            mainHandler.post(() -> listener.onComplete(mascota));
        });
    }

    //U
    public void actualizar(Mascota mascota, OnOperacionListener listener) {
        executorService.execute(() -> {
            int filas = db.mascotaDao().actualizar(mascota);
            mainHandler.post(() -> listener.onComplete(filas > 0));
        });
    }

    //D
    public void eliminar(Mascota mascota, OnOperacionListener listener) {
        executorService.execute(() -> {
            int filas = db.mascotaDao().eliminar(mascota);
            mainHandler.post(() -> listener.onComplete(filas > 0));
        });
    }

    //Interfaces
    // Regresa una sola mascota
    public interface OnMascotaLoadedListener {
        void onComplete(Mascota mascota);
    }

    // Regresa el listado de mascotas
    public interface OnMascotasLoadListener {
        void onComplete(List<Mascota> mascotas);
    }

    // Avisa si la operación (insert/update/delete) se realizó
    public interface OnOperacionListener {
        void onComplete(boolean exito);
    }
}
