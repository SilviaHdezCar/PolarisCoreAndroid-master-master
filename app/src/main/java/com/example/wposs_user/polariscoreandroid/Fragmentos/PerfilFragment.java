package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class PerfilFragment extends Fragment {
    //PERFIL USUARIO
    private ImageView imageView;
    private TextView nomUsuario;
    private TextView usuario;
    private TextView cargo;
    private TextView telefono;
    private TextView correo;
    private TextView ubicacion;
    private Button btn_cambiar_clave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_perfil, container, false);

        objeto.setTitle("DATOS DEL USUARIO");
        nomUsuario = (TextView) v.findViewById(R.id.perfil_nombre_usuario);
        usuario = (TextView) v.findViewById(R.id.perfil_usuario);
        cargo = (TextView) v.findViewById(R.id.perfil_cargo);
        telefono = (TextView) v.findViewById(R.id.perfil_telefono);
        correo = (TextView) v.findViewById(R.id.perfil_correo);
        ubicacion = (TextView) v.findViewById(R.id.perfil_ubicacion);
        imageView = (ImageView) v.findViewById(R.id.perfil_imagen_usuario);
        btn_cambiar_clave = (Button) v.findViewById(R.id.btn_cambiar_clave);

          //extraemos el drawable en un bitmap
        Drawable originalDrawable = getResources().getDrawable(R.mipmap.user_grey);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());



        imageView.setImageDrawable(roundedDrawable.getCurrent());

        nomUsuario.setText(Global.NOMBRE);
        usuario.setText(Global.CODE);
        cargo.setText(Global.POSITION);
        telefono.setText(Global.PHONE);
        correo.setText(Global.EMAIL);
        ubicacion.setText(Global.LOCATION);

        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/view/"+Global.ID+".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView);

        btn_cambiar_clave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ActualizarClave_perfil()).addToBackStack(null).commit();
            }
        });

        return v;

    }


}
