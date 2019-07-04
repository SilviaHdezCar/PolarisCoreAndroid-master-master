package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEvidenciasAutorizadas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTipificacionesAutorizadas;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.Dialogs.DialogMostarFoto;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;
import static java.util.Collections.*;

public class TipificacionesAutorizadas extends Fragment {


    private View v;

    private ImageView img_estado;
    private TextView serial;
    private TextView marca;
    private TextView modelo;
    private TextView tecnologia;
    private TextView estado;
    private TextView fechaANS;

    private ImageView btn_siguiente;
    private ImageView btn_atras;
    private RecyclerView rv;/*
    private RecyclerView rvFotos;
    private RecyclerView rvRepuestos;*/
    private TableLayout tablaRepuestos;
    private LinearLayout layout_evidencias;
    private LinearLayout layout_repuestos;

    private ImageView img_evidencia1;
    private ImageView img_evidencia2;
    private TextView txt_nomFoto1;
    private TextView txt_nomFoto2;
    private TextView txt_fechaFoto1;
    private TextView txt_fechaFoto2;
    FragmentManager fragmentManager;


    public static ArrayList<Observacion> list_con_fotos;

    private List<Repuesto> repuestos;
    private List<Tipificacion> tipificacionesRecibidas;
    private boolean tieneRepuestos;

    public TipificacionesAutorizadas() {
        // Required empty public constructor
    }

    // Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/view/"+Global.ID+".jpg").error(R.mipmap.ic_profile).fit().centerInside().into(imageView);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tipificaciones_autorizadas, container, false);
        objeto.setTitulo("TIPIFICACIONES");

        this.tieneRepuestos = false;
        list_con_fotos = new ArrayList<Observacion>();
        this.repuestos = new ArrayList<Repuesto>();
        ImageView imagen = (ImageView) v.findViewById(R.id.imagen_asociada);
        Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewModel/" + Global.terminalVisualizar.getTerm_model().toUpperCase() + ".jpg").error(R.drawable.img_no_disponible).fit().centerInside().into(imagen);
        img_estado = (ImageView) v.findViewById(R.id.imagen_estado_val);
        serial = (TextView) v.findViewById(R.id.serial_ter_autorizada);
        marca = (TextView) v.findViewById(R.id.marca_ter_autorizada);
        modelo = (TextView) v.findViewById(R.id.modelo_ter_autorizada);
        tecnologia = (TextView) v.findViewById(R.id.tecno_ter_autorizada);
        estado = (TextView) v.findViewById(R.id.estado_ter_autorizada);
        fechaANS = (TextView) v.findViewById(R.id.fechaANS_ter_autorizada);
        btn_siguiente = (ImageView) v.findViewById(R.id.btn_siguiente_tipificaciones_autorizadas);
        btn_atras = (ImageView) v.findViewById(R.id.btn_atras_tipificaciones);
        rv = (RecyclerView) v.findViewById(R.id.recycler_view_tipificaciones_autorizadas);
        layout_evidencias = (LinearLayout) v.findViewById(R.id.layout_evidencias);
        layout_repuestos = (LinearLayout) v.findViewById(R.id.layout_repuestos);
        tablaRepuestos = (TableLayout) v.findViewById(R.id.tabla_seleccionar_repuestos);


        img_evidencia1 = (ImageView) v.findViewById(R.id.img_evidencia1);
        img_evidencia2 = (ImageView) v.findViewById(R.id.img_evidencia2);
        txt_nomFoto1 = (TextView) v.findViewById(R.id.txt_nomFoto1);
        txt_nomFoto2 = (TextView) v.findViewById(R.id.txt_nomFoto2);
        txt_fechaFoto1 = (TextView) v.findViewById(R.id.txt_fechaFoto1);
        txt_fechaFoto2 = (TextView) v.findViewById(R.id.txt_fechaFoto2);


        layout_repuestos.setVisibility(View.VISIBLE);
        layout_evidencias.setVisibility(View.VISIBLE);


        asignarImagenEstado(Global.terminalVisualizar.getTerm_status());
        serial.setText(Global.terminalVisualizar.getTerm_serial());
        marca.setText(Global.terminalVisualizar.getTerm_brand());
        modelo.setText(Global.terminalVisualizar.getTerm_model());
        tecnologia.setText(Global.terminalVisualizar.getTerm_technology());
        estado.setText(Global.terminalVisualizar.getTerm_status());
        fechaANS.setText(Utils.darFormatoFechaObservaciones(Global.terminalVisualizar.getTerm_date_register()));

