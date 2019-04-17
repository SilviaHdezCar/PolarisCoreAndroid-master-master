package com.example.wposs_user.polariscoreandroid.Actividades;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;

public class DialogError extends AppCompatDialogFragment {
    private TextView textAlert;
    private TextView txtviewError;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder
                .setTitle("INFORMACION!")
                .setMessage(Global.mensaje)

                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            return;
                    }
                });
        return builder.create();
    }



}
