package com.example.gustavo.easypasse;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LogarCadastrarActivity extends AppCompatActivity {

    private Button botaoacessar;
    private Button botaoCadastrar;

    private EditText cpf;
    private EditText senha;
    private RequestQueue mVolleyRequest;

    Typeface raleway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        raleway = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        cpf = (EditText) findViewById(R.id.edtCpf);
        senha = (EditText) findViewById(R.id.edtSenha);

        mVolleyRequest = Volley.newRequestQueue(this);

        botaoacessar = (Button) findViewById(R.id.btnAcessar);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastre_se);

        botaoacessar.setTypeface(raleway);
        botaoCadastrar.setTypeface(raleway);

        botaoacessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cpf = (EditText) findViewById(R.id.edtCpf);
                senha = (EditText) findViewById(R.id.edtSenha);

                JSONObject dadosJson = new JSONObject();
                try {
                    dadosJson.put("cpf", cpf.getText().toString());
                    dadosJson.put("cpf", cpf.getText().toString());
                    dadosJson.put("senha", Utilitarios.md5(senha.getText().toString()));
                    dadosJson.put("method", "app-get-login");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dadosJson.toString();
                //Log.d("Dados que vão", dadosJson.toString());

                final JsonObjectRequest json = new JsonObjectRequest(Request.Method.POST,
                        "http://easypasse.com.br/gestao/wsLogin.php", dadosJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Retorno", response.toString());

                                try {
                                    String msgAlerta = response.getString("msg");
                                    Toast.makeText(LogarCadastrarActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("TAG", "ERRO!!: " + error);
                            }
                        });

                mVolleyRequest.add(json);

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
