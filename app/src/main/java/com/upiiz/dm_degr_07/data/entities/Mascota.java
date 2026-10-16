package com.upiiz.dm_degr_07.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "mascotas",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "id",
                childColumns = "usuario_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("usuario_id")})
public class Mascota {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "nombre")
    private String nombre;

    @ColumnInfo(name = "raza")
    private String raza;

    @ColumnInfo(name = "edad")
    private int edad;

    @ColumnInfo(name = "usuario_id")
    private long usuarioId;

    // Constructor que usa Room
    public Mascota(Long id, String nombre, String raza, int edad, long usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.usuarioId = usuarioId;
    }

    // Constructor para crear una mascota nueva (id autogenerado)
    @Ignore
    public Mascota(String nombre, String raza, int edad, long usuarioId) {
        this(null, nombre, raza, edad, usuarioId);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(long usuarioId) { this.usuarioId = usuarioId; }
}
