package com.example.wposs_user.polariscoreandroid.java;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class Productividad implements Comparable<Productividad>{

    String uste_user;
    String uste_date;
    String uste_associated_terminals;
    String uste_completed_terminals;
    String uste_diagnosed_terminals;
    String uste_repaired_terminals;


    public Productividad(String uste_user, String uste_date, String uste_associated_terminals, String uste_completed_terminals, String uste_diagnosed_terminals, String uste_repaired_terminals) {
        this.uste_user = uste_user;
        this.uste_date = uste_date;
        this.uste_associated_terminals = uste_associated_terminals;
        this.uste_completed_terminals = uste_completed_terminals;
        this.uste_diagnosed_terminals = uste_diagnosed_terminals;
        this.uste_repaired_terminals = uste_repaired_terminals;
    }

    public String getUste_diagnosed_terminals() {
        return uste_diagnosed_terminals;
    }

    public void setUste_diagnosed_terminals(String uste_diagnosed_terminals) {
        this.uste_diagnosed_terminals = uste_diagnosed_terminals;
    }

    public String getUste_repaired_terminals() {
        return uste_repaired_terminals;
    }

    public void setUste_repaired_terminals(String uste_repaired_terminals) {
        this.uste_repaired_terminals = uste_repaired_terminals;
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

    public String getUste_associated_terminals() {
        return uste_associated_terminals;
    }

    public void setUste_associated_terminals(String uste_associated_terminals) {
        this.uste_associated_terminals = uste_associated_terminals;
    }

    public String getUste_completed_terminals() {
        return uste_completed_terminals;
    }

    public void setUste_completed_terminals(String uste_completed_terminals) {
        this.uste_completed_terminals = uste_completed_terminals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productividad that = (Productividad) o;
        return uste_date.equals(that.uste_date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(uste_date);
    }

    @Override
    public String toString() {
        return "Productividad{" +
                "uste_user='" + uste_user + '\'' +
                ", uste_date='" + uste_date + '\'' +
                ", uste_associated_terminals=" + uste_associated_terminals +
                ", uste_completed_terminals=" + uste_completed_terminals +
                '}';
    }

    @Override
    public int compareTo(Productividad o) {
        String dia1= getUste_date().toString();
        String[]fecha1= dia1.split("/");
        int diadado1= Integer.parseInt(fecha1[0]);
        String dia2= o.getUste_date().toString();
        String[]fecha2= dia2.split("/");
        int diadado2= Integer.parseInt(fecha2[0]);

        if(diadado1<diadado2){
            return -1;

        }

        if(diadado2<diadado1){

            return 1;
        }


        return 0;
    }
}
