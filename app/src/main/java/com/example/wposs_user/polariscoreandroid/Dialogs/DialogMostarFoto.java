package com.example.wposs_user.polariscoreandroid.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Actividades.Activity_UpdatePassword;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.wposs_user.polariscoreandroid.Actividades.Activity_login.objeto_login;
import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.eliminarValues;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreference;
import static com.example.wposs_user.polariscoreandroid.java.SharedPreferencesClass.saveValueStrPreferenceLogueoHuella;

public class DialogMostarFoto extends DialogFragment {

    private View view;
    private ImageView imagen;
    private PhotoViewAttacher photoView;
    private ImageButton btn_before;
    private ImageButton btn_next;
    private LinearLayout layout_desplazamiento;


//    private LinearLayout layout_informacion;
//    private LinearLayout layout_credenciales;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_evidencia_autorizada, null);
        imagen = (ImageView) view.findViewById(R.id.imagen_zoom);
        btn_before = (ImageButton) view.findViewById(R.id.btn_before);
        btn_next = (ImageButton) view.findViewById(R.id.btn_next);

        layout_desplazamiento = (LinearLayout) view.findViewById(R.id.layout_desplazamiento);

        setCancelable(true);

        if (Global.foto == 1) {
            Picasso.with(objeto).load(Global.rutaFotoObservacion).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
           // Picasso.with(objeto).load(Global.rutaFotoObservacion).error(R.drawable.img_no_disponible).fit().centerInside().into(imagen);
        } else if (Global.foto == 2) {
            Picasso.with(objeto).load(Global.rutaFotoObservacion2).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
           // Picasso.with(objeto).load(Global.rutaFotoObservacion2).error(R.drawable.img_no_disponible).fit().centerInside().into(imagen);
        }

        photoView = new PhotoViewAttacher(imagen);


        btn_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.foto == 1) {
                    Global.foto = 2;
                    Picasso.with(objeto).load(Global.rutaFotoObservacion2).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
                } else if (Global.foto == 2) {
                    Global.foto = 1;
                    Picasso.with(objeto).load(Global.rutaFotoObservacion).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.foto == 1) {
                    Global.foto = 2;
                    Picasso.with(objeto).load(Global.rutaFotoObservacion2).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
                } else if (Global.foto == 2) {
                    Global.foto = 1;
                    Picasso.with(objeto).load(Global.rutaFotoObservacion).error(R.drawable.img_no_disponible).fit().centerCrop().into(imagen);
                }
            }
        });

        layout_desplazamiento.setVisibility(View.VISIBLE);

        builder.setView(view);


        return builder.create();
    }

}



