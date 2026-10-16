package com.upiiz.dm_degr_07.data.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "veterinarios")
public class Veterinario {

    // Toda entidad de Room necesita una llave primaria
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String nombre;
    private int edad;
    private String cedula;

    public Veterinario(Long id, String nombre, int edad, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.cedula = cedula;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getCedula() { return cedula; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setCedula(String cedula) { this.cedula = cedula; }
}
