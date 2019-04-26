package com.example.wposs_user.polariscoreandroid.java;

public class Validacion {




    private String teva_id;
    private String teva_description;
    private boolean ok=false;
    private boolean falla;
    private boolean no_aplica;
    private String estado;


    public Validacion(String teva_id, String teva_description) {
        this.teva_id = teva_id;
        this.teva_description = teva_description;
        this.ok=false;
        this.falla=false;
        this.no_aplica=false;
        this.estado="";
    }

    public String getTeva_id() {
        return teva_id;
    }

    public void setTeva_id(String teva_id) {
        this.teva_id = teva_id;
    }

    public String getTeva_description() {
        return teva_description;
    }

    public void setTeva_description(String teva_description) {
        this.teva_description = teva_description;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isFalla() {
        return falla;
    }

    public void setFalla(boolean falla) {
        this.falla = falla;
    }

    public boolean isNo_aplica() {
        return no_aplica;
    }

    public void setNo_aplica(boolean no_aplica) {
        this.no_aplica = no_aplica;
    }

    @Override
    public String toString() {
        return "Validacion{" +
                "teva_id='" + teva_id + '\'' +
                ", teva_description='" + teva_description + '\'' +
                '}';
    }
}
