package com.example.wposs_user.polariscoreandroid.java;

public class Validacion {




    private String tevs_terminal_serial;
    private String tevs_terminal_validation;
    private String tevs_status;

    public Validacion(String tevs_terminal_serial, String tevs_terminal_validation, String tevs_status) {
        this.tevs_terminal_serial = tevs_terminal_serial;
        this.tevs_terminal_validation = tevs_terminal_validation;
        this.tevs_status = tevs_status;
    }


    public Validacion() {
    }

    public String getTevs_terminal_serial() {
        return tevs_terminal_serial;
    }

    public void setTevs_terminal_serial(String tevs_terminal_serial) {
        this.tevs_terminal_serial = tevs_terminal_serial;
    }

    public String getTevs_terminal_validation() {
        return tevs_terminal_validation;
    }

    public void setTevs_terminal_validation(String tevs_terminal_validation) {
        this.tevs_terminal_validation = tevs_terminal_validation;
    }

    public String getTevs_status() {
        return tevs_status;
    }

    public void setTevs_status(String tevs_status) {
        this.tevs_status = tevs_status;
    }
}
