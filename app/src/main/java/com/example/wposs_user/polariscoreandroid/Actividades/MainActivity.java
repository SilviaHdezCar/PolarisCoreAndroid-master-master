package com.example.wposs_user.polariscoreandroid.Actividades;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterEtapas;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterRepuesto;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterTerminal_asociada;
import com.example.wposs_user.polariscoreandroid.Adaptadores.AdapterValidaciones;
import com.example.wposs_user.polariscoreandroid.Comun.Global;
import com.example.wposs_user.polariscoreandroid.Comun.Messages;
import com.example.wposs_user.polariscoreandroid.Comun.Utils;
import com.example.wposs_user.polariscoreandroid.DialogOpcionesConsulta;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ActualizarClave_perfil;
import com.example.wposs_user.polariscoreandroid.Fragmentos.EtapasTerminal;
import com.example.wposs_user.polariscoreandroid.Fragmentos.InicialFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.PerfilFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.ProductividadFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.StockFragment;
import com.example.wposs_user.polariscoreandroid.Fragmentos.TipificacionesFragment;
import com.example.wposs_user.polariscoreandroid.R;
import com.example.wposs_user.polariscoreandroid.RegistroDiagnostico;
import com.example.wposs_user.polariscoreandroid.TCP.TCP;
import com.example.wposs_user.polariscoreandroid.Tools;
import com.example.wposs_user.polariscoreandroid.ValidacionesTerminalesAsociadas;
import com.example.wposs_user.polariscoreandroid.java.Etapas;
import com.example.wposs_user.polariscoreandroid.java.Observacion;
import com.example.wposs_user.polariscoreandroid.java.Repuesto;
import com.example.wposs_user.polariscoreandroid.java.Terminal;
import com.example.wposs_user.polariscoreandroid.java.Tipificacion;
import com.example.wposs_user.polariscoreandroid.java.Usuario;
import com.example.wposs_user.polariscoreandroid.java.Validacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private Vector<Repuesto> repuestos;
    private AutoCompleteTextView autocomplete_tipificaciones;


    private TextView claveActual;
    private TextView clavenueva;
    private TextView claveConfirmarClave;

    private Button btn_asociadas;
    private Button btn_autorizadas;


    private TextView serial;
    private Button buscar_serial_terminal;
    private List<Terminal> terminales;
    Terminal t6;
    Terminal t7;
    Terminal t9;

    String serialObtenido = "";

    //private AutoCompleteTextView serial;

    private Spinner spinner_estado_terminal;
    private EditText f_inicio;
    private EditText f_fin;
    FragmentManager fragmentManager;

    public static MainActivity objeto;

    private LinearLayout layout_terminal_etapas;
    private TextView serial_ter_seleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(null);
        setSupportActionBar(toolbar);

        agregarRepuestos();
        objeto = this;
        btn_asociadas = (Button) findViewById(R.id.btn_terminales_asociadas);
        btn_autorizadas = (Button) findViewById(R.id.btn_terminales_autorizadas);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).commit();


        // verTerminalesAsociadas();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Activity_login.class);
        startActivity(i);
        finish();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {


            case R.id.btn_home:
                fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).commit();
                return true;

            case R.id.btn_aumentar:
                Intent inte = new Intent(this, RegistroDiagnostico.class);
                startActivity(inte);
                finish();


                return true;

            case R.id.btn_disminuir:
                dismuir();
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    private void dismuir() {

    }

    private void aumentar() {
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // AL SELECCIONAR ALGUUNA OPCION DEL MENU
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).commit();
            // cargarDatosPerfil();
        } else if (id == R.id.nav_stock) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new StockFragment()).commit();
        } else if (id == R.id.nav_consultar_terminales_reparadas) {
            opcionesBusqueda();

        } else if (id == R.id.nav_productividad) {
            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ProductividadFragment()).commit();

        } else if (id == R.id.nav_cerrar_sesion) {
            Intent i = new Intent(this, Activity_login.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    /**********************************************************************
     * METODOS PARA ACTUALIZAR LA CLAVE DESDE EL PERFIL
     * **/

    public void cancelarCambioclave(View v) {
        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).commit();

    }

    public void actualizarClave(View view) {

        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ActualizarClave_perfil()).commit();
    }

    public void aceptarCambioClave(View v) {
        //fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ActualizarClave_perfil()).commit();

        claveActual = (EditText) findViewById(R.id.perfil_clave_actual);
        clavenueva = (EditText) findViewById(R.id.perfil_clave_nueva);
        claveConfirmarClave = (EditText) findViewById(R.id.perfil_clave_confirmar);

        String actual = claveActual.getText().toString();
        String nueva = clavenueva.getText().toString();
        String confirmacion = claveConfirmarClave.getText().toString();

        final String msj = validarClave(actual, nueva, confirmacion);
        Toast.makeText(this, msj, Toast.LENGTH_LONG).show();

        if (!msj.equalsIgnoreCase("Actualización exitosa")) {
            Toast.makeText(this, msj, Toast.LENGTH_LONG).show();
            return;
        } else {

            fragmentManager.beginTransaction().replace(R.id.contenedor_main, new PerfilFragment()).commit();
        }

    }

    //este metodo hace las validaciones escritas en el cuaderno
    private String validarClave(String actual, String nueva, String confirmacion) {
        String msj = "Actualización exitosa";
        //validaciones
        if (actual.isEmpty()) {
            return "Debe ingresar la contraseña actual";
        } else if (nueva.isEmpty()) {
            return "Debe ingresar la contraseña nueva";
        } else if (confirmacion.isEmpty()) {
            return "Debe confirmar la contraseña nueva";
        } else if (!actual.equals("1")) {
            return "La contraseña actual es incorrecta";
        } else if (!(nueva.length() >= 8)) {
            return "La contraseña debe contener como minimo 8 caracteres";
        } else if (!revisarMayMinNum(nueva)) {
            return "La contraseña debe contener números, letras en mayúscula y minúscula";
        } else if (nueva.equals(actual)) {
            return "La contraseña  nueva debe ser diferente a la actual";
        } else if (!nueva.equals(confirmacion)) {
            return "La confirmación de contraseña no coincide con la clave ingresada";
        }
        //consumir servicio para cambiar clave


        return msj;
    }

    //este metodo es para validar que la clave contenga numeros, letras minus y mayus
    public boolean revisarMayMinNum(String password) {
        String msj = "";

        char clave;

        byte contNumero = 0;
        byte contLetraMay = 0;
        byte contLetraMin = 0;

        for (byte i = 0; i < password.length(); i++) {

            clave = password.charAt(i);

            String passValue = String.valueOf(clave);

            if (passValue.matches("[A-Z]")) {

                contLetraMay++;
            } else if (passValue.matches("[a-z]")) {

                contLetraMin++;
            } else if (passValue.matches("[0-9]")) {

                contNumero++;
            }
        }
        if (contLetraMay > 0 && contNumero > 2 && contLetraMin > 0) {
            return true;
        }


        return false;
    }


    /**
     * BUSQUEDA DE TERMINALES
     **/

    public void opcionesBusqueda() {
        DialogOpcionesConsulta dialog = new DialogOpcionesConsulta();
        dialog.show(getSupportFragmentManager(), "Actualización de la clave.main");
    }


    //********************************************AGREGAR TERMINALES*********************************************************************************************






    //********************************************AGREGAR REPUESTOS*********************************************************************************************


    private void agregarRepuestos() {
        repuestos = new Vector<>();


    }


    //*******************************BUSQUEDA POR SERIAL

    public void buscarTerminalesPorSerial(View v) {
        this.buscar_serial_terminal = (Button) findViewById(R.id.btn_buscar_terminales_serial);
        serial = (TextView) findViewById(R.id.serial);

        Vector<Terminal> terminal = new Vector<>();
        terminal.removeAllElements();

        if (serial.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el serial", Toast.LENGTH_SHORT).show();
            return;
        }


        for (Terminal ter : this.terminales) {
            if (ter.getTerm_serial().equalsIgnoreCase(serial.getText().toString())) {
                terminal.add(ter);
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_consultaTerminales_por_serial);
                recyclerView.setAdapter(new AdapterTerminal(this, terminal));//le pasa los datos-> lista de usuarios
                layoutManager = new LinearLayoutManager(this);// en forma de lista
                recyclerView.setLayoutManager(layoutManager);

            }
        }
        if (terminal.size() == 0) {
            Toast.makeText(this, "No se encontraron terminales registradas con ese serial", Toast.LENGTH_SHORT).show();
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_consultaTerminales_por_serial);
            recyclerView.setAdapter(new AdapterTerminal(this, terminal));//le pasa los datos-> lista de usuarios
            layoutManager = new LinearLayoutManager(this);// en forma de lista
            recyclerView.setLayoutManager(layoutManager);
        }
        serial.setText("");
    }

    //******************************BUSQUEDA POR FECHAS*****************************


    public void buscarTerminalesFechas(View V) {
        spinner_estado_terminal = (Spinner) findViewById(R.id.sp_estado_terminal);
        String estado = spinner_estado_terminal.getSelectedItem().toString();

        // if()


    }


    /**********************************************************************************************************
     * METODOS ATRÁS DE LOS BOTONES     * siguienteEtapas
     * *******************************************************************************************************/

    //boton atras de la calse etapas
    public void volverEtapas(View v) {
        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new InicialFragment()).commit();
    }

    public void siguienteEtapas(View v) {


        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).commit();

        Global.WEB_SERVICE = "/PolarisCore/Terminals/validatorTerminal";
        new TaskListarValidaciones().execute();

    }

    /**********************************************************************************************************
     * METODOS ATRÁS Y SIGUIENTE DE LOS BOTONES     VALIDACIONES
     * *******************************************************************************************************/
    public void volverValidaciones(View view) {
        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new EtapasTerminal()).commit();
    }

    public void siguienteValidaciones(View view) {

        //Hacer un mtodo y llamrlo acá --> en el que agregue a un arreglo la lista de validaciones


        Global.WEB_SERVICE = "/PolarisCore/Terminals/tipesValidatorTerminal";

        new TaskListarTipificaciones().execute();
    }


    //METODOS PARA MOSTRAR EL CALENDARIO


    //***********TERMINALES ASOCIADAS
