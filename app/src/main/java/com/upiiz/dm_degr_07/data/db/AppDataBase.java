package com.upiiz.dm_degr_07.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.upiiz.dm_degr_07.data.dao.MascotaDao;
import com.upiiz.dm_degr_07.data.dao.UsuarioDao;
import com.upiiz.dm_degr_07.data.dao.VeterinarioDao;
import com.upiiz.dm_degr_07.data.entities.Mascota;
import com.upiiz.dm_degr_07.data.entities.Usuario;
import com.upiiz.dm_degr_07.data.entities.Veterinario;

@Database(entities = {Usuario.class, Mascota.class, Veterinario.class},
        version = 1,
        exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract MascotaDao mascotaDao();
    public abstract VeterinarioDao veterinarioDao();

    private static volatile AppDataBase INSTANCE;


    public static AppDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDataBase.class,
                                    "veterinaria")
                            .fallbackToDestructiveMigration(true)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
