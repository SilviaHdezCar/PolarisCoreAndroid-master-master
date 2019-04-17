package com.example.wposs_user.polariscoreandroid.java;

public class Observacion {

    //colocarle los mismos atributos que estan en la BD
    String fecha;
    String usuario;
    String observacion;

    public Observacion(String fecha, String usuario, String observacion) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.observacion = observacion;
    }

    public Observacion() {

    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Observacion{" +
                "fecha='" + fecha + '\'' +
                ", usuario='" + usuario + '\'' +
                ", observacion='" + observacion + '\'' +
                '}';
    }
}
