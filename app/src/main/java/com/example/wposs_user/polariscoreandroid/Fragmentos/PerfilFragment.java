package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class PerfilFragment extends Fragment {
    //PERFIL USUARIO
    private ImageView imgPerfil;
    private TextView nomUsuario;
    private TextView usuario;
    private TextView cargo;
    private TextView telefono;
    private TextView correo;
    private TextView ubicacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomUsuario = (TextView) v.findViewById(R.id.perfil_nombre_usuario);
        usuario = (TextView) v.findViewById(R.id.perfil_usuario);
        cargo = (TextView) v.findViewById(R.id.perfil_cargo);
        telefono = (TextView) v.findViewById(R.id.perfil_telefono);
        correo = (TextView) v.findViewById(R.id.perfil_correo);
        ubicacion = (TextView) v.findViewById(R.id.perfil_ubicacion);

        nomUsuario.setText(Global.NOMBRE);
        usuario.setText(Global.CODE);
        cargo.setText(Global.POSITION);
        telefono.setText(Global.PHONE);
        correo.setText(Global.EMAIL);
        ubicacion.setText(Global.LOCATION);



        return v;

    }


}
