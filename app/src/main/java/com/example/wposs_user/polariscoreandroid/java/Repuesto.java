package com.example.wposs_user.polariscoreandroid.java;

public class Repuesto {

    String codigo,nombre,estado;
    int cantidad;

    public Repuesto(String codigo, String nombre, String estado, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.cantidad = cantidad;
    }

    public Repuesto() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
