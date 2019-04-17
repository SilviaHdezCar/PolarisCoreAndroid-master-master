package com.example.wposs_user.polariscoreandroid.java;

public class Tipificacion {

    String nombre;

    @Override
    public String toString() {
        return "Tipificacion{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
