package com.example.wposs_user.polariscoreandroid.java; /**
 * Created by Ertzil on 05/03/2018.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogGuardarCredenciales;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogHuella;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ConsultaTerminalesSerial;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.Activity_login.objeto_login;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private String resultado;
    private boolean huella;


    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
        this.resultado = "";
        this.huella = false;

    }

    public FingerprintHandler() {
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Error de Autenticación de huellas dactilares\n" + errString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("No se reconoce la huella digital", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Éxito al autenticar con la huella dactilar.", true);
    }


    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isHuella() {
        return huella;
    }

    public void setHuella(boolean huella) {
        this.huella = huella;
    }

    public void update(String e, Boolean success) {

        DialogHuella dialogo = objeto_login.getDialogo();

        this.huella = success;
        this.resultado = e;
         if(huella){

            dialogo.dismiss();

            objeto_login.validarHuella();

            return;
        }else{
            Toast.makeText(this.context, e, Toast.LENGTH_SHORT).show();
            dialogo.dismiss();
        }

    }


}

