package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ObservacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogEsRepable extends DialogFragment {
    private TextView txt_descripcion;
    private TextView btn_info1;
    private TextView btn_info2;
    private View v;
    private String case1; //SI
    private String case2; //NO


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_falla_detectada, null);
        txt_descripcion = (TextView) v.findViewById(R.id.txt_descripcion);
        btn_info1 = (Button) v.findViewById(R.id.btn_opcion1);
        btn_info2 = (Button) v.findViewById(R.id.btn_opcion2);
        setCancelable(true);

        if (Global.panel_reparable) {
            txt_descripcion.setText("¿La terminal es reparable? ");
            btn_info1.setText("SI");
            btn_info2.setText("NO");
        }


        btn_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case1 = btn_info1.getText().toString();
                if (case1.equals("SI")) {
                    Global.reparable = "SI";
                    fallaDetectada();
                    dismiss();
                }
            }
        });

        btn_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case2 = btn_info2.getText().toString();
                if (case2.equals("NO")) {
                    Global.reparable = "NO";
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ObservacionesFragment()).addToBackStack(null).commit();
                    dismiss();
                }
            }
        });

        builder.setView(v);

        return builder.create();
    }


    public void fallaDetectada() {
        DialogFallaDetectada dialog = new DialogFallaDetectada();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }

}
