package com.example.wposs_user.polariscoreandroid.Actividades;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogHuella;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.java.FingerprintHandler;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreference;



@TargetApi(Build.VERSION_CODES.M)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Activity_login extends AppCompatActivity {

    private EditText txtCorreo;
    private EditText txtPass;
    private RequestQueue queue;
    private String correo;
    private String pass;
    private ImageButton verClave;
    private TextView recuperarClave;
    ImageButton huella;

    //variables de la huella
    private static final String KEY_NAME = "pruebaHuella";

    private KeyStore keyStore;
    private Cipher cipher;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtPass = (EditText) findViewById(R.id.txtPass);
        StringBuilder str = new StringBuilder();
        queue = Volley.newRequestQueue(Activity_login.this);
        verClave = (ImageButton) findViewById(R.id.btn_mostrarClave);
        recuperarClave = (TextView) findViewById(R.id.txt_reestablecerClave);
        huella=(ImageButton)findViewById(R.id.btn_huella) ;

        huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHuella();
            }
        });

        recuperarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i;
                i = new Intent(Activity_login.this, Reestablecer_password .class);
                startActivity(i);
                finish();
            }
        });



    }




    /**
     * Realiza las validaciones correpondientes para iniciar sesión
     *
     * @param view
     */
    public void iniciarSesion(View view) {
        correo = this.txtCorreo.getText().toString();
        pass = this.txtPass.getText().toString();

        if (correo.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        if (correo.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
            return;
        } else {

            consumirServicoLogin();


        }

    }

    /**
     * mETODO UTILIZADO PARA CONSUMIR EL SEVICIO QUE PERMITE INICIAR SESIÓN
     */
    public void consumirServicoLogin() {

        String url = "http://100.25.214.91:3000/PolarisCore/Users/login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", this.correo);
            jsonObject.put("user_password", this.pass);
            jsonObject.put("gethash", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            if (response.get("message").toString().equalsIgnoreCase("error")) {
                                try {
                                    Global.mensaje = response.get("description").toString();
                                    Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_SHORT).show();
                                    System.out.println("description " + response.get("description").toString());
                                    if (Global.mensaje.equalsIgnoreCase("Contraseña inválida")) {
                                        txtPass.setText("");
                                    } else {
                                        limpiarLogin();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return;

                            } else {
                                try {
                                    Global.TOKEN = response.get("token").toString();
                                    // Global.MESSAGE = lineastrama[1].substring(11, lineastrama[1].length() - 1);
                                    Global.ROL = response.get("roles").toString();
                                    Global.LOGIN = response.get("login").toString();
                                    Global.ID = response.get("id").toString();//CEDULA
                                    Global.STATUS = response.get("status").toString();
                                    Global.POSITION = response.get("position").toString();
                                    Global.CODE = response.get("code").toString();
                                    Global.NOMBRE = response.get("name").toString();
                                    Global.EMAIL = response.get("email").toString();
                                    Global.LOCATION = response.get("location").toString();
                                    Global.PHONE = response.get("phone").toString();
                                    // Global.PHOTO = jsonObject.get("photo").toString();;


                                    Log.i("------------STATUS: ", "" + Global.STATUS);
                                    Log.i("------------POSITION: ", "" + Global.POSITION);
                                    Log.i("------------TOKEN: ", "" + Global.TOKEN);
                                    Log.i("--------CODE: ", "" + Global.CODE);
                                    Log.i("-------NAME: ", "" + Global.NOMBRE);

                                    if (!Global.POSITION.equalsIgnoreCase("TÉCNICO")) {
                                        Global.mensaje = "El usuario no tiene permisos";
                                        Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_SHORT).show();
                                        limpiarLogin();
                                        cerrarSesion();
                                        return;
                                    } else if (Global.STATUS.equalsIgnoreCase("INACTIVO")) {
                                        Global.mensaje = "El usuario está inactivo";
                                        limpiarLogin();
                                        Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    guardarSesion();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (Integer.parseInt(Global.LOGIN) == 0) {
                                    Intent i = new Intent(Activity_login.this, Activity_UpdatePassword.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(Activity_login.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                return;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(Activity_login.this, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        queue.add(jsArrayRequest);
    }


    public void guardarSesion() {

        saveValueStrPreference(Activity_login.this, "token", Global.TOKEN);
        saveValueStrPreference(Activity_login.this, "rol", Global.ROL);
        saveValueStrPreference(Activity_login.this, "login", Global.LOGIN);
        saveValueStrPreference(Activity_login.this, "id", Global.ID);
        saveValueStrPreference(Activity_login.this, "status", Global.STATUS);
        saveValueStrPreference(Activity_login.this, "position", Global.POSITION);
        saveValueStrPreference(Activity_login.this, "code", Global.CODE);
        saveValueStrPreference(Activity_login.this, "nombre", Global.NOMBRE);
        saveValueStrPreference(Activity_login.this, "email", Global.EMAIL);
        saveValueStrPreference(Activity_login.this, "location", Global.LOCATION);
        saveValueStrPreference(Activity_login.this, "phone", Global.PHONE);
    }


    public void cerrarSesion() {
        String url = "http://100.25.214.91:3000/PolarisCore/Users/close";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user", Global.ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.get("status").toString().equalsIgnoreCase("fail")) {
                                try {
                                    Global.mensaje = response.get("message").toString();
                                    Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            eliminarValues(Activity_login.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA CERRAR SESIÓN", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "Error Respuesta en JSON: " + error.getMessage());
                        Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authenticator", Global.TOKEN);
                return params;
            }
        };

        queue.add(jsArrayRequest);
    }

    private void limpiarLogin() {
        this.txtCorreo.setText("");
        this.txtPass.setText("");
    }

    public void mostrarClave(View v){

     if(txtPass.getInputType()==129){

         txtPass.setInputType(1);

         return;
     }

        if(txtPass.getInputType()==1){

            txtPass.setInputType(129);

            return;
        }


    }



    /*******************************METODOS USADOS PARA LA HUELLA ***************************************/

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Error al obtener la instancia de KeyGenerator", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Error al obtener cifrado", e);
        }


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error al iniciar el cifrado", e);
        }
    }


    public void loginHuella(){
        //Inicializo las variables  para la huella

        DialogHuella dialog = new DialogHuella();
        dialog.show(Activity_login.this.getSupportFragmentManager(), "");





        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

         FingerprintManager fingerprintManager = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }


        // Check whether the device has a Fingerprint sensor.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */

                Toast.makeText(this,"su dipositivo no cuenta con un sensor de huellas",Toast.LENGTH_SHORT).show();
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"No tiene habilitados los permisos de autenticación con huellas dactilares",Toast.LENGTH_SHORT).show();

                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {

                                      Toast.makeText(this,"Debe registrar almenos una huella dactilar",Toast.LENGTH_SHORT).show();

                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            Toast.makeText(this,"No esta Habiliatada la seguridad por sensor de huellas ",Toast.LENGTH_SHORT).show();
                        } else {
                            generateKey();


                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }





    }




}
