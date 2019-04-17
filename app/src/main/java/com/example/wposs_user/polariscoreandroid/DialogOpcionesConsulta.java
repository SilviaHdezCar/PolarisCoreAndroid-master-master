package com.example.wposs_user.polariscoreandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesReparadasFragm;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;

public class DialogOpcionesConsulta extends DialogFragment {
    public static MainActivity objeto;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();


        builder
                .setTitle("Seleccione el tipo de consulta")
                .setItems(R.array.opciones_consulta, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesSerial()).commit();

                        } else if (which == 1) {
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ConsultaTerminalesReparadasFragm()).commit();
                        }
                    }
                });
        return builder.create();
    }




}
