package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wposs_user.polariscoreandroid.R;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static com.example.wposs_user.polariscoreandroid.Actividades.Activity_login.objeto_login;

public class DialogHuella  extends DialogFragment {

    private View view;
    private Button cancelar;
    AlertDialog.Builder dialogo;
    private FingerprintManager fingerprintManager;
    ImageView img;


    public DialogHuella() {
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogo = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.login_huella, null);
        cancelar = (Button) view.findViewById(R.id.cancelar_login);
        img=(ImageView)view.findViewById(R.id.imagen_huella);
        setCancelable(false);


          cancelar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                dismiss();


            }


        });
        dialogo.setView(view);


        return dialogo.create();


    }

}