package com.upiiz.dm_degr_07.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "usuarios",
        indices = {@Index(value = {"email"}, unique = true)})
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @NonNull
    @ColumnInfo(name = "password")
    private String password;

    @NonNull
    @ColumnInfo(name = "nombre")
    private String nombre;

    // Constructor que usa Room
    public Usuario(Long id, @NonNull String email, @NonNull String password, @NonNull String nombre) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
    }

    // Constructor para registrar (id autogenerado)
    @Ignore
    public Usuario(@NonNull String email, @NonNull String password, @NonNull String nombre) {
        this(null, email, password, nombre);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @NonNull
    public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }

    @NonNull
    public String getPassword() { return password; }
    public void setPassword(@NonNull String password) { this.password = password; }

    @NonNull
    public String getNombre() { return nombre; }
    public void setNombre(@NonNull String nombre) { this.nombre = nombre; }
}
