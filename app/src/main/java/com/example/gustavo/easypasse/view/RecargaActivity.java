package com.example.gustavo.easypasse.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.gustavo.easypasse.R;

public class RecargaActivity extends AppCompatActivity {

    private ImageButton cartaoCredito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cartaoCredito  = (ImageButton) findViewById(R.id.ibCartaoCredito);

        cartaoCredito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCartaoCredito = new Intent(RecargaActivity.this, CartaoCreditoActivity.class);
                startActivity(intentCartaoCredito);
            }
        });

    }



}
