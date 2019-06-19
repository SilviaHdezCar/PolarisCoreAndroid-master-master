package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ObservacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogLoading extends DialogFragment {

    private View v;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_loading, null);

        setCancelable(true);

        builder.setView(v);
        cerrar();

        return builder.create();
    }

    public void cerrar() {
        if(!Global.loading){
            dismiss();
        }
    }
}
