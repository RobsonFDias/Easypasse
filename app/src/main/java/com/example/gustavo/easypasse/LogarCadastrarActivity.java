package com.example.gustavo.easypasse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LogarCadastrarActivity extends AppCompatActivity {

    private Button botaoacessar;
    private Button botaoCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botaoacessar = (Button) findViewById(R.id.btnAcessar);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastre_se);

        botaoacessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent principalIntent = new Intent(LogarCadastrarActivity.this, PrincipalActivity.class);
                startActivity(principalIntent);
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastrarIntent = new Intent(LogarCadastrarActivity.this, CadastroActivity.class);
                startActivity(cadastrarIntent);
            }
        });

    }



}
