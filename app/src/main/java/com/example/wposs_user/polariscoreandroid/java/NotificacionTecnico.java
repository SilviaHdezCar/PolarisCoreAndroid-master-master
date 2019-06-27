package com.example.wposs_user.polariscoreandroid.java;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogNotificacion;
import com.example.wposs_user.polariscoreandroid.R;

import java.util.ArrayList;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.R.drawable.ic_sinnotif;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//clase que permite ejecutar en2 plano el servicio de notificaciones actualizandolo cada 20 SEGUNDOS


public class NotificacionTecnico implements Runnable {

    private Boolean correr= TRUE;


    public NotificacionTecnico() {
    }

    @Override
    public void run() {

        try {
            while (correr){
                Global.notificaciones= new ArrayList<>();
                objeto.consumirServicioNotificaciones();

                      Thread.sleep(20000);


            }



        }catch (Exception e){

            Toast.makeText(objeto, "Ha ocurrido un error al cargar las notificaciones", Toast.LENGTH_SHORT).show();

        }


           }

           public void stop(){
           correr= FALSE;

           }



}
