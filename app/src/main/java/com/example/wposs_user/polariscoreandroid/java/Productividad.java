package com.example.wposs_user.polariscoreandroid.java;

public class Productividad {

    String uste_user;
    String uste_date;
    Float uste_associated_terminals;
    Float uste_completed_terminals;


    public Productividad(String uste_user, String uste_date, Float uste_associated_terminals, Float uste_completed_terminals) {
        this.uste_user = uste_user;
        this.uste_date = uste_date;
        this.uste_associated_terminals = uste_associated_terminals;
        this.uste_completed_terminals = uste_completed_terminals;
    }


    public String getUste_user() {
        return uste_user;
    }

    public void setUste_user(String uste_user) {
        this.uste_user = uste_user;
    }

    public String getUste_date() {
        return uste_date;
    }

    public void setUste_date(String uste_date) {
        this.uste_date = uste_date;
    }

    public Float getUste_associated_terminals() {
        return uste_associated_terminals;
    }

    public void setUste_associated_terminals(Float uste_associated_terminals) {
        this.uste_associated_terminals = uste_associated_terminals;
    }

    public Float getUste_completed_terminals() {
        return uste_completed_terminals;
    }

    public void setUste_completed_terminals(Float uste_completed_terminals) {
        this.uste_completed_terminals = uste_completed_terminals;
    }
}
