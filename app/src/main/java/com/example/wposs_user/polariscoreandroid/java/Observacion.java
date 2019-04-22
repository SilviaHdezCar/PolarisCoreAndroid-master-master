package com.example.wposs_user.polariscoreandroid.java;

public class Observacion {

    //colocarle los mismos atributos que estan en la BD
    String teob_id;
    String teob_description;
    String teob_fecha;
    String teob_id_user;
    String teob_photo;
    String teob_serial_terminal;


    public Observacion(String teob_id, String teob_description, String teob_fecha, String teob_id_user, String teob_photo, String teob_serial_terminal) {
        this.teob_id = teob_id;
        this.teob_description = teob_description;
        this.teob_fecha = teob_fecha;
        this.teob_id_user = teob_id_user;
        this.teob_photo = teob_photo;
        this.teob_serial_terminal = teob_serial_terminal;
    }


    public String getTeob_id() {
        return teob_id;
    }

    public void setTeob_id(String teob_id) {
        this.teob_id = teob_id;
    }

    public String getTeob_description() {
        return teob_description;
    }

    public void setTeob_description(String teob_description) {
        this.teob_description = teob_description;
    }

    public String getTeob_fecha() {
        return teob_fecha;
    }

    public void setTeob_fecha(String teob_fecha) {
        this.teob_fecha = teob_fecha;
    }

    public String getTeob_id_user() {
        return teob_id_user;
    }

    public void setTeob_id_user(String teob_id_user) {
        this.teob_id_user = teob_id_user;
    }

    public String getTeob_photo() {
        return teob_photo;
    }

    public void setTeob_photo(String teob_photo) {
        this.teob_photo = teob_photo;
    }

    public String getTeob_serial_terminal() {
        return teob_serial_terminal;
    }

    public void setTeob_serial_terminal(String teob_serial_terminal) {
        this.teob_serial_terminal = teob_serial_terminal;
    }

    @Override
    public String toString() {
        return "Observacion{" +
                "teob_id='" + teob_id + '\'' +
                ", teob_description='" + teob_description + '\'' +
                ", teob_fecha='" + teob_fecha + '\'' +
                ", teob_id_user='" + teob_id_user + '\'' +
                ", teob_photo='" + teob_photo + '\'' +
                ", teob_serial_terminal='" + teob_serial_terminal + '\'' +
                '}';
    }
}
