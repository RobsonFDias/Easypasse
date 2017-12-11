package com.example.gustavo.easypasse;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastroActivity extends AppCompatActivity {

    private Button botaoSalvar;
    private EditText usuario;
    private EditText cpf;
    private EditText senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        botaoSalvar = (Button) findViewById(R.id.btnSalvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = (EditText) findViewById(R.id.edtUsuario);
                cpf = (EditText) findViewById(R.id.edtCpf);
                senha = (EditText) findViewById(R.id.edtSenha);
//                Log.d("dados", "usuario "+usuario.getText().toString()+
//                        " cpf "+cpf.getText().toString()+
//                        " senha "+Utilitarios.md5(senha.getText().toString()));

                JSONObject dadosJsonObject = new JSONObject();
                try {
                    dadosJsonObject.put("username", usuario.getText().toString() );
                    dadosJsonObject.put("cpf", cpf.getText().toString() );
                    dadosJsonObject.put("senha", Utilitarios.md5(senha.getText().toString()) );
                    dadosJsonObject.put("method", "app-set-usuario");
                    dadosJsonObject.toString();
                    //Log.d("Dados que vão", dadosJsonObject.toString());

                    JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                            "http://easypasse.com.br/gestao/wsCadastro.php", dadosJsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Dados que vem", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }



    public void voltarLogin(View view) {
        Intent voltar = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(voltar);
    }


}
