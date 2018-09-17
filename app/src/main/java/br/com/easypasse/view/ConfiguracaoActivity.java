package br.com.easypasse.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import br.com.easypasse.R;
import br.com.easypasse.controller.UsuarioControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.UsuarioModelo;
import br.com.easypasse.utils.ObjetosTransitantes;

public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RelativeLayout rlAjuda = (RelativeLayout) findViewById(R.id.rlAjuda);
        rlAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfiguracaoActivity.this, AjudaActivity.class));
                finish();
            }
        });

        RelativeLayout rlConta = (RelativeLayout) findViewById(R.id.rlConta);
        rlConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfiguracaoActivity.this, ContaActivity.class));
                finish();
            }
        });

        RelativeLayout rlDesconectar = (RelativeLayout) findViewById(R.id.rlDesconectar);
        rlDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sair();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ConfiguracaoActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void desconectar() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

            UsuarioModelo usuarioModelo = new UsuarioControle().buscarUsuarioId(ObjetosTransitantes.USUARIO_MODELO.getId());
            if (usuarioModelo != null) {
                usuarioModelo.setLogado("0");

                new UsuarioControle().atualizarUsuario(usuarioModelo);

                startActivity(new Intent(ConfiguracaoActivity.this, LogarCadastrarActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sair() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Deseja realmente sair do EasyPasse");
        alertDialogBuilder.setPositiveButton("sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        desconectar();
                    }
                });

        alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
