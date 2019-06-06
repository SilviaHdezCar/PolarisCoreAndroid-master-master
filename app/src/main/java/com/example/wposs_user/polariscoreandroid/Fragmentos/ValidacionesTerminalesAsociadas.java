package com.example.wposs_user.polariscoreandroid.Fragmentos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.Comun.Tools;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Validacion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.wposs_user.polariscoreandroid.Actividades.MainActivity.objeto;


public class ValidacionesTerminalesAsociadas extends Fragment {//CREO QUE ACA SE DEBE LLENAR EL RCV


    private Fragment fragment;

    //   private Button siguiente;

    private TextView marca_ter_validaciones;
    private TextView modelo_ter_validaciones;
    private TextView serial_ter_validaciones;
    private TextView tecno_ter_validaciones;
    private TextView estado_ter_validaciones;
    private TextView garantia_ter_validaciones;
    private TextView fechal_ans_ter_validaciones;

    private TextView title_validaciones;
    private TableLayout tabla;
    private Button btn_siguiente;
    private String fechaActual;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_validaciones_terminales_asociadas, container, false);
        objeto.setTitulo("VALIDACIONES TERMINAL");
        fechaActual = Utils.getDate();

        marca_ter_validaciones = (TextView) v.findViewById(R.id.marca_ter_validaciones);
        modelo_ter_validaciones = (TextView) v.findViewById(R.id.modelo_ter_validaciones);
        serial_ter_validaciones = (TextView) v.findViewById(R.id.serial_ter_validaciones);
        tecno_ter_validaciones = (TextView) v.findViewById(R.id.tecno_ter_validaciones);
        estado_ter_validaciones = (TextView) v.findViewById(R.id.estado_ter_validaciones);
        garantia_ter_validaciones = (TextView) v.findViewById(R.id.garantia_ter_validaciones);
        fechal_ans_ter_validaciones = (TextView) v.findViewById(R.id.fechal_ans_ter_validaciones);
        Global.VALIDACIONES_DIAGNOSTICO = new ArrayList<>();


        title_validaciones = (TextView) v.findViewById(R.id.tile_validaciones);
        title_validaciones.setVisibility(View.GONE);


        tabla = (TableLayout) v.findViewById(R.id.tabla_validaciones_autorizadas);
        btn_siguiente = (Button) v.findViewById(R.id.btn_siguiente_seleccionar_validaciones_autorizadas);


        //voy a recorrer el arreglo de terminales para que me liste la informacion de la terminal selecciona
       /* Global.VALIDACIONES = null;
        Global.VALIDACIONES = new ArrayList<Validacion>();*/

        String fechaRecepción = "";
        String fechaANS = "";

        if (Global.diagnosticoTerminal.equals("asociada")) {
            modelo_ter_validaciones.setText(Global.modelo);
            marca_ter_validaciones.setText(Global.marca);
            serial_ter_validaciones.setText(Global.serial_ter);
            tecno_ter_validaciones.setText(Global.tecnologia);
            estado_ter_validaciones.setText(Global.estado);
            fechal_ans_ter_validaciones.setText("");
            garantia_ter_validaciones.setText("No establecida");
            if (Global.garantia != null) {
                if (!Global.garantia.trim().isEmpty()) {
                    int comp=compararFechas(Global.garantia);
                    System.out.println("garantía: "+comp);
                    if (comp>0) {
                        garantia_ter_validaciones.setText("Con garantía");
                    } else if(comp==-3){
                        garantia_ter_validaciones.setText("No establecida");
                    } else {
                        garantia_ter_validaciones.setText("Sin garantía");
                    }
                } else {

                }
            }

            if (Global.fechaRecepcion != null) {
                if (!Global.fechaRecepcion.trim().isEmpty()) {
                    fechaRecepción = Utils.darFormatoFecha2(Global.fechaRecepcion);
                    fechal_ans_ter_validaciones.setText(fechaRecepción);
                }
            }
            if (Global.fechaANS != null) {
                if (!Global.fechaANS.trim().isEmpty()) {
                    fechaANS = Utils.darFormatoFecha2(Global.fechaANS);
                    fechal_ans_ter_validaciones.setText(fechal_ans_ter_validaciones.getText().toString() + " - " + fechaANS);
                }


            }
        } else if (Global.diagnosticoTerminal.equals("autorizada")) {

            marca_ter_validaciones.setText(Global.terminalVisualizar.getTerm_brand());
            modelo_ter_validaciones.setText(Global.terminalVisualizar.getTerm_model());
            serial_ter_validaciones.setText(Global.terminalVisualizar.getTerm_serial());
            tecno_ter_validaciones.setText(Global.terminalVisualizar.getTerm_technology());
            estado_ter_validaciones.setText(Global.terminalVisualizar.getTerm_status());
            garantia_ter_validaciones.setText("No establecida");

            if (Global.terminalVisualizar.getTerm_date_finish() != null) {
                if (!Global.terminalVisualizar.getTerm_date_finish().trim().isEmpty()) {
                    int comp=compararFechas(Global.terminalVisualizar.getTerm_date_finish());
                    System.out.println("garantía: "+comp);
                    if (comp>0) {
                        garantia_ter_validaciones.setText("Con garantía");
                    }else if(comp==-3){
                        garantia_ter_validaciones.setText("No establecida");
                    } else {
                        garantia_ter_validaciones.setText("Sin garantía");
                    }
                } else {

                }
            }

            if (Global.terminalVisualizar.getTerm_date_reception() != null) {
                fechaRecepción = Utils.darFormatoFecha2(Global.terminalVisualizar.getTerm_date_reception());
                fechal_ans_ter_validaciones.setText(fechaRecepción);
            }
            if (Global.terminalVisualizar.getTerm_date_ans() != null) {
                fechaANS = Utils.darFormatoFecha2(Global.terminalVisualizar.getTerm_date_reception());
                fechal_ans_ter_validaciones.setText(fechal_ans_ter_validaciones.getText().toString() + " - " + fechaANS);
            }
            fechal_ans_ter_validaciones.setText("");


        }


        btn_siguiente.setText("Siguiente");
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean verificacionEstados = validarEstadosValidaciones();
                if (verificacionEstados) {
                    // Global. lista_tipificaciones_tabla =new ArrayList<Tipificacion>();
                    objeto.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor_main, new TipificacionesFragment()).addToBackStack(null).commit();
                    return;
                }
            }
        });
        llenarTabla();

        return v;

    }

    /**
     *
     * @param garantia fecha fin de lka garantia
     * @return 0 --->si fecha1==fecha2, 1-->  fecha1>fecha2, -1-->fecha1 <fecha2
     */
    public int compararFechas(String garantia) {//Dia/mes/año (20/01/18)
        System.out.println("Fecha actual: " + fechaActual);
        System.out.println("Fecha termina: " + garantia);
        String[] fecha1 = fechaActual.split("/");
        if (garantia!=null){
            if (!garantia.trim().isEmpty()&&!garantia.equals("Invalid Date")){
                String[] fecha2 = garantia.split(" ");

                int mes = Utils.obtenerNumMes(fecha1[1]);
                int mes2 = Utils.obtenerNumMes(fecha2[1]);
                int dia = Integer.parseInt(fecha1[0]);
                int dia2 = Integer.parseInt(fecha2[2]);
                int anio = Integer.parseInt(fecha1[2]);
                int anio2 = Integer.parseInt(fecha2[3]);

                if ((anio2 - anio) > 0) {
                    return 1;
                } else if ((anio2 - anio) == 0) {

                    if ((mes2 - mes > 0)) {
                        return 1;
                    }

                    if (mes2 - mes < 0) {
                        return -1;
                    } else if (mes2 - mes == 0) {
                        if (dia2 > dia) {
                            return 1;
                        }
                        if (dia2 < dia) {
                            return -1;
                        } else if (dia2 == dia) {

                        }

                    }
                } else if ((anio2 - anio) < 0) {
                    return -1;
                }


                return 0;
            }
        }

        return -3;//si no tiene fecha fin
    }

    /**
     * Este metodo Recorre el arreglo de validaciones y verifica que todos los estados esten ok o NA.     *
     *
     * @return true--> si el estado de la validacion es ok o Na(No aplica). false--> si el estado es Falla
     */
    public boolean validarEstadosValidaciones() {
        boolean retorno = true;
        recorrerTabla(tabla);
        int contFalla = 0;
        Validacion val = new Validacion();
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            val = Global.VALIDACIONES.get(i);
            if (val != null) {
                if (val.getEstado().isEmpty() || val.getEstado() == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(objeto).create();
                    alertDialog.setTitle("Información");
                    alertDialog.setMessage("Verifique el estado de la validación: " + val.getTeva_description());
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                    return false;
                } else {
                    Validacion v = new Validacion(Global.serial_ter, val.getTeva_description(), val.getEstado());
                    Global.VALIDACIONES_DIAGNOSTICO.add(v);
                    retorno = true;
                }
            }
        }
        return retorno;
    }


    /**
     * Este metodo se utiliza para recorrer la tabla mostrada de validaciones y cambia el estado
     * de la validacion al presionar un radio button
     *
     * @param tabla
     */
    public void recorrerTabla(TableLayout tabla) {

        int pos_fila;
        int pos_radio;

        for (int i = 1; i < tabla.getChildCount(); i++) {//filas
            View child = tabla.getChildAt(i);
            TableRow row = (TableRow) child;
            pos_fila = row.getId();
            System.out.println("fila: " + pos_fila);
            View view = row.getChildAt(0);//celdas

            view = row.getChildAt(1);//celda en la pos 1
            if (view instanceof RadioGroup) {
                pos_radio = ((RadioGroup) view).getCheckedRadioButtonId();
                System.out.println("Pos_radio: " + pos_radio);

                if (pos_radio == (300 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("ok");
                    Global.VALIDACIONES.get(i - 1).setOk(true);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                    System.out.println("isOK");
                }
                if (pos_radio == (400 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("falla");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(true);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(false);
                    System.out.println("isFalla");
                }
                if (pos_radio == (500 + pos_fila)) {
                    Global.VALIDACIONES.get(i - 1).setEstado("na");
                    Global.VALIDACIONES.get(i - 1).setOk(false);
                    Global.VALIDACIONES.get(i - 1).setFalla(false);
                    Global.VALIDACIONES.get(i - 1).setNo_aplica(true);
                    System.out.println("isNa");
                }
            }
            System.out.println("Pos: " + i + "-->" + Global.VALIDACIONES.get(i - 1).getTeva_description() + "-" + Global.VALIDACIONES.get(i - 1).getEstado());
        }
    }


    /**
     * Metodo utilizado para llenar la tabla de validaciones
     **/
    public void llenarTabla() {
        if (Global.VALIDACIONES.size() == 0) {
            Toast.makeText(objeto, "No tiene validaciones", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < Global.VALIDACIONES.size(); i++) {
            TableRow fila = new TableRow(objeto);
            fila.setId(i);
            fila.setBackgroundResource(R.drawable.borde_inferior_gris);
            fila.setGravity(Gravity.CENTER_VERTICAL);

            //celdas

            TextView nombre = new TextView(objeto);
            nombre.setId(200 + i);
            nombre.setText(Global.VALIDACIONES.get(i).getTeva_description());
            nombre.setWidth(2);
            nombre.setPadding(20, 0, 0, 0);

            RadioGroup rg = new RadioGroup(objeto);


            RadioButton ok = new RadioButton(objeto);
            ok.setId(300 + i);
            ok.setChecked(false);
            ok.setText("   ");

            RadioButton falla = new RadioButton(objeto);
            falla.setId(400 + i);
            falla.setChecked(false);
            falla.setText("   ");

            RadioButton na = new RadioButton(objeto);
            na.setId(500 + i);
            na.setChecked(false);
            na.setText("  ");

            rg.addView(ok);
            rg.addView(falla);
            rg.addView(na);
            rg.setOrientation(LinearLayout.HORIZONTAL);
            fila.addView(nombre);
            fila.addView(rg);
            tabla.addView(fila);


        }
    }
}
