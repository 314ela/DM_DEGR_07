package com.upiiz.dm_degr_07.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.upiiz.dm_degr_07.data.entities.Mascota;

import java.util.List;

@Dao
public interface MascotaDao {

    @Query("SELECT * FROM mascotas")
    List<Mascota> obtenerTodos();

    // Solo las mascotas del usuario logueado
    @Query("SELECT * FROM mascotas WHERE usuario_id = :usuarioId ORDER BY nombre COLLATE NOCASE")
    List<Mascota> obtenerPorUsuario(long usuarioId);

    @Query("SELECT * FROM mascotas WHERE id = :id")
    Mascota obtenerPorId(Long id);

    @Insert
    long crear(Mascota mascota);

    @Update
    int actualizar(Mascota mascota);

    // @Delete recibe la entidad completa (Room la busca por su llave primaria)
    @Delete
    int eliminar(Mascota mascota);
}
