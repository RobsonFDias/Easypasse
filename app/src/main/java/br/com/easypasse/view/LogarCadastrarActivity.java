package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import br.com.easypasse.R;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.easypasse.config.EndPoints;
import br.com.easypasse.utils.Utilitarios;

public class LogarCadastrarActivity extends AppCompatActivity {

    private Button botaoacessar;
    private Button botaoCadastrar;

    private EditText cpf;
    private EditText senha;
    private RequestQueue mVolleyRequest;
    private String msgAlerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVolleyRequest = Volley.newRequestQueue(this);

        cpf = (EditText) findViewById(R.id.edtCpf);
        senha = (EditText) findViewById(R.id.edtSenha);
        botaoacessar = (Button) findViewById(R.id.btnAcessar);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastre_se);

        botaoacessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FazerLogin().execute();
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

    private class FazerLogin extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LogarCadastrarActivity.this);
            pDialog.setMessage("Fazendo login ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            fazerLogin();

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            Toast.makeText(LogarCadastrarActivity.this, msgAlerta, Toast.LENGTH_LONG).show();

            Intent principalIntent = new Intent(LogarCadastrarActivity.this, PrincipalActivity.class);
            startActivity(principalIntent);
        }
    }

    private JSONObject fazerLogin() {
        final JSONObject retornoJson = new JSONObject();
        try {
            final JSONObject dadosJson = new JSONObject();
            dadosJson.put("cpf", cpf.getText().toString());
            dadosJson.put("cpf", cpf.getText().toString());
            dadosJson.put("senha", Utilitarios.md5(senha.getText().toString()));
            dadosJson.put("method", "app-get-login");

            dadosJson.toString();

            final JsonObjectRequest json = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_LOGIN, dadosJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Retorno", response.toString());
                            try {
                                msgAlerta = response.getString("msg");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
