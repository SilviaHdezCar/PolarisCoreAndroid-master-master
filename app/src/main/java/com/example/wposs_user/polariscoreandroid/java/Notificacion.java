package com.example.wposs_user.polariscoreandroid.java;

public class Notificacion {

    private String noti_id;
    private String noti_date_create;
    private String noti_dest;
    private String noti_msg;
    private String noti_origin;
    private String noti_state;
    private String noti_type;


    public Notificacion(String noti_id, String noti_date_create, String noti_dest, String noti_msg, String noti_origin, String noti_state, String noti_type) {
        this.noti_id = noti_id;
        this.noti_date_create = noti_date_create;
        this.noti_dest = noti_dest;
        this.noti_msg = noti_msg;
        this.noti_origin = noti_origin;
        this.noti_state = noti_state;
        this.noti_type = noti_type;
    }


    public String getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(String noti_id) {
        this.noti_id = noti_id;
    }

    public String getNoti_date_create() {
        return noti_date_create;
    }

    public void setNoti_date_create(String noti_date_create) {
        this.noti_date_create = noti_date_create;
    }

    public String getNoti_dest() {
        return noti_dest;
    }

    public void setNoti_dest(String noti_dest) {
        this.noti_dest = noti_dest;
    }

    public String getNoti_msg() {
        return noti_msg;
    }

    public void setNoti_msg(String noti_msg) {
        this.noti_msg = noti_msg;
    }

    public String getNoti_origen() {
        return noti_origin;
    }

    public void setNoti_origen(String noti_origen) {
        this.noti_origin = noti_origen;
    }

    public String getNoti_state() {
        return noti_state;
    }

    public void setNoti_state(String noti_state) {
        this.noti_state = noti_state;
    }

    public String getNoti_type() {
        return noti_type;
    }

    public void setNoti_type(String noti_type) {
        this.noti_type = noti_type;
    }



}

