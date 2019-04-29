package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesReparadasFragm;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;
import com.example.wposs_user.polariscoreandroid.Fragmentos.Registro_diagnostico;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogFallaDetectada extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();


        builder
                .setTitle("Falla detectada por: ")
                .setItems(R.array.falla_detectada, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Global.fallaDetectada="Uso";
                            //consumir servioco repuestos
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).commit();

                        } else if (which == 1) {
                            Global.fallaDetectada="Fábrica";
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).commit();
                        }
                    }
                });
        return builder.create();
    }




}
