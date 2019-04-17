package com.example.wposs_user.polariscoreandroid.java;

public class Validacion {

    private String teva_id;
    private String teva_description;


    public Validacion(String teva_id, String teva_description) {
        this.teva_id = teva_id;
        this.teva_description = teva_description;
    }

    public Validacion() {
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

    @Override
    public String toString() {
        return "Validacion{" +
                "teva_id='" + teva_id + '\'' +
                ", teva_description='" + teva_description + '\'' +
                '}';
    }
}
