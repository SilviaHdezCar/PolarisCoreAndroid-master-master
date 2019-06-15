package com.example.wposs_user.polariscoreandroid.java;

import org.json.JSONException;
import org.json.JSONObject;

public class Tipificacion implements Comparable<Tipificacion>{

   private String tetv_id;
    private String tetv_description;
    private String estado;

    public Tipificacion() {
    }

    public Tipificacion(String tetv_id, String tetv_description) {
        this.tetv_id = tetv_id;
        this.tetv_description = tetv_description;
    }

    public Tipificacion(String tetv_id, String tetv_description, String estado) {
        this.tetv_id = tetv_id;
        this.tetv_description = tetv_description;
        this.estado = estado;
    }



    public String getTetv_id() {
        return tetv_id;
    }

    public void setTetv_id(String tetv_id) {
        this.tetv_id = tetv_id;
    }

    public String getTetv_description() {
        return tetv_description;
    }

    public void setTetv_description(String tetv_description) {
        this.tetv_description = tetv_description;
    }


    public JSONObject getObj() {


        JSONObject obj = new JSONObject();
        try {
            obj.put("tets_terminal_serial",tetv_id);
            obj.put("tets_terminal_type_validation",tetv_description);
            obj.put("tets_status",estado);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    @Override
    public String toString() {
        return "Tipificacion{" +
                "tetv_id='" + tetv_id + '\'' +
                ", tetv_description='" + tetv_description + '\'' +
                '}';
    }

    @Override
    public int compareTo(Tipificacion o) {
        String a=new String(String.valueOf(this.getTetv_description()));
        String b=new String(String.valueOf(o.getTetv_description()));
        return a.compareTo(b);
    }
}
