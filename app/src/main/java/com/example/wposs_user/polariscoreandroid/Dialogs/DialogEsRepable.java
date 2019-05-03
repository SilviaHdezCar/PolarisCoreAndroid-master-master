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
import com.example.wposs_user.polariscoreandroid.Fragmentos.ObservacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogEsRepable extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();


        builder
                .setTitle("¿La terminal es repable?")
                .setItems(R.array.esReparable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Global.reparable="SI";
                            fallaDetectada();
                        } else if (which == 1) {
                            Global.reparable="NO";
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ObservacionesFragment()).commit();
                        }
                    }
                });
        return builder.create();
    }


    public void fallaDetectada() {
        DialogFallaDetectada dialog = new DialogFallaDetectada();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }

}
