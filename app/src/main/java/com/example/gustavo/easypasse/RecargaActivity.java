package com.example.gustavo.easypasse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class RecargaActivity extends AppCompatActivity {

    private EditText valorRecarga;
    private EditText cpf;
    private ImageButton cartaoCreditoDebito;
    private ImageButton boleto;
    private TextView saldoDisponivel;
    private TextView saldoGasto;
    private TextView renovarRecarga;
    private TextView transSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        valorRecarga = (EditText) findViewById(R.id.edtValorRecarga);
        cpf = (EditText) findViewById(R.id.edtCpf);

        saldoDisponivel = (TextView) findViewById(R.id.tvValorSaldoDisponivel);
        saldoGasto = (TextView) findViewById(R.id.tvValorSaldoGasto);
        renovarRecarga = (TextView) findViewById(R.id.tvDataRenovarRecarga);
        transSaldo = (TextView) findViewById(R.id.tvValorTransSaldo);

        cartaoCreditoDebito = (ImageButton) findViewById(R.id.ibCartaoCredito);

        cartaoCreditoDebito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "Botão chamado");
                //getSupportFragmentManager().beginTransaction().add(R.id.llContainer);
            }
        });

    }



}
