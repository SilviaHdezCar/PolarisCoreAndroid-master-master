package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterNotificacion;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuesto;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Notificacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import java.util.ArrayList;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.R.drawable.ic_notnotificacion;

public class DialogNotificacion extends DialogFragment {


    private View view;
    private AlertDialog.Builder dialogo;
     private RecyclerView rv;
     private Button cerrarNotificacion;



    public DialogNotificacion() {
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        dialogo = new AlertDialog.Builder(objeto);
        view = getActivity().getLayoutInflater().inflate(R.layout.notificaciones, null);
        rv= (RecyclerView)view.findViewById(R.id.rv_notificaciones);
        dialogo.setCancelable(true);


        dialogo.setView(view);
        llenarRv();




        return dialogo.create();


    }



    public void llenarRv(){

System.out.println("LLENO EL RECICLER");

        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        AdapterNotificacion adapter = new AdapterNotificacion(Global.notificaciones, new AdapterNotificacion.interfaceClick() {

            public void onClick(List<Notificacion> notificaciones, int position) {
                Notificacion not= Global.notificaciones.get(position);
                 objeto.eliminarNotificacion(not.getNoti_id());
                  Global.notificaciones.remove(position);

        System.out.println("cargados en el recyclerview*********"+ Global.notificaciones.size());
        if(Global.notificaciones.size()==0){
            dismiss();
            ImageView im = (ImageView)objeto.findViewById(R.id.btn_notificaciones);
            im.setImageResource(R.mipmap.ic_campanano);
            Toast.makeText(objeto, "No tiene ninguna notificación pendiente", Toast.LENGTH_SHORT).show();
        }
                llenarRv();
            }
        },R.layout.notificaciones);

        rv.setAdapter(adapter);


    }


}
