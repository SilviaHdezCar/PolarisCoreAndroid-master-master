package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.getValueStrPreference;

public class Inicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        setTitle(null);

        Global.TOKEN =   getValueStrPreference(Inicial.this, "token");
        Global.ROL =  getValueStrPreference(Inicial.this, "rol");
        Global.LOGIN =  getValueStrPreference(Inicial.this, "login");
        Global.ID = getValueStrPreference(Inicial.this, "id");
        Global.STATUS =getValueStrPreference(Inicial.this, "status");
        Global.POSITION = getValueStrPreference(Inicial.this, "position");
        Global.CODE =getValueStrPreference(Inicial.this, "code");
        Global.NOMBRE =   getValueStrPreference(Inicial.this, "nombre");
        Global.LOCATION = getValueStrPreference(Inicial.this, "location");
        Global.EMAIL = getValueStrPreference(Inicial.this, "email");
        Global.PHONE =   getValueStrPreference(Inicial.this, "phone");
        System.out.println("----token----"+Global.TOKEN);
        if (Global.TOKEN == null || Global.TOKEN.isEmpty()) {
            Intent i = new Intent(Inicial.this, Activity_login.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(Inicial.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }


}
