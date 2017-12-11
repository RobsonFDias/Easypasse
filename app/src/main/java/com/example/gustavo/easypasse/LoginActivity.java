package com.example.gustavo.easypasse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText cpf;
    private EditText senha;
    private RequestQueue mVolleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Button acessar = (Button) findViewById(R.id.btnAcessar);
        mVolleyRequest = Volley.newRequestQueue(this);
        
    }


    public void cadastro(View view) {
        Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(cadastro);
    }


    public void acessar(View view) throws JSONException {

        cpf = (EditText) findViewById(R.id.edtCpf);
        senha = (EditText) findViewById(R.id.edtSenha);

        JSONObject dadosJson = new JSONObject();
        dadosJson.put("cpf", cpf.getText().toString());
        dadosJson.put("senha", Utilitarios.md5(senha.getText().toString()));
        dadosJson.put("method", "app-get-login");
        dadosJson.toString();
        //Log.d("Dados que vão", dadosJson.toString());
        //192.168.0.1

        final JsonObjectRequest json = new JsonObjectRequest(Request.Method.POST,
                "http://easypasse.com.br/gestao/wsLogin.php", dadosJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Retorno", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "ERRO!!: " + error);
                    }
        });

        mVolleyRequest.add(json);
    }
}
