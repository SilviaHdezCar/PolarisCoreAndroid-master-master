package com.example.wposs_user.polariscoreandroid.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValuesLogueoHuella;

public class DialogCancelarHuella extends DialogFragment {

    private View view;
    private Button cancelar;
    private Button aceptar;


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.cancelar_huella, null);

        cancelar = (Button) view.findViewById(R.id.btn_cancelarHuella);
        aceptar = (Button) view.findViewById(R.id.btn_AceptarHuella);
        setCancelable(false);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarValuesLogueoHuella(objeto);
                dismiss();
            }
        });
        dialogo.setView(view);
        return dialogo.create();
    }


}
