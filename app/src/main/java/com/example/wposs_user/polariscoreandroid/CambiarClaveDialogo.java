package com.example.wposs_user.polariscoreandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CambiarClaveDialogo extends AppCompatDialogFragment {
    private TextView claveActual;
    private TextView clavenueva;
    private TextView claveConfirmarClave;





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialogcambiarclave, null);
        claveActual = view.findViewById(R.id.dialog_clave_actual);
        clavenueva = view.findViewById(R.id.dialog_clave_nueva);
        claveConfirmarClave = view.findViewById(R.id.dialog_clave_confirmar);

        builder.setView(view)

                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String actual=claveActual.getText().toString();
                        String nueva=clavenueva.getText().toString();
                        String confirmacion=claveConfirmarClave.getText().toString();

                        final String msj=validarClave(actual, nueva,confirmacion);
                        Toast.makeText(getContext(), msj, Toast.LENGTH_LONG).show();

                        if(!msj.equalsIgnoreCase("Actualización exitosa")){

                        }else{
                            Toast.makeText(getContext(), msj, Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                            return;
                    }
                });
        return builder.create();
    }








    //este metodo hace las validaciones escritas en el cuaderno
    private String validarClave(String actual, String nueva, String confirmacion) {
        String msj="Actualización exitosa";
        //validaciones
        if (actual.isEmpty()) {
            return "Debe ingresar la contraseña actual";
        }else if(nueva.isEmpty()){
            return "Debe ingresar la contraseña nueva";
        }else if(confirmacion.isEmpty()){
            return "Debe confirmar la contraseña nueva";
        }else if(!actual.equals("1")){
            return "La contraseña actual es incorrecta";
        }else if(!(nueva.length()>=8)){
            return "La contraseña debe contener como minimo 8 caracteres";
        }else if(!revisarMayMinNum(nueva)){
            return "La contraseña debe contener números, letras en mayúscula y minúscula";
        }else if(nueva.equals(actual)){
            return "La contraseña  nueva debe ser diferente a la actual";
        }else if(!nueva.equals(confirmacion)){
            return "La confirmación de contraseña no coincide con la clave ingresada";
        }
        //consumir servicio para cambiar clave


        return msj;
    }

    //este metodo es para validar que la clave contenga numeros, letras minus y mayus
    public boolean revisarMayMinNum( String password){
        String msj="";

        char clave;

        byte  contNumero = 0;
        byte contLetraMay = 0;
        byte contLetraMin=0;

        for (byte i = 0; i < password.length(); i++) {

            clave = password.charAt(i);

            String passValue = String.valueOf(clave);

            if (passValue.matches("[A-Z]")) {

                contLetraMay++;
            } else if (passValue.matches("[a-z]")) {

                contLetraMin++;
            } else if (passValue.matches("[0-9]")) {

                contNumero++;
            }
        }
        if(contLetraMay>0 && contNumero>2 && contLetraMin>0){
            return true;
        }


        return false;
    }


}
