package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ObservacionesFragment extends Fragment {
    private View v;
    private TextView btn_tomar_foto;
    private TextView btn_tomar_foto2;
    private TextView txt_observacion;
    private ImageView imagen_observación;
    private ImageView imagen_observación2;
    private Button finalizar;

    Bitmap bitmap_foto1;
    Bitmap bitmap_foto2;
    private RequestQueue queue;
    private StringRequest stringRequest;
    private int foto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_observaciones, container, false);
        objeto.setTitle("OBSERVACIÓN");

        imagen_observación = (ImageView) v.findViewById(R.id.imagen_observación);
        imagen_observación2 = (ImageView) v.findViewById(R.id.imagen_observación2);
        queue = Volley.newRequestQueue(objeto);
        finalizar = (Button) v.findViewById(R.id.btn_finalizar_observacion);

        txt_observacion = (TextView) v.findViewById(R.id.txt_observacion_fin);
        btn_tomar_foto = (TextView) v.findViewById(R.id.lbl_cargarFoto);
        btn_tomar_foto2 = (TextView) v.findViewById(R.id.lbl_cargarFoto2);

        btn_tomar_foto.setPaintFlags(btn_tomar_foto.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // nomFoto = Global.ID + ".jpg";
        bitmap_foto1 = null;
        bitmap_foto2 = null;

        foto = 0;

        btn_tomar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 1;
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intento1, 0);
            }
        });

        btn_tomar_foto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foto = 2;
                Intent intento1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intento1, 0);
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

/**
 * Este método se utiliza para cargar subir las fotos
 * **/
    public void enviarFoto() {
        String url = "http://100.25.214.91:3000/PolarisCore/upload/uploadImgObservations/";
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Global.STATUS_SERVICE = response.get("status").toString();
                    System.out.println("status:  " + Global.STATUS_SERVICE);

                    if (Global.STATUS_SERVICE.equalsIgnoreCase("fail")) {
                        Global.mensaje = response.get("message").toString();
                        Toast.makeText(objeto, "Error al registrar las fotos", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        Toast.makeText(objeto, "Fotos cargadas correctamente", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(objeto, "ERROR\n " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               // String nom_foto1 = Global.serial_ter + "_" + Global.ID + "_1.jpg";
                //String nom_foto2 = Global.serial_ter + "_" + Global.ID + "_2.jpg";

                String img1 = convertirImagenString(bitmap_foto1);
                String img2 = convertirImagenString(bitmap_foto2);
                Map<String, String> parametros = new HashMap<>();
              //  parametros.put("nom_foto1", nom_foto1);
                parametros.put("identificacion", Global.ID);
                parametros.put("serial", Global.serial_ter);
                parametros.put("firstPhoto", img1);
              //  parametros.put("nom_foto2", nom_foto2);
                parametros.put("secondPhoto", img2);
                return parametros;
            }
        };
        queue.add(jsArrayRequest);
    }


    /**
     *este método se utiliza pára convertir la imagen a String, para ser enviada al servisor
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
        if (foto == 1) {
            bitmap_foto1 = (Bitmap) data.getExtras().get("data");
            imagen_observación.setImageBitmap(bitmap_foto1);
        } else if (foto == 2) {
            bitmap_foto2 = (Bitmap) data.getExtras().get("data");
            imagen_observación2.setImageBitmap(bitmap_foto2);
        }


    }

    //Metodo utilizado para finalizar el diagnostico (Consume el servicio de finalizar diagnostico)
    public void finalizarDiagnostico() {
        String observaciones = txt_observacion.getText().toString();

        if (observaciones.isEmpty()) {
            Toast.makeText(objeto, "Por favor ingrese la observación", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bitmap_foto1 == null) {
            Toast.makeText(objeto, "Por favor tome la primera foto", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bitmap_foto2 == null) {
            Toast.makeText(objeto, "Por favor tome la segunda foto", Toast.LENGTH_SHORT).show();
            return;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
            alertDialog.setTitle("INFORMACIÓN");
            alertDialog.setMessage("El diagnóstico fue creado correctamente");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ACEPTAR",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).addToBackStack(null).commit();
                        }
                    });
            alertDialog.show();
        }
    }


}
