package com.example.gustavo.easypasse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class TranferenciaRecargaActivity extends AppCompatActivity {

    private Spinner spinnerAmigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranferencia_recarga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        spinnerAmigos = (Spinner) findViewById(R.id.spinnerAmigos);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerAmigos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmigos.setAdapter(adapter);

        spinnerAmigos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0){
                    Toast.makeText(getBaseContext(), "Fulano", Toast.LENGTH_LONG).show();
                    //String dadoBusca = "Cabeleireiro";
                }else if (i == 1){
                    Toast.makeText(getBaseContext(), "Beltrano", Toast.LENGTH_LONG).show();
                    //String dadoBusca = "Restaurante";
                }else if (i == 2){
                    Toast.makeText(getBaseContext(), "Ciclano", Toast.LENGTH_LONG).show();
                    //String dadoBusca = "Casa de festa";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



}
