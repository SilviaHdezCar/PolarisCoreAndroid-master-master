package com.example.wposs_user.polariscoreandroid.java;

import org.json.JSONException;
import org.json.JSONObject;

public class Repuesto {



    String spar_id,spar_code,spar_date_register,spar_name,spar_register_by,spar_status,spar_status_temporal,spar_terminal_model,spar_warehouse;
    int spar_quantity;

    public Repuesto() {
    }

    public Repuesto(String spar_code, String spar_name, int spar_quantity) {
        this.spar_code = spar_code;
        this.spar_name = spar_name;
        this.spar_quantity = spar_quantity;
    }

    public Repuesto(String spar_id, String spar_code, String spar_date_register, String spar_name, int spar_quantity, String spar_register_by, String spar_status, String spar_status_temporal, String spar_terminal_model, String spar_warehouse) {
        this.spar_id = spar_id;
        this.spar_code = spar_code;
        this.spar_date_register = spar_date_register;
        this.spar_name = spar_name;
        this.spar_quantity = spar_quantity;
        this.spar_register_by = spar_register_by;
        this.spar_status = spar_status;
        this.spar_status_temporal = spar_status_temporal;
        this.spar_terminal_model = spar_terminal_model;
        this.spar_warehouse = spar_warehouse;
    }

    public String getSpar_id() {
        return spar_id;
    }

    public void setSpar_id(String spar_id) {
        this.spar_id = spar_id;
    }

    public String getSpar_code() {
        return spar_code;
    }

    public void setSpar_code(String spar_code) {
        this.spar_code = spar_code;
    }

    public String getSpar_date_register() {
        return spar_date_register;
    }

    public void setSpar_date_register(String spar_date_register) {
        this.spar_date_register = spar_date_register;
    }

    public String getSpar_name() {
        return spar_name;
    }

    public void setSpar_name(String spar_name) {
        this.spar_name = spar_name;
    }

    public int getSpar_quantity() {
        return spar_quantity;
    }

    public void setSpar_quantity(int spar_quantity) {
        this.spar_quantity = spar_quantity;
    }

    public String getSpar_register_by() {
        return spar_register_by;
    }

    public void setSpar_register_by(String spar_register_by) {
        this.spar_register_by = spar_register_by;
    }

    public String getSpar_status() {
        return spar_status;
    }

    public void setSpar_status(String spar_status) {
        this.spar_status = spar_status;
    }

    public String getSpar_status_temporal() {
        return spar_status_temporal;
    }

    public void setSpar_status_temporal(String spar_status_temporal) {
        this.spar_status_temporal = spar_status_temporal;
    }

    public String getSpar_terminal_model() {
        return spar_terminal_model;
    }

    public void setSpar_terminal_model(String spar_terminal_model) {
        this.spar_terminal_model = spar_terminal_model;
    }

    public String getSpar_warehouse() {
        return spar_warehouse;
    }

    public void setSpar_warehouse(String spar_warehouse) {
        this.spar_warehouse = spar_warehouse;
    }

    @Override
    public String toString() {

        return "{"+(char)34+ "codigo"+(char)34 +":"+(char)34+ spar_code +(char)34+","+(char)34+ "nombre"+(char)34 +":"+(char)34+ spar_name +(char)34+","+(char)34+ "cantidad"+(char)34+":"+ (char)34+spar_quantity+(char)34+"}";

    }


    public JSONObject getObj() {


        JSONObject obj = new JSONObject();
        try {
            obj.put("codigo",spar_code);
            obj.put("nombre",spar_code);
            obj.put("cantidad",String.valueOf(spar_quantity));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
