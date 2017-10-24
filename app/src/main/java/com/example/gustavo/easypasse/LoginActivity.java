package com.example.gustavo.easypasse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button acessar = (Button) findViewById(R.id.btnAcessar);
        
    }


    public void cadastro(View view) {
        Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(cadastro);
    }

    public void acessar(View view) {
        EditText cpf = (EditText) findViewById(R.id.edtCpf);
        EditText senha = (EditText) findViewById(R.id.edtSenha);
        Toast.makeText(this, "CPF "+cpf.getText().toString()+" senha "+senha.getText().toString(), Toast.LENGTH_LONG).show();

        Intent intentPrincipal = new Intent(LoginActivity.this, PrincipalActivity.class);
        startActivity(intentPrincipal);
    }
}
