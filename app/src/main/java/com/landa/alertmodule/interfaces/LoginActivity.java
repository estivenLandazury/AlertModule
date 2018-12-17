package com.landa.alertmodule.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landa.alertmodule.MainActivity;
import com.landa.alertmodule.R;
import com.landa.alertmodule.clases.AppMovil;
import com.landa.alertmodule.clases.Constante;
import com.landa.alertmodule.clases.User;
import com.landa.alertmodule.clases.Usuario;
import com.landa.alertmodule.clases.UsuarioApp;
import com.landa.alertmodule.com.WEBUtilDomi;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public final static  String CONEXION= Constante.CONEXION;
    EditText nombreUsuarioText, contraseñaText;
    Button ingresarBtn, registrarBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nombreUsuarioText= findViewById(R.id.nombreUsuario);
        contraseñaText= findViewById(R.id.contraseña);
        ingresarBtn= findViewById(R.id.ingresar);
        registrarBtn= findViewById(R.id.Registrar);

        ingresarBtn.setOnClickListener(this);
        registrarBtn.setOnClickListener(this);
    }

    public void onClick(View v) {

        if(v.equals(ingresarBtn)){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {


                 autenticarUsuario();
                    } catch (IOException e) {
                        showToats(e.getMessage());
                        Log.e(">>>>>",""+"error al autenticar");

                        e.printStackTrace();
                    }
                }
            }).start();



        }

        if(v.equals(registrarBtn)){
            Intent Menu = new Intent(getApplicationContext(), crearCuentaActivity.class);
            startActivity(Menu);
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

    public void autenticarUsuario()throws IOException{


        Gson gson = new Gson();
        User usuario = new User();
        usuario.setEmail("");
        usuario.setPassword(contraseñaText.getText().toString());
        usuario.setUsername(nombreUsuarioText.getText().toString());

        String jsonNombre = gson.toJson(usuario);


        try {

            User cuent=obtenerCuenta(nombreUsuarioText.getText().toString());

            if(cuent==null){
                throw new IOException("No exsite la cuenta con la que quiere ingresar");
            }else{
                Constante.setUsrename(cuent.getUsername());
                Constante.setHotmail(cuent.getEmail());
                UsuarioApp usuApp= obtenrApp(cuent.getId());

                final String respuesta =  WEBUtilDomi.JsonByPOSTrequest(CONEXION+"Autenticade", jsonNombre);
                Intent Menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(Menu);

            }
        } catch (IOException e) {

            throw new IOException(e.getMessage());
        }
    }


    public UsuarioApp obtenrApp(String idcuenta){
        String Jsonusu = null;
        UsuarioApp usAp=null;
        Usuario us= retornarUsuario(idcuenta);
        /*Guardo en constante Usuario Encargado*/
        Constante.setIdUsuarioEncargado(us.getId());

        try{
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION + "UsuarioIdApp/" + us.getId());
            Gson gson = new Gson();
            usAp = gson.fromJson(Jsonusu, UsuarioApp.class);
            /*Guardo en constante id de App*/
            Constante.setIdApp(usAp.getAppMovil());

        }catch(IOException e){

            Log.e(">>>>>", "" + "error al tarer Usuario app ");
            Log.e(">>>>>", "" + e.getMessage());




        }

        return usAp;

    }

    public Usuario retornarUsuario(String idCuenta)  {
        Usuario  usuario=null;
        String Jsonusu = null;
        try {
            Jsonusu = WEBUtilDomi.GETrequest(CONEXION + "UsuarioCuenta/" + idCuenta);
            Gson gson = new Gson();
            usuario = gson.fromJson(Jsonusu, Usuario.class);
        } catch (IOException e) {

            Log.e(">>>>>", "" + "error al tarer Json de usuario "+ idCuenta);
            e.printStackTrace();

        }
        return usuario;

    }

    private void showToats(final String mensaje) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }

}