        recorrerTipificaciones();
        llenarListaRepuestos();

        tieneRepuestos = tieneRepuestos();

        if (!tieneRepuestos && Global.observaciones_con_fotos.size() == 0) {
            Toast.makeText(objeto, "La terminal no tiene repuestos ni evidencias", Toast.LENGTH_SHORT).show();
            layout_evidencias.setVisibility(View.GONE);
            layout_repuestos.setVisibility(View.GONE);
        } else if (Global.observaciones_con_fotos != null && Global.observaciones_con_fotos.size() >= 2) {//NO Tiene repuestos
            if (!tieneRepuestos) {
                layout_repuestos.setVisibility(View.GONE);
            } else {
                layout_repuestos.setVisibility(View.VISIBLE);
                layout_evidencias.setVisibility(View.VISIBLE);

                sort(Global.observaciones_con_fotos);
                System.out.println("tamaño ob fotos: " + Global.observaciones_con_fotos.size());

                Observacion obFoto1 = Global.observaciones_con_fotos.get(Global.observaciones_con_fotos.size() - 1);
                System.out.println("obFoto1 " + obFoto1.toString());
                Observacion obFoto2 = Global.observaciones_con_fotos.get(Global.observaciones_con_fotos.size() - 2);
                System.out.println("obFoto2 " + obFoto2.toString());
                final String foto1 = obFoto1.getTeob_photo();
                final String foto2 = obFoto2.getTeob_photo();

                txt_nomFoto1.setText(foto1);
                txt_nomFoto2.setText(foto2);

                txt_fechaFoto1.setText(Utils.darFormatoFechaObservaciones(obFoto1.getTeob_fecha()));
                txt_fechaFoto2.setText(Utils.darFormatoFechaObservaciones(obFoto2.getTeob_fecha()));


                Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + foto1).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia1);
                Picasso.with(objeto).load("http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + foto2).error(R.drawable.img_no_disponible).fit().centerInside().into(img_evidencia2);
                Global.foto = 1;
                Global.rutaFotoObservacion = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + foto1;
                img_evidencia1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inflar fragment evidencias y carga la foto

                        cargarPanel();

                    }
                });
                Global.foto = 2;
                Global.rutaFotoObservacion2 = "http://100.25.214.91:3000/PolarisCore/upload/viewObservation/" + foto2;
                img_evidencia2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inflar fragment evidencias y carga la foto

                        cargarPanel();

                    }
                });
            }


        }
        btn_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new EtapasTerminalAutorizada()).addToBackStack(null).commit();
                try {
                    android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tieneRepuestos) {
                    Global.REPUESTOS_CAMBIAR_ESTADO_DANADO = repuestos;
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                    return;
                } else {
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new ValidacionesSeleccionarAutorizadas()).addToBackStack(null).commit();
                    return;
                }
            }
        });


        return v;
    }

    private void asignarImagenEstado(String estado) {
        if (estado.equalsIgnoreCase("ALISTAMIENTO")) {
            img_estado.setImageResource(R.mipmap.estado_alistamiento);
        } else if (estado.equalsIgnoreCase("COTIZACIÓN")) {
            img_estado.setImageResource(R.mipmap.estado_cotizacion);
        } else if (estado.equalsIgnoreCase("DADO DE BAJA")) {
            img_estado.setImageResource(R.mipmap.estado_dado_baja);
        } else if (estado.equalsIgnoreCase("DIAGNÓSTICO")) {
            img_estado.setImageResource(R.mipmap.estado_diagnostico);
        } else if (estado.equalsIgnoreCase("GARANTÍA")) {
            img_estado.setImageResource(R.mipmap.estado_garantia);
        } else if (estado.equalsIgnoreCase("NUEVO")) {
            img_estado.setImageResource(R.mipmap.estado_nuevo);
        } else if (estado.equalsIgnoreCase("PREDIAGNÓSTICO")) {
            img_estado.setImageResource(R.mipmap.estado_prediagnostico);
        } else if (estado.equalsIgnoreCase("QA")) {
            img_estado.setImageResource(R.mipmap.estado_qa);
        } else if (estado.equalsIgnoreCase("REPARACIÓN")) {
            img_estado.setImageResource(R.mipmap.estado_reparacion);
        } else if (estado.equalsIgnoreCase("TRANSITO")) {
            img_estado.setImageResource(R.mipmap.estado_transito);
        }
    }

    public void cargarPanel() {
        DialogMostarFoto dialog = new DialogMostarFoto();
        dialog.show(objeto.getSupportFragmentManager(), "");
    }

    public boolean buscarArreglo(String nomFoto) {
        if (Global.fotos != null && Global.fotos.size() > 0) {
            for (Observacion o : Global.fotos) {
                if (nomFoto.equals(o.getTeob_photo())) {
                    return true;
                }
            }
        }
        return false;
    }


    /*

     */
    public boolean tieneRepuestos() {
        if (this.repuestos != null) {
            if (this.repuestos.size() > 0) {
                llenarTabla();
                layout_evidencias.setVisibility(View.GONE);
                return true;
            }
        }
        return false;
    }


    /**
     * Metodo utilizado para llenar la tabla de respuestos con la columna OK
     **/
    public void llenarTabla() {

        for (int i = 0; i < this.repuestos.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            // fila.setGravity(View.TEXT_ALIGNMENT_CENTER);
            fila.setBackgroundResource(R.drawable.borde_inferior_gris);
            fila.setGravity(Gravity.CENTER_VERTICAL);

            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(100 + i);
            nombre.setText(this.repuestos.get(i).getSpar_name());
            nombre.setPadding(20, 20, 0, 20);
            nombre.setGravity(Gravity.CENTER);

            fila.addView(nombre);
            tablaRepuestos.addView(fila);
        }
    }


    /**
     * Este metodo se utiliza para recorrer el arreglo de repuestos enviado por el servicio al seleccionar una autorizada
     * Split de los repuestos recibidos y los agrega a la tabla
     **/
    public void llenarListaRepuestos() {
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS = null;
        Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS = new ArrayList<Repuesto>();

        if (Global.repuestos_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()) != null
                && !Global.repuestos_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).equalsIgnoreCase("")) {
            String reps[] = Global.repuestos_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");
            repuestos = new ArrayList<>();
            if (reps == null || reps.length == 0 || reps.equals("")) {
                Toast.makeText(objeto, "No tiene repuestos", Toast.LENGTH_SHORT).show();
            } else {
                if (!reps[0].equals("[]") || !reps[0].trim().isEmpty()) {
                    Repuesto repuesto = null;
                    String[] rep = null;
                    for (int i = 0; i < reps.length; i++) {
                        rep = reps[i].split("-");
                        System.out.println();
                        if (rep != null && rep.length > 0) {
                            repuesto = new Repuesto(rep[0], rep[1], rep[2], rep[3]);
                            this.repuestos.add(repuesto);
                            Global.REPUESTOS_DEFECTUOSOS_AUTORIZADAS.add(repuesto);
                        }


                    }

                }
            }
        }

    }


    public void recorrerTipificaciones() {

        String tipificaciones[] = Global.tipificaciones_listar_autorizadas.get(Global.terminalVisualizar.getTerm_serial()).split(",");
        if (!tipificaciones[0].equals("[]")) {

            ArrayList<String> tipificacions = new ArrayList<>();

            if ((tipificaciones.length == 0) || tipificaciones == null) {
                Toast.makeText(objeto, "No tiene tipificaciones", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < tipificaciones.length; i++) {
                    tipificacions.add(tipificaciones[i]);
                }
                llenarRVTipificaciones(tipificacions);
            }

        }
    }

    /**
     * este metodo llena el recycler view con las tipificaciones obtenidas al consumir el
     * servicio que muestra el detalle de la terminal autorizada seleccionada
     **/
    public void llenarRVTipificaciones(List<String> tipificacionesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        if (tipificacionesRecibidas == null || tipificacionesRecibidas.size() == 0) {
            Toast.makeText(objeto, "No tiene tificaciones", Toast.LENGTH_SHORT).show();
        }
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        final ArrayList tipificaciones = new ArrayList<>();

        for (String val : tipificacionesRecibidas) {
            if (val != null) {
                tipificaciones.add(val);
            }

            final AdapterTipificacionesAutorizadas adapter = new AdapterTipificacionesAutorizadas(tipificaciones, null, R.layout.panel_tipificaciones_autorizadas);

            rv.setAdapter(adapter);

        }

    }
}
