package br.com.easypasse.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import br.com.easypasse.R;

public class CarregandoActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Integer contador = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

iniciaProgress();
    }

    private void iniciaProgress(){
        try{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (contador < 100){
                        contador+=1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(contador);
                            }
                        });
                        if(contador == 99){
                            abrirTelaLogin();
                        }

                        try {
                            Thread.sleep(200);
                        }catch (InterruptedException i){
                            i.printStackTrace();
                        }

                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirTelaLogin(){
        try {
            startActivity(new Intent(CarregandoActivity.this,LogarCadastrarActivity.class));
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
