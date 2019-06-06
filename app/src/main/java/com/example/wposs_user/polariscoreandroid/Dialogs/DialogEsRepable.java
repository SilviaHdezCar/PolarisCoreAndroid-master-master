package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ObservacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogEsRepable extends DialogFragment {
    private TextView txt_descripcion;
    private TextView txt_info1;
    private TextView txt_info2;
    private View v;
    private Button btn_cancelar;
    private String case1; //SI
    private String case2; //NO


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_falla_detectada, null);
        txt_descripcion = (TextView) v.findViewById(R.id.txt_descripcion);
        txt_info1 = (TextView) v.findViewById(R.id.txt_falla1);
        txt_info2 = (TextView) v.findViewById(R.id.txt_falla2);
        btn_cancelar = (Button) v.findViewById(R.id.btn_cancelar_falla_detectada);
       setCancelable(false);

        if (Global.panel_reparable) {
            txt_descripcion.setText("¿La terminal es reparable? ");
            txt_info1.setText("SI");
            txt_info2.setText("NO");
        }


        txt_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case1 = txt_info1.getText().toString();
                if (case1.equals("SI")) {
                    Global.reparable = "SI";
                    fallaDetectada();
                    dismiss();
                }
            }
        });

        txt_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                case2 = txt_info2.getText().toString();
                if (case2.equals("NO")) {
                    Global.reparable = "NO";
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ObservacionesFragment()).addToBackStack(null).commit();
                    dismiss();
                }
            }
        });
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(v);
        

        /*LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();

        setCancelable(false);


        builder.setTitle("¿La terminal es reparable?");
        builder.setItems(R.array.esReparable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Global.reparable="SI";
                            fallaDetectada();
                        } else if (which == 1) {
                            Global.reparable="NO";
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ObservacionesFragment()).addToBackStack(null).commit();
                        }
                    }
                });*/
        return builder.create();
    }


    public void fallaDetectada() {
        DialogFallaDetectada dialog = new DialogFallaDetectada();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }

}
