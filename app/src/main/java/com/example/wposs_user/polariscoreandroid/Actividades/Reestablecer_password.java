package com.example.wposs_user.polariscoreandroid.Actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wposs_user.polariscoreandroid.R;

public class Reestablecer_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reestablecer_password);

    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Activity_login.class);
        startActivity(i);
        finish();
    }


}
