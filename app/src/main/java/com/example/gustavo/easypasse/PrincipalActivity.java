package com.example.gustavo.easypasse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PrincipalActivity extends AppCompatActivity {

    Typeface raleway;
    private TextView mensagem;
    private Button botaoPainelAcesso;
    private TextView saldoDisponivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        raleway = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        mensagem = (TextView) findViewById(R.id.tvMensagem);
        saldoDisponivel = (TextView) findViewById(R.id.tvSaldoDisponivel);
        botaoPainelAcesso = (Button) findViewById(R.id.btnPainelAcesso);

        mensagem.setTypeface(raleway);
        saldoDisponivel.setTypeface(raleway);

        botaoPainelAcesso.setTypeface(raleway);

    }

}
