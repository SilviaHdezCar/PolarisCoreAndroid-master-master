package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Fragmentos.EtapasNuevoD_autorizadas;
import com.example.wposs_user.polariscoreandroid.Fragmentos.Registro_diagnostico;
import com.example.wposs_user.polariscoreandroid.Fragmentos.RepuestoDefectuosoAutorizadas;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class DialogFallaDetectada extends DialogFragment {

    private TextView txt_descripcion;
    private Button btn_info1;
    private Button btn_info2;
    private View v;

    private String falla1;
    private String falla2;
    //validar si es asociada o autoizada
    //si es asociada -->se muestran los tipos de falla: uso-fabrica
    //si esautorizada -->se muestran los tipos de falla: repuesto defectuoso-nuevo diagnostico


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        v = getActivity().getLayoutInflater().inflate(R.layout.dialog_falla_detectada, null);
        txt_descripcion = (TextView) v.findViewById(R.id.txt_descripcion);
        btn_info1 = (Button) v.findViewById(R.id.btn_opcion1);
        btn_info2 = (Button) v.findViewById(R.id.btn_opcion2);
        setCancelable(true);
        btn_info1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        btn_info2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        if (Global.diagnosticoTerminal.equals("asociada")) {
            txt_descripcion.setText("Falla detectada por: ");
            btn_info1.setText("Uso");
            btn_info2.setText("Fábrica");
        } else if (Global.diagnosticoTerminal.equals("autorizada")) {
            txt_descripcion.setText("Falla detectada por: ");
            btn_info1.setText("Repuesto defectuoso");
            btn_info2.setText("Nuevo Diagnóstico");
            btn_info1.setWidth(130);
            btn_info2.setWidth(130);
            btn_info1.setPadding(0, 10,5,10);
            btn_info2.setPadding(0, 10,5,10);
        }

        btn_info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                falla1 = btn_info1.getText().toString();
                if (falla1.equals("Uso")) {
                    Global.fallaDetectada = "USO";
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).addToBackStack(null).commit();
                    dismiss();
                } else if (falla1.equals("Repuesto defectuoso")) {
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new RepuestoDefectuosoAutorizadas()).addToBackStack(null).commit();
                    dismiss();
                }
            }
        });

        btn_info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                falla2 = btn_info2.getText().toString();
                if (falla2.equals("Fábrica")) {
                    Global.fallaDetectada = "FABRICA";
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).addToBackStack(null).commit();
                    dismiss();
                } else if (falla2.equals("Nuevo Diagnóstico")) {
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasNuevoD_autorizadas()).addToBackStack(null).commit();
                    dismiss();
                }
            }
        });


        builder.setView(v);

       /*
        LayoutInflater inflater = getActivity().getLayoutInflater();


        //  View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        final FragmentManager fragmentManager = objeto.getSupportFragmentManager();
        setCancelable(false);


        builder
                .setTitle("Falla detectada por: ")
                .setItems(R.array.falla_detectada, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Global.fallaDetectada = "USO";
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).addToBackStack(null).commit();

                        } else if (which == 1) {
                            Global.fallaDetectada = "FABRICA";
                            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new Registro_diagnostico()).addToBackStack(null).commit();
                        }
                    }
                });*/
        return builder.create();
    }


}