//Listas las terminales asociadas al tecnico que inicio sesión en la app

    public void verTerminalesAsociadas() {


        Global.WEB_SERVICE = "/PolarisCore/Terminals//associatedsWithDiagnosis";


        new TaskListarTerminalesAsociadas().execute();//hacer la peticion para que me retorne la lista de terminales


    }


    /**
     *
     **/
    public void llenarRVEtapas(List<Observacion> observaciones) {

       /* Vector<Observacion> obs = new Vector<>();

        for (Observacion o : observaciones) {
            if (o != null) {
                obs.add(o);
            }

        }
        if (obs.size() == 0) {
            Toast.makeText(MainActivity.this, "Aún no tiene observaciones", Toast.LENGTH_SHORT).show();
        }

      //  recyclerView = (RecyclerView) findViewById(R.id.recycler_view_etapas);
        recyclerView.setAdapter(new AdapterEtapas(MainActivity.this, obs));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(this);// en forma de lista
        recyclerView.setLayoutManager(layoutManager);*/


    }

    public void llenarRVValidaciones(List<Validacion> validacions) {

        // fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).commit();

        Vector<Validacion> vals = new Vector<>();

        for (Validacion v : validacions) {
            if (v != null) {
                vals.add(v);
            }

        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_validaciones);
        recyclerView.setAdapter(new AdapterValidaciones(objeto, vals));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(this);// en forma de lista
        recyclerView.setLayoutManager(layoutManager);


    }

    //este metodo llena el recycler view con las terminales obtenidas al consumir el servicio

    public void llenarRVAsociadas(List<Terminal> terminalesRecibidas) {
        //************************SE MUESTRA LA LISTA DE TERMINALES ASOCIADAS
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view_consultaTerminales_inicial);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(Tools.getCurrentContext());
        rv.setLayoutManager(llm);

        ArrayList terminals = new ArrayList<>();

        //recorro la lista obtenida y la agg a la lista

        for (Terminal ter : terminalesRecibidas) {
            if (ter != null) {
                terminals.add(ter);//  butons.add(new ButtonCard(nombre, "","",icon,idVenta));
            }
        }


        final AdapterTerminal_asociada adapter = new AdapterTerminal_asociada(terminals, new AdapterTerminal_asociada.interfaceClick() {//seria termi asoc
            @Override
            public void onClick(List<Terminal> terminal, int position) {

                /********************
                 *  cuando de clic en el panel de la terminal que desea ver los detalles, captura la posicion del panel donde dio clic
                 *  y consume el servicio de listarObservacionesTeerminal
                 *  **************/

                serialObtenido = terminal.get(position).getTerm_serial();
                Global.modelo = terminal.get(position).getTerm_model();


                System.out.println("-------*****-_-**********************************VA A LISTAR LAS ETAPAS DE LA TERMINAL SELECCIONADA");

                listarObservacionesTerminal(serialObtenido);
                // listarObservacionesTerminal("prueba");
            }
        }, R.layout.panel_terminal_asociada);

        rv.setAdapter(adapter);

    }

    /********************************************************
     * INICIO----->METODOS DEL FRAGMENT TIPIFICACIONES     *
     *****************************************************************/



    //boton atras de la calse TIPIFICACIONES
    public void volverTipificaciones(View v) {
        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).commit();
        //LLENAR RCV CON EL ARREGLO GLOBAL DE VALIDACIONES
    }

    //boton SIGUIENTE de la calse TIPIFICACIONES
    public void siguienteTipificaciones(View v) {


        fragmentManager.beginTransaction().replace(R.id.contenedor_main, new ValidacionesTerminalesAsociadas()).commit();

        Global.WEB_SERVICE = "/PolarisCore/Terminals/validatorTerminal";
        new TaskListarValidaciones().execute();

    }

    /********************************************************
     * FIN----->METODOS DEL FRAGMENT TIPIFICACIONES     *
     *****************************************************************/


    //Mostrar las terminales  asociadas al dar clic en el boton
    public void verTerminalesAsociadas(View v) {
        btn_asociadas = (Button) findViewById(R.id.btn_terminales_asociadas);
        btn_autorizadas = (Button) findViewById(R.id.btn_terminales_autorizadas);
        layout_terminal_etapas = (LinearLayout) findViewById(R.id.layout_terminal_etapas);

        btn_autorizadas.setBackgroundColor(getResources().getColor(R.color.azul_nav_bar_transparencia));//azul_nav_bar_transparencia

        btn_asociadas.setBackgroundColor(getResources().getColor(R.color.azul_claro_nav_bar));


        verTerminalesAsociadas();
    }


    //******************consumir servicio listar terminales asociadas
    class TaskListarTerminalesAsociadas extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando terminaleas asociadas...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgListaAsociadas();

            trans = TCP.transaction(Global.outputLen);

            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK) {

                System.out.println();
                return true;
            } else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                System.out.println("*********************************************************************SI SE PUDO CONECTAR LISTAR TERM****************************");
                if (Messages.unPackMsgListarAsociadas(MainActivity.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;

                    if (Global.TERMINALES_ASOCIADAS == null) {
                        Toast.makeText(objeto, Global.CODE + " No tiene terminales asociadas", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    llenarRVAsociadas(Global.TERMINALES_ASOCIADAS);


                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, MainActivity.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR");
        }


    }


    /*********************************************************************************
     * Este metodo lo invoco al dar clic en una de las terminales listadas
     * ESTE METODO SE UTILIZA PARA CONSUMIR EL SERVICIO DE LISTAR OBSERVACIONES DE LA TER SELECCIONADA
     * RECIBE EL SERIAL DE LA TERMINAL DE LA CUAL DESEA VER LOS DETALLES DE LAS ETAPAS
     *************************************************************************************/
    public void listarObservacionesTerminal(String serial) {
        Global.WEB_SERVICE = "/PolarisCore/Terminals/observations";
        Global.serial_ter = serial;

        new TaskListarObservaciones().execute();
    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LAS OBSERVACIONES
     *
     ***************************************************** **/

//******************consumir servicio listar observaciones
    class TaskListarObservaciones extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando observaciones de la terminal...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgListarObservaciones();

            trans = TCP.transaction(Global.outputLen);

            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK)
                return true;
            else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                System.out.println("*********************************************************************SI SE PUDO CONECTAR LISTAR OBSER****************************");
                if (Messages.unPackMsgListarObservaciones(MainActivity.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;
                    fragmentManager.beginTransaction().replace(R.id.contenedor_main, new EtapasTerminal()).commit();


                    //   objeto.llenarRVEtapas(Global.OBSERVACIONES);

                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, MainActivity.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR OBSERVA");
        }


    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LAS VALIDACIONES
     *
     ***************************************************** **/

//******************consumir servicio listar observaciones
    class TaskListarValidaciones extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando validaciones de la terminal...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar coenxión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgListarValidaciones();

            trans = TCP.transaction(Global.outputLen);
            System.out.println("-----------RESULTADO TRANS = " + trans);
            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK) {
                System.out.println("-------------trans == Global.TRANSACTION_OK*******************************************************");
                return true;
            } else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                System.out.println("*******************************SI SE PUDO CONECTAR LISTAR VALIDACIONES****************************");
                if (Messages.unPackMsgListarValidaciones(MainActivity.this)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;


                    if (Global.VALIDACIONES == null) {
                        Toast.makeText(objeto, Global.serial_ter + " No tiene validaciones", Toast.LENGTH_SHORT).show();

                    } else {
                        objeto.llenarRVValidaciones(Global.VALIDACIONES);
                    }
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        // Utils.GoToNextActivity(Activity_login.this, DialogError.class, Global.StatusExit);
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Toast.makeText(Activity_login.this, Global.mensaje, Toast.LENGTH_LONG).show();
                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, MainActivity.this)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(MainActivity.this, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR VALIDACIONES");
        }


    }


    /*************************************************************************************
     * CLASE QUE CONSUME EL SERVICIO PARA LISTAR LAS VALIDACIONES
     *
     ***************************************************** **/

//******************consumir servicio listar observaciones
    class TaskListarTipificaciones extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;
        int trans = 0;


        /*******************************************************************************
         Método       : onPreExecute
         Description  : Se ejecuta antes de realizar el proceso, muestra una ventana con uin msj de espera
         *******************************************************************************/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(objeto, R.style.MyAlertDialogStyle);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Buscando tipificaciones del diagnóstico...");
            progressDialog.show();
        }


        /*******************************************************************************
         Método       : doInBackground
         Description  : Se ejecuta para realizar la transacción y verificar conexión
         *******************************************************************************/
        @Override
        protected Boolean doInBackground(String... strings) {
            Messages.packMsgListarTipificaciones();

            trans = TCP.transaction(Global.outputLen);

            // Verifica la transacción
            if (trans == Global.TRANSACTION_OK)
                return true;
            else
                return false;
        }

        /*******************************************************************************
         Método       : onPostExecute
         Description  : Se ejecuta después de realizar el doInBackground
         *******************************************************************************/
        @Override
        protected void onPostExecute(Boolean value) {

            progressDialog.dismiss();

            if (value) {
                System.out.println("*******************************SI SE PUDO CONECTAR LISTAR TIPIFICACIONES****************************");
                if (Messages.unPackMsgListarTipificaciones(objeto)) {
                    Global.enSesion = true;
                    Global.StatusExit = true;
                    fragmentManager.beginTransaction().replace(R.id.contenedor_main, new TipificacionesFragment()).commit();
                    //inflar el fragmento de tipificaciones y cargar las tipificaciones en el autocomplete
                } else {
                    // Si el login no es OK, manda mensaje de error
                    try {
                        Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Si es falso, cierra el socket y vuelve a crearlo, si es verdadero el socket continua abierto
                TCP.disconnect();

            } else {
                switch (Utils.validateErrorsConexion(false, trans, objeto)) {

                    case 0:                                                                         // En caso de que continue = true y error data
                        break;

                    case 1:                                                                         // En caso de que continue = false y error data
                        break;

                    default:                                                                        // Errores de conexion
                        Global.MsgError = Global.MSG_ERR_CONEXION;
                        Global.mensaje = Global.MsgError;
                        Global.StatusExit = false;
                        // Muestra la ventana de error
                        Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
                        break;
                }

                Toast.makeText(objeto, Global.mensaje, Toast.LENGTH_LONG).show();
            }
            System.out.println("******************TERMINÓ DE CONSUMIR EL SERVICIO DE LISTAR TIPIFICAICONES");
        }


    }


    //*************AUTORIZADAS
    public void verTerminalesAutorizadas(View v) {
        btn_asociadas = (Button) findViewById(R.id.btn_terminales_asociadas);
        btn_autorizadas = (Button) findViewById(R.id.btn_terminales_autorizadas);

        btn_asociadas.setBackgroundColor(0x802196F5);

        btn_autorizadas.setBackgroundColor(0x45A5F3);

        Vector<Terminal> terminales_aut = new Vector<>();
        for (Terminal ter : this.terminales) {
            if ((ter.getTerm_status()).equalsIgnoreCase("Autorizada")) {
                terminales_aut.add(ter);
            }

        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_consultaTerminales_inicial);
        recyclerView.setAdapter(new AdapterTerminal(this, terminales_aut));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(this);// en forma de lista
        recyclerView.setLayoutManager(layoutManager);
    }


    public void cargarTerminal_stock(View view) {


      /*  btn_mostrarTerminales = (Button) findViewById(R.id.btn_ter_stock);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_stock);
        recyclerView.setAdapter(new AdapterTerminal(this, this.terminales));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(this);// en forma de lista
        recyclerView.setLayoutManager(layoutManager);
    */
    }

    Button btn_mostrarRepuestos;

    public void cargarRepuesto_stock(View view) {

        btn_mostrarRepuestos = (Button) findViewById(R.id.btn_rep_stock);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_stock);
        recyclerView.setAdapter(new AdapterRepuesto(this, repuestos));//le pasa los datos-> lista de usuarios

        layoutManager = new LinearLayoutManager(this);// en forma de lista
        recyclerView.setLayoutManager(layoutManager);
    }


    public List<Terminal> getTerminales() {
        System.out.println("***************************************LISTA DE TERMINALES********************************************" + this.terminales.size());
        return terminales;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public EditText getF_inicio() {
        return f_inicio;
    }

    public void setF_inicio(EditText f_inicio) {
        this.f_inicio = f_inicio;
    }

    public EditText getF_fin() {
        return f_fin;
    }

    public void setF_fin(EditText f_fin) {
        this.f_fin = f_fin;
    }


    public Terminal getT6() {
        return t6;
    }

    public Terminal getT7() {
        return t7;
    }

    public Terminal getT9() {
        return t9;
    }


}





