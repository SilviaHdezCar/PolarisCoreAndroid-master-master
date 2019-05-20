package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.example.wposs_user.polariscoreandroid.R;

public class DialogHuella  extends DialogFragment {

    private View view;
    private Button cancelar;


    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
        view= getActivity().getLayoutInflater().inflate(R.layout.login_huella,null);
        cancelar=(Button)view.findViewById(R.id.cancelar_login);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialogo.setView(view);

        return dialogo.create();



    }






}
