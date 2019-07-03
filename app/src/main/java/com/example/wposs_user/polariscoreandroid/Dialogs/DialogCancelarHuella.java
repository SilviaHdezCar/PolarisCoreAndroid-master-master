package com.example.wposs_user.polariscoreandroid.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValuesLogueoHuella;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.getValueStrPreferenceLogueoHuella;

public class DialogCancelarHuella extends DialogFragment {

    private View view;
    private Button cancelar;
    private Button aceptar;
    private TextView text_mensaje;
    private ImageView img_huella;


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.cancelar_huella, null);

        text_mensaje = (TextView) view.findViewById(R.id.text_mensaje);
        cancelar = (Button) view.findViewById(R.id.btn_cancelarHuella);
        aceptar = (Button) view.findViewById(R.id.btn_AceptarHuella);
        img_huella = (ImageView) view.findViewById(R.id.img_huella);
        setCancelable(false);

        if(getValueStrPreferenceLogueoHuella(objeto,"email_user")==null
                ||getValueStrPreferenceLogueoHuella(objeto,"email_user").isEmpty()){
            img_huella.setVisibility(View.GONE);
            text_mensaje.setText("El inicio de sesión mediante huella se encuentra deshabilitado");
            aceptar.setVisibility(View.GONE);
            cancelar.setGravity(Gravity.CENTER);

        }

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
