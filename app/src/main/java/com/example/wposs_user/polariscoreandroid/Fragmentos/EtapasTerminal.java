package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapas;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.Actividades.MainActivity;
import com.example.wposs_user.polariscoreandroid.java.Terminal;

import java.util.Vector;


public class EtapasTerminal extends Fragment {
    public static MainActivity objeto;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    Vector<Etapas> etapas;
    Vector<Terminal> terminales;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_etapas_terminal, container, false);

       // agregarEtapasVector();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_etapas);
         recyclerView.setAdapter(new AdapterEtapas(getActivity(), etapas));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(getContext());// en forma de lista
        recyclerView.setLayoutManager(layoutManager);
        return view;

    }


 /*   private void agregarEtapasVector() {
        this.terminales =new Vector<>();
        Terminal t1 = new Terminal("678", "Gertec", "Newpos9220", "DIAL", "Asociada", null, null, 56, "Le está fallando algo");
        Terminal t2 = new Terminal("147", "Newposs", "9220", "DIAL", "Asociada", null, null, -3, "Cuidado, Le está fallando algo");
        Terminal t3 = new Terminal("342", "Gertec", "Newpos7220", "WIFI", "Asociada", null, null, 0, "Algo tiene mal");
        this.terminales.add(t1);
        this.terminales.add(t2);
        this.terminales.add(t3);


        this.etapas = new Vector<Etapas>();
        Etapas e1 = new Etapas(t1, null);
        Etapas e2 = new Etapas(t2, null);
        Etapas e3 = new Etapas(t3, null);

        etapas.add(e1);
        etapas.add(e2);
        etapas.add(e3);

        System.err.println("******************estapas creadas");
    }
*/

}
