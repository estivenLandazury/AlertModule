package com.landa.alertmodule.interfaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.landa.alertmodule.R;

public class crearCuentaActivity extends AppCompatActivity {
   Spinner tipodocumentos;
   String documentoSelecionado;
   TextView tipoDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        tipodocumentos= findViewById(R.id.cuentaTipoDoc);


        final ArrayAdapter<String> myAdapter= new ArrayAdapter<String>(crearCuentaActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.TipoDocumento));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        tipodocumentos.setAdapter(myAdapter);

        tipodocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                documentoSelecionado= myAdapter.getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
