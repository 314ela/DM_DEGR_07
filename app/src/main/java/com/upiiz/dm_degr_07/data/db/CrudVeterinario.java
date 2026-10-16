package com.upiiz.dm_degr_07.data.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.upiiz.dm_degr_07.data.entities.Veterinario;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// crud del vet
public class CrudVeterinario {

    private final AppDataBase db;
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CrudVeterinario(Context context) {
        db = AppDataBase.getInstance(context);
    }

    public void agregar(Veterinario veterinario) {
        executorService.execute(() -> db.veterinarioDao().crear(veterinario));
    }

    public void actualizar(Veterinario veterinario) {
        executorService.execute(() -> db.veterinarioDao().actualizar(veterinario));
    }

    public void eliminar(Veterinario veterinario) {
        executorService.execute(() -> db.veterinarioDao().eliminar(veterinario));
    }

    public void obtenerTodos(OnVeterinariosLoadListener listener) {
        executorService.execute(() -> {
            List<Veterinario> lista = db.veterinarioDao().obtenerTodos();
            mainHandler.post(() -> listener.onComplete(lista));
        });
    }

    public interface OnVeterinariosLoadListener {
        void onComplete(List<Veterinario> veterinarios);
    }
}
