package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogLoading;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogMostarFoto;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ObservacionesFragment extends Fragment {
    private View v;
    private TextView lbl_nomFoto;
    private TextView lbl_nomFoto2;
    private TextView txt_observacion;
    private ImageView imagen_observación;
    private ImageView imagen_observación2;
    private Button finalizar;

    private String nombre_foto1;
    private String nombre_foto2;
    private Bitmap bitmap_foto1;
    private Bitmap bitmap_foto2;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private int foto;


    private Observacion obser;


    private String observacion_text;
    private String nomFotos;

    //tomar foto
    private static final int PICTURE_RESULT = 122;
    private ContentValues values;
    private Uri imageUri;
    private String imageurl;
    private String mensaje;


    private boolean actualizar;
    private JSONArray jsonArrayHistorial;

    private DialogLoading dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_observaciones, container, false);
        //  objeto.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        objeto.setTitulo("OBSERVACIÓN");

        permisos();

        imagen_observación = (ImageView) v.findViewById(R.id.imagen_observacion);
        imagen_observación2 = (ImageView) v.findViewById(R.id.imagen_observacion2);
        queue = Volley.newRequestQueue(objeto);
        finalizar = (Button) v.findViewById(R.id.btn_finalizar_observacion);

        //layout_finalizar_diagnostico.setVisibility(View.INVISIBLE);

        txt_observacion = (TextView) v.findViewById(R.id.txt_observacion_fin);
        lbl_nomFoto = (TextView) v.findViewById(R.id.lbl_nomFoto);
        lbl_nomFoto2 = (TextView) v.findViewById(R.id.lbl_nomFoto2);

        bitmap_foto1 = null;
        bitmap_foto2 = null;

        foto = 0;

        imagen_observación.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 1;
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "PolarisCoreLab");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                imageUri = objeto.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);
            }
        });

        imagen_observación2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 2;
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "PolarisCoreLab");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                imageUri = objeto.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalizarDiagnostico();
            }
        });


        return v;

    }

    //validar los permisos para acceder SD
    public boolean permisos() {
        boolean retorno = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Verifica permisos para Android 6.0+
            if (!checkExternalStoragePermission()) {
                Toast.makeText(objeto, "No tiene permiso para acceder archivos", Toast.LENGTH_SHORT).show();
                retorno = false;
            }
        }
        return retorno;
    }

    //pERMISOS PARA ACCEDER A LA MEMORIA EXTERNA
    private boolean checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                objeto, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(objeto, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer!");
            return true;
        }

        return false;
    }


    /**
     * Este método se utiliza para cargar subir las fotos y activa el boton para finalizar el diagnostico
     **/
    public void consumirServicioEnviarFotos() {
        finalizar.setEnabled(false);
        String url = "http://100.25.214.91:3000/PolarisCore/upload/uploadImgObservations/";
        JSONObject jsonObject = new JSONObject();

        String img1 = convertirImagenString(bitmap_foto1);
        String img2 = convertirImagenString(bitmap_foto2);

        try {
            jsonObject.put("identificacion", Global.ID);
            jsonObject.put("serial", Global.serial_ter);
            jsonObject.put("firstPhoto", img1);
            jsonObject.put("secondPhoto", img2);
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
                            System.out.println("RESPUESTA FOTOS-->" + response.get("message").toString());
                            if (!response.get("status").toString().equalsIgnoreCase("ok")) {
                                dialog.dismiss();
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "ERROR: " + mensaje, Toast.LENGTH_SHORT).show();
                                finalizar.setEnabled(true);
                                return;
                            } else {

                                response = new JSONObject(response.get("data").toString());

                                nombre_foto1 = response.get("image1").toString();
                                System.out.println("nombre_foto1 " + nombre_foto1);
                                nombre_foto2 = response.get("image2").toString();

                                System.out.println("nombre_foto2 " + nombre_foto2);
                                nomFotos = nombre_foto1 + "/" + nombre_foto2;
                                System.out.println("nombre de las fotos" + nomFotos);
                                obser = new Observacion(observacion_text, Global.serial_ter, nomFotos);
                                consumirServicioObtenerHistorial();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    if (!error.getMessage().isEmpty()) {
                        Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
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


    /**
     * este método se utiliza pára convertir la imagen a String, para ser enviada al servisor
     **/
    public String convertirImagenString(Bitmap bitmap) {
        String img = "";
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imageByte = array.toByteArray();
        img = Base64.encodeToString(imageByte, Base64.DEFAULT);

        return img;
    }


    //este metodo se utiliza para obetenr la foto tomada
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
*  values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "PolarisCoreLab");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                imageUri = objeto.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);*/
        switch (requestCode) {
            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            if (foto == 1) {
                                bitmap_foto1 = MediaStore.Images.Media.getBitmap(objeto.getContentResolver(), imageUri);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                bitmap_foto1 = Bitmap.createBitmap(bitmap_foto1, 0, 0, bitmap_foto1.getWidth(), bitmap_foto1.getHeight(), matrix, true);//para girar la imagen
                                imagen_observación.setImageBitmap(bitmap_foto1);
                                Global.bitmap = bitmap_foto1;

                                //Obtiene la ruta donde se encuentra guardada la imagen.
                                imageurl = getRealPathFromURI(imageUri);
                            } else if (foto == 2) {
                                bitmap_foto2 = MediaStore.Images.Media.getBitmap(objeto.getContentResolver(), imageUri);
                                Global.bitmap = bitmap_foto2;
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                bitmap_foto2 = Bitmap.createBitmap(bitmap_foto2, 0, 0, bitmap_foto2.getWidth(), bitmap_foto2.getHeight(), matrix, true);
                                imagen_observación2.setImageBitmap(bitmap_foto2);
                                //Obtiene la ruta donde se encuentra guardada la imagen.
                                imageurl = getRealPathFromURI(imageUri);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = objeto.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Metodo utilizado para finalizar el diagnostico (Consume el servicio de finalizar diagnostico)
    public void finalizarDiagnostico() {
        observacion_text = txt_observacion.getText().toString();
        dialogLoading();
        if (bitmap_foto1 == null) {
            Toast.makeText(objeto, "Por favor tome la primera foto", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if (bitmap_foto2 == null) {
            Toast.makeText(objeto, "Por favor tome la segunda foto", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if (observacion_text.isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        consumirServicioEnviarFotos();
    }

    public void dialogLoading() {
        dialog = new DialogLoading();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }


    /**
     * Metodo utilizados para consumir el servicio  que permite obtener el historial de la terminal
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioObtenerHistorial() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/getHistoryTerminal";
        System.out.println("llegó a consumir servicio que obtiene el historial");
        jsonArrayHistorial = new JSONArray();
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPUESTA OB His", response.toString());
                            if (!response.get("message").toString().equals("success")) {
                                dialog.dismiss();
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                response = response.getJSONObject("data");
                                // jsonObject=response.getJSONArray("tehi_historial");
                                System.out.println("response-->" + response.toString());
                                String tehi_historial = response.getString("tehi_historial");
                                System.out.println("tehi_historial-->" + tehi_historial);
                                if (!tehi_historial.equals("")) {
                                    jsonArrayHistorial = new JSONArray(tehi_historial);
                                    System.out.println("jsonArray--->" + jsonArrayHistorial.length());
                                    if (jsonArrayHistorial != null && jsonArrayHistorial.length() > 0) {
                                        evaluarJsonArray();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authenticator", Global.TOKEN);
                params.put("Id", Global.serial_ter);

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     * Este metodo se utiliza para revisar la respuesta obtenida luego de consumir el servicio en
     * le que se obtiene el historial de gestionadas de la terminal
     */
    public void evaluarJsonArray() {
        try {
            if (jsonArrayHistorial.length() == 1) {
                consumirServicioSumarGestion();

            } else {
                JSONObject jsonObject = jsonArrayHistorial.getJSONObject(jsonArrayHistorial.length() - 2);
                String estado = jsonObject.get("term_state").toString();
                String tecnico = jsonObject.get("term_location").toString();
                if ((estado.equalsIgnoreCase("COTIZACIÓN")
                        || estado.equalsIgnoreCase("GARANTÍA")
                        || estado.equalsIgnoreCase("DIAGNÓSTICO"))
                        && tecnico.equalsIgnoreCase(Global.CODE)) {
                    consumirServicioActualizarGestionadas();
                } else {
                    consumirServicioSumarGestion();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * Metodo utilizados para consumir el servicio  que permite incrementar las terminales
     * gwartionadas por el técnico
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioSumarGestion() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/incrementarGestionadas";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", Global.CODE);
            jsonObject.put("tipo", "DIAGNÓSTICO");
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
                            Log.d("RESPUESTA SUMAR", response.toString());
                            if (!response.get("status").toString().equals("ok")) {
                                dialog.dismiss();
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                consumirServicioActualizarGestionadas();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authenticator", Global.TOKEN);

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     * Metodo utilizados para consumir el servicio  que permite actualizar las terminales gestionadas
     * gwartionadas por el técnico
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioActualizarGestionadas() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/updateHistoryTerminal";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("terminal_history", this.JObjectActualizarHistorial());
            jsonObject.put("tehi_serial", Global.serial_ter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPUESTA ACTUALIZAR", response.toString());
                            if (!response.get("status").toString().equals("ok")) {
                                dialog.dismiss();
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                System.out.println("consumirServicioActualizarGestionadas()-->ok");
                                consumirServicioDiagnostico();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authenticator", Global.TOKEN);
                params.put("iD", Global.serial_ter);

                return params;
            }
        };
        queue.add(jsArrayRequest);
    }

    public JSONObject JObjectActualizarHistorial() {
        JSONObject jsonObject = new JSONObject();
        try {
            Date date = new Date();
            jsonObject.put("term_location", Global.CODE);
            jsonObject.put("term_state", "DIAGNÓSTICO");
            jsonObject.put("date", date + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * Metodo utilizados para consumir el servicio  que permite registrar un diagnostico con observaciones mediante una petición REST
     * En el encabezado va el token-> Authenticator
     **/
    public void consumirServicioDiagnostico() {
        String url = "http://100.25.214.91:3000/PolarisCore/Terminals/savediagnosis";

        JSONObject jsonObject = new JSONObject();
        JSONObject obj2 = new JSONObject();
        try {

            JSONArray val = this.getValidaciones();
            jsonObject.put("validaciones", val);
            JSONArray tip = this.getTipificaciones();
            jsonObject.put("tipificaciones", tip);
            jsonObject.put("reparable", Global.reparable);
            jsonObject.put("observacion", obser.getObjRep());
            jsonObject.put("falla", "");
            obj2.put("tesw_serial", Global.serial_ter);
            JSONArray o = this.getRepuestos();
            obj2.put("tesw_repuestos", o);
            jsonObject.put("repuestos", obj2);


            Log.d("RESPUESTA", jsonObject.toString());


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
                            System.out.println("STATUS-->" + response.get("status").toString());
                            if (response.get("status").toString().equals("fail")) {
                                dialog.dismiss();
                                mensaje = response.get("message").toString();
                                if (mensaje.equalsIgnoreCase("token no valido")) {
                                    Toast.makeText(objeto, "Su sesión ha expirado, debe iniciar sesión nuevamente", Toast.LENGTH_SHORT).show();
                                    objeto.consumirSercivioCerrarSesion();
                                    return;
                                }
                                Toast.makeText(objeto, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                System.out.println("registrar diagnistico--->ok");
                                dialog.dismiss();
                                eliminarPila();
                                objeto.CustomAlertDialog(objeto, "Información", "Diagnóstico registrado exitosamente", 3000, false);
                                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("RESPUESTA REG OBs", response.toString());
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getMessage() != null) {
                            if (!error.getMessage().isEmpty()) {
                                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
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

    public void eliminarPila() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

    }

    /**
     * METODO QUE RECORRE LA LISTA DE VALIDACIONES Y LAS AGREGA AL ARREGLO
     **/
    public JSONArray getValidaciones() throws JSONException {

        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.VALIDACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.VALIDACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getTipificaciones() throws JSONException {

        JSONArray listas = new JSONArray();
        for (int i = 0; i < Global.TIPIFICACIONES_DIAGNOSTICO.size(); i++) {
            JSONObject ob = Global.TIPIFICACIONES_DIAGNOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

    public JSONArray getRepuestos() throws JSONException {
        Global.REPUESTOS_DIAGONOSTICO = null;
        Global.REPUESTOS_DIAGONOSTICO = new ArrayList<Repuesto>();
        JSONArray listas = new JSONArray();

        for (int i = 0; i < Global.REPUESTOS_DIAGONOSTICO.size(); i++) {
            JSONObject ob = Global.REPUESTOS_DIAGONOSTICO.get(i).getObj();
            listas.put(ob);
        }

        return listas;
    }

}
