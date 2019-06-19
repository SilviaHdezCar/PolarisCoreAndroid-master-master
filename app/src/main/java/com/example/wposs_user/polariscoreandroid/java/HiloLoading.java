package com.example.wposs_user.polariscoreandroid.java;

import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogLoading;

import java.util.ArrayList;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//clase mostrar el panel dialog


public class HiloLoading implements Runnable {

    private Boolean correr = TRUE;
    private DialogLoading dialog;

    public HiloLoading() {
    }

    @Override
    public void run() {
        try { dialog = new DialogLoading();
            while (correr) {

                dialog.show(objeto.getSupportFragmentManager(), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        correr = FALSE;
    }
}
