package com.example.wposs_user.polariscoreandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Actualizar_clave extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_clave);


    }

    public void cambiar_clave(View v) {

    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Activity_login.class);
        startActivity(i);
        finish();
    }
}
