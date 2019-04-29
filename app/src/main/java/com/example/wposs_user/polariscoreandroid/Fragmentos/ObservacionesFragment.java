package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.R;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;

public class ObservacionesFragment extends Fragment {
    private View v;
    private TextView lbl_cargarFoto;
    private ImageView imagen_observación;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_observaciones, container, false);

        lbl_cargarFoto=(TextView)v.findViewById(R.id.lbl_cargarFoto);
        lbl_cargarFoto=(TextView)v.findViewById(R.id.lbl_cargarFoto);
        lbl_cargarFoto.setPaintFlags(lbl_cargarFoto.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        lbl_cargarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subirFoto();
            }
        });


        return v;

    }


    private void subirFoto(){
        Toast.makeText(objeto, "DIO CLIC CARGAR FOTO", Toast.LENGTH_SHORT).show();
    }

}
