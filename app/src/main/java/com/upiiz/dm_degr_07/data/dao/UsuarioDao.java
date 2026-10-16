package com.upiiz.dm_degr_07.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.upiiz.dm_degr_07.data.entities.Usuario;

@Dao
public interface UsuarioDao {

    // ABORT si el email ya existe lanza excepción
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long crear(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario obtenerPorEmail(String email);

    //Validación del login contra la base de datos
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario obtenerPorId(long id);
}
