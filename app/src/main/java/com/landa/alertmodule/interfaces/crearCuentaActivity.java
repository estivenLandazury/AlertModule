package com.landa.alertmodule.interfaces;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landa.alertmodule.R;
import com.landa.alertmodule.clases.AppMovil;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.clases.RolUsuario;
import com.landa.alertmodule.clases.User;
import com.landa.alertmodule.clases.Usuario;
import com.landa.alertmodule.clases.UsuarioApp;
import com.landa.alertmodule.clases.UsuarioDocumento;
import com.landa.alertmodule.com.WEBUtilDomi;

import java.io.IOException;


public class crearCuentaActivity extends AppCompatActivity implements View.OnClickListener {
    public final static String CONEXION= Constante.CONEXION;

    Spinner tipodocumentos;
    String documentoSelecionado,username,email,password,repeatPass,nombreUsuario,apellidous,numeroId;
    Button registrar;
    TextView tipoDoc;
    EditText nombre, correo, numeroDocmento, apellido, nombreCuenta, contraseña, repiContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        tipodocumentos = findViewById(R.id.cuentaTipoDoc);
        nombre = findViewById(R.id.txtUserName);
        apellido = findViewById(R.id.txtApellido);
        numeroDocmento = findViewById(R.id.numeroDocumento);
        nombreCuenta = findViewById(R.id.username);
        correo = findViewById(R.id.correoM);
        contraseña = findViewById(R.id.txtPass);
        repiContraseña = findViewById(R.id.repitPass);
        registrar = findViewById(R.id.btnRegister);





        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(crearCuentaActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);

        tipodocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                documentoSelecionado = myAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registrar.setOnClickListener(this);
    }

    @Override

    /*GESTION USUARIO---------------------------------------------------------------*/
    public void onClick(View v) {
        username= nombreCuenta.getText().toString();
        email=  correo.getText().toString();
        password=  contraseña.getText().toString();
        repeatPass=repiContraseña.getText().toString();
        nombreUsuario=nombre.getText().toString();
        apellidous=apellido.getText().toString();
        numeroId= numeroDocmento.getText().toString();

        if (v.equals(registrar)){

            new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                try {


                    crearCuentaUsuario(username, email, password, repeatPass);
                    User cu = obtenerCuenta(username);

                    AñadirUsuario(nombre.getText().toString(), apellido.getText().toString(), cu.getId(), numeroDocmento.getText().toString());

                    Usuario usu = retornarUsuario(numeroDocmento.getText().toString());
                    /* Aquí se añade el rol de usuario encargado por defualt creado*/
                    añadirRolUsuario(usu.getId(), Constante.ROL_ENCARGADO + "");

                    /* Aquí se añade el tipo documento que el usuario utiliza*/
                    int tipoDocumento= tipoDocumentoSeleccionado();
                    añadirUsuarioDocumento(usu.getId(),""+tipoDocumento );


                    /*Se crea el registro de la app que será  asociada al usuario*/
                    String nombreApp=nombreCuenta.getText().toString()+numeroDocmento.getText().toString();

                    crearAppMovil(nombreApp);

                    /*Se obtiene el objeto app, a partir del nombre de la app, que es el nombre de usuario y numero dde documento del usuario*/
                    AppMovil app= obtenerApp(nombreApp);

                    /* se asocia el usuario registrado y la app que enviará el estado de alarma a la web*/
                    crearUsuarioApp(usu.getId(),app.getId());

                    /*Almacena el id de la app, para asociarla cuando se registre el usuario peditrico y la manilla*/
                    Constante.setIdApp(app.getId());

                    /*Almacena el id del usuario encargado para asoicarlo cuando se cree el usuario pediatrico*/
                    Constante.setIdUsuarioEncargado(usu.getId());

//                    Constante.setUsrename(cu.getUsername());
//                    Constante.setHotmail(cu.getEmail());





                    showToats("se añadió usuario exitosamente");
                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(login);

                } catch (IOException e) {
                    e.printStackTrace();
                    showToats(e.getMessage());

                    Log.e(">>>>>", "" + "error de registro");
                } catch (Exception e) {
                    showToats(e.getMessage());

                }
            }
        }).start();
        }
    }


    public void crearUsuarioApp(String idUsuario, String idApp){
        Gson json= new Gson();
        UsuarioApp usuApp= new UsuarioApp();
        usuApp.setUsuario(idUsuario);
        usuApp.setAppMovil(idApp);
        String jsonNombre = json.toJson(usuApp);
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"UsuarioApps", jsonNombre);

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(">>>>>",""+"error al crear el usuarioApp");


        }



    }

    public void crearAppMovil(String nombreApp){
        Gson json= new Gson();
        AppMovil app= new AppMovil();
        app.setNombre(nombreApp);
        String jsonNombre = json.toJson(app);
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Apps", jsonNombre);

        } catch (IOException e) {
            e.printStackTrace();

            Log.e(">>>>>",""+"error al crear la AppMovil");


        }

    }


    public AppMovil obtenerApp(String nombreApp){
        Gson manilla = new Gson();
        AppMovil App=null;
        try {
            String  AppM = WEBUtilDomi.GETrequest(CONEXION+"AppName/"+nombreApp);
            App= manilla.fromJson(AppM, AppMovil.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer App");
            e.printStackTrace();
        }
        return App;
    }


    public Usuario retornarUsuario(String numeroDocumento)  {
        Usuario  usuario=null;
        String Jsonusu = null;
        try {
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION + "usuario/" + numeroDocumento);
            Gson gson = new Gson();
            usuario = gson.fromJson(Jsonusu, Usuario.class);
        } catch (IOException e) {

            Log.e(">>>>>", "" + "error al tarer Json de usuario "+ numeroDocumento);
            e.printStackTrace();

        }
        return usuario;

    }



    public void limpiarDatos(){

        nombre.setText(" ");
        apellido.setText(" ");
        numeroDocmento.setText(" ");
        nombreCuenta.setText(" ");
        correo.setText(" ");
        contraseña.setText(" ");
        repiContraseña.setText(" ");
        registrar.setText(" ");

    }

    public void añadirRolUsuario(String idUsuario, String rolUsuario){
        Gson gson = new Gson();
        RolUsuario rol= new RolUsuario();
        rol.setTipoUsuario(rolUsuario);
        rol.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(rol);
       ;
        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION+"rolUsuarios", jsonNombre);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(">>>>>",""+"error al añadir rolUsuario");

        }
    }


    public void añadirUsuarioDocumento(String idUsuario, String tipoDocumento) {
        Gson gson = new Gson();
        UsuarioDocumento usud = new UsuarioDocumento();
        usud.setTipoDocumento(tipoDocumento);
        usud.setUsuario(idUsuario);
        String jsonNombre = gson.toJson(usud);

        try {
            final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "UsuarioDocumentos", jsonNombre);

        } catch (IOException e) {
            Log.e(">>>>>", "" + "error al añadir UsuarioDocumento");

        }


    }


    /*Crea la cuenta del usuario, para poder loguearse posteriormente*/
    public void crearCuentaUsuario(String username, String email, String password, String repeatPassword) throws IOException,Exception{





        Gson json= new Gson();
        User cuenta=new User();

        cuenta.setUsername(username);
        cuenta.setPassword(password);
        cuenta.setEmail(email);
        String jsonNombre = json.toJson(cuenta);

        /*se envía el usuario por el JSON*/
        try {

            if(password.equals(repeatPassword)){


            User cuen= obtenerCuenta(username);
            if(cuen==null) {

                final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "AddUser", jsonNombre);
            }else{
                throw new IOException("el nombre de la cuenta ya existe, elija otro");
            }
            }
            else{
                throw new IOException("Las contraseñas no coinciden, verifique");

            }
        } catch (IOException e) {

            Log.e(">>>>>",""+"error al crear la cuenta");

            throw new IOException(e.getMessage());



        }


    }

    public User obtenerCuenta(String username)throws IOException{
        Gson gson = new Gson();
        User cuenta= null;
        try {
            String  AppM = WEBUtilDomi.GETrequest(CONEXION+"GetUser/"+username);
            cuenta= gson.fromJson(AppM, User.class);
        } catch (IOException e) {
            Log.e(">>>>>",""+"error al traer la cuenta con username: "+ username);
            e.printStackTrace();

        }

        return cuenta;

}


         public void AñadirUsuario(String nombre, String apellido, String idCuenta,String numeroDocumento) throws IOException {

             Gson gson = new Gson();
             Usuario usuario = new Usuario();
             usuario.setNombre(nombre);
             usuario.setApellido(apellido);
             usuario.setNumeroDocumento(numeroDocumento);
             usuario.setUser(idCuenta);
             String jsonNombre = gson.toJson(usuario);

             /*se envía el usuario por el JSON*/
             try {

                 Usuario us= retornarUsuario(numeroDocumento);
                 Log.e(">>>>>",""+"usurio que retorna: "+ us);

                 if(us==null) {
                     final String respuesta = WEBUtilDomi.JsonByPOSTrequest(CONEXION + "usuarios", jsonNombre);
                 }else{
                     throw new IOException("El usuario con el numero documento: " + numeroDocumento + "\n" + "ya se encuentra registrado");
                 }
             }catch (IOException e){
                 throw new IOException(e.getMessage());
             }


         }


    public int tipoDocumentoSeleccionado(){
        int tipo=0;
        if(documentoSelecionado.equals("cedula")){
            tipo= Constante.TIPO_DOC_CEDULA;
        }else if(documentoSelecionado.equals("Tarjeta Identidad")){
            tipo= Constante.TIPO_DOC_IDENTIDAD;
        }

        return tipo;
    }

    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(crearCuentaActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }
}
