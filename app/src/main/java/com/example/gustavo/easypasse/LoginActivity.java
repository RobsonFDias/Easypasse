package com.example.gustavo.easypasse;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void acessar(View view) throws JSONException {
        EditText username = (EditText) findViewById(R.id.edtCpf);
        EditText senha = (EditText) findViewById(R.id.edtSenha);
//        Toast.makeText(this, "CPF "+username.getText().toString()+
//                " senha "+senha.getText().toString(), Toast.LENGTH_LONG).show();

        JSONObject dadosJsonObject = new JSONObject();
        dadosJsonObject.put("username", username.getText().toString());
        dadosJsonObject.put("senha", Utilitarios.md5(senha.getText().toString()));
        dadosJsonObject.put("method", "app-get-login" );
        dadosJsonObject.toString();
        Log.d("Dados enviados", dadosJsonObject.toString());

        final JsonObjectRequest json = new JsonObjectRequest(Request.Method.POST,
                "http://easypasse.com.br/gestao/wsLogin.php", dadosJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erro", error.toString());
            }
        });

//        Intent intentPrincipal = new Intent(LoginActivity.this, PrincipalActivity.class);
//        startActivity(intentPrincipal);
    }
}
