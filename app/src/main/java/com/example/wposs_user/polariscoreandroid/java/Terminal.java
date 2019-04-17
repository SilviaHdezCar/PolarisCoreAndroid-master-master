package com.example.wposs_user.polariscoreandroid.java;

import java.util.ArrayList;
import java.util.Date;

public class Terminal {

    private String term_serial;
    private String term_imei;
    private String brand;
    private String term_model;
    private String term_technology;
    private String term_status; //estado;//
    private String  term_security_seal;//sellos;//
    private String term_mk;
    private String term_buy_date;///"fechaCompra;
    private String term_start_date_warranty;//":"NaN"fechaInicioGarantía;
    private String term_date_register; //fechaRegistro;//
    private String term_date_finish;//fechaLimite;//
    private  String term_warranty_time;//periodoGarantía;//

    private String term_localication;//ubicacion;/

    public Terminal(String term_serial, String term_imei, String brand, String term_model,
                    String term_technology, String term_status, String term_security_seal, String term_mk,
                    String term_buy_date, String term_start_date_warranty, String term_date_register,
                    String term_date_finish, String term_warranty_time, String term_localication) {
        this.term_serial = term_serial;
        this.term_imei = term_imei;
        this.brand = brand;
        this.term_model = term_model;
        this.term_technology = term_technology;
        this.term_status = term_status;
        this.term_security_seal = term_security_seal;
        this.term_mk = term_mk;
        this.term_buy_date = term_buy_date;
        this.term_start_date_warranty = term_start_date_warranty;
        this.term_date_register = term_date_register;
        this.term_date_finish = term_date_finish;
        this.term_warranty_time = term_warranty_time;
        this.term_localication = term_localication;
    }



    public String getTerm_serial() {
        return term_serial;
    }

    public void setTerm_serial(String term_serial) {
        this.term_serial = term_serial;
    }

    public String getTerm_imei() {
        return term_imei;
    }

    public void setTerm_imei(String term_imei) {
        this.term_imei = term_imei;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTerm_model() {
        return term_model;
    }

    public void setTerm_model(String term_model) {
        this.term_model = term_model;
    }

    public String getTerm_technology() {
        return term_technology;
    }

    public void setTerm_technology(String term_technology) {
        this.term_technology = term_technology;
    }

    public String getTerm_status() {
        return term_status;
    }

    public void setTerm_status(String term_status) {
        this.term_status = term_status;
    }

    public String getTerm_security_seal() {
        return term_security_seal;
    }

    public void setTerm_security_seal(String term_security_seal) {
        this.term_security_seal = term_security_seal;
    }

    public String getTerm_mk() {
        return term_mk;
    }

    public void setTerm_mk(String term_mk) {
        this.term_mk = term_mk;
    }

    public String getTerm_buy_date() {
        return term_buy_date;
    }

    public void setTerm_buy_date(String term_buy_date) {
        this.term_buy_date = term_buy_date;
    }

    public String getTerm_start_date_warranty() {
        return term_start_date_warranty;
    }

    public void setTerm_start_date_warranty(String term_start_date_warranty) {
        this.term_start_date_warranty = term_start_date_warranty;
    }

    public String getTerm_date_register() {
        return term_date_register;
    }

    public void setTerm_date_register(String term_date_register) {
        this.term_date_register = term_date_register;
    }

    public String getTerm_date_finish() {
        return term_date_finish;
    }

    public void setTerm_date_finish(String term_date_finish) {
        this.term_date_finish = term_date_finish;
    }

    public String getTerm_warranty_time() {
        return term_warranty_time;
    }

    public void setTerm_warranty_time(String term_warranty_time) {
        this.term_warranty_time = term_warranty_time;
    }

    public String getTerm_localication() {
        return term_localication;
    }

    public void setTerm_localication(String term_localication) {
        this.term_localication = term_localication;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "term_serial='" + term_serial + '\'' +
                ", term_imei='" + term_imei + '\'' +
                ", brand='" + brand + '\'' +
                ", term_model='" + term_model + '\'' +
                ", term_technology='" + term_technology + '\'' +
                ", term_status='" + term_status + '\'' +
                ", term_security_seal='" + term_security_seal + '\'' +
                ", term_mk='" + term_mk + '\'' +
                ", term_buy_date='" + term_buy_date + '\'' +
                ", term_start_date_warranty='" + term_start_date_warranty + '\'' +
                ", term_date_register='" + term_date_register + '\'' +
                ", term_date_finish='" + term_date_finish + '\'' +
                ", term_warranty_time='" + term_warranty_time + '\'' +
                ", term_localication='" + term_localication + '\'' +
                '}';
    }
}

