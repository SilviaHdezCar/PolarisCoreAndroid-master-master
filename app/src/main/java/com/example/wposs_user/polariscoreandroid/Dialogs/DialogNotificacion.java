package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.wposs_user.polariscoreandroid.R;

public class DialogNotificacion extends DialogFragment {


      private View view;
      AlertDialog.Builder dialogo;
     RecyclerView rv;



    public DialogNotificacion() {
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialogo = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.notificaciones, null);
        rv= (RecyclerView)view.findViewById(R.id.rv_notificaciones);

        dialogo.setView(view);
        return dialogo.create();


    }
}
