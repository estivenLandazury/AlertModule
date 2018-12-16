package com.landa.alertmodule.interfaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.landa.alertmodule.MainActivity;
import com.landa.alertmodule.R;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

//    public final static  String CONEXION= Constante.CONEXION;
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
            Intent Menu = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(Menu);

        }

        if(v.equals(registrarBtn)){
            Intent Menu = new Intent(getApplicationContext(), crearCuentaActivity.class);
            startActivity(Menu);
        }

    }




}
