package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.EtapasNuevoD_autorizadas;
import com.example.wposs_user.polariscoreandroid.Fragmentos.Registro_diagnostico;
import com.example.wposs_user.polariscoreandroid.Fragmentos.RepuestoDefectuosoAutorizadas;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogFallaDetectada_autorizadas extends DialogFragment {




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();

        setCancelable(false);


        builder
                .setTitle("Falla detectada por: ")
                .setItems(R.array.falla_detectada_autorizadas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            //inflar panel de repuesto defectuoso
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new RepuestoDefectuosoAutorizadas()).addToBackStack(null).commit();

                        } else if (which == 1) {
                            //inflar panel de nuevo díagnostico
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new EtapasNuevoD_autorizadas()).addToBackStack(null).commit();
                        }
                    }
                });
        return builder.create();
    }




}
