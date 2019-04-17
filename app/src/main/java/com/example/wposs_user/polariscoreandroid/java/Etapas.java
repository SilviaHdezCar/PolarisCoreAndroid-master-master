package com.example.wposs_user.polariscoreandroid.java;

import java.util.Date;

public class Etapas {

    String observacion;
    private Date fecha;
    private Usuario usuario;

    public Etapas(String observacion, Date fecha, Usuario usuario) {
        this.observacion = observacion;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
