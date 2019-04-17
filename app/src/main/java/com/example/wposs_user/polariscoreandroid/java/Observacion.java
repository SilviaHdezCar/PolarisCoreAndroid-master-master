package com.example.wposs_user.polariscoreandroid.java;

public class Observacion {

    //colocarle los mismos atributos que estan en la BD
    String teob_description;
    String teob_serial_terminal;
    String teob_photo;

    public Observacion(String teob_description, String teob_serial_terminal, String teob_photo) {
        this.teob_description = teob_description;
        this.teob_serial_terminal = teob_serial_terminal;
        this.teob_photo = teob_photo;
    }

    public Observacion() {
    }

    public String getTeob_description() {
        return teob_description;
    }

    public void setTeob_description(String teob_description) {
        this.teob_description = teob_description;
    }

    public String getTeob_serial_terminal() {
        return teob_serial_terminal;
    }

    public void setTeob_serial_terminal(String teob_serial_terminal) {
        this.teob_serial_terminal = teob_serial_terminal;
    }

    public String getTeob_photo() {
        return teob_photo;
    }

    public void setTeob_photo(String teob_photo) {
        this.teob_photo = teob_photo;
    }
}
