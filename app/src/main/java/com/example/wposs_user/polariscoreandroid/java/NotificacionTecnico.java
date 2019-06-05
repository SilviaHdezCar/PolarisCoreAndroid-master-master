package com.example.wposs_user.polariscoreandroid.java;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogNotificacion;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.R.drawable.ic_sinnotif;

//clase que permite ejecutar en2 plano el servicio de notificaciones actualizandolo cada 15 minutos


public class NotificacionTecnico implements Runnable {



    public NotificacionTecnico() {
    }

    @Override
    public void run() {

        try {
            while (Boolean.TRUE){

                objeto.consumirServicioNotificaciones();

                Thread.sleep(30000);


            }
        }catch (Exception e){


        }
        objeto.consumirServicioNotificaciones();

    }



}
