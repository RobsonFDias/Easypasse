package br.com.easypasse.view;

import android.app.ProgressDialog;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.easypasse.R;
import br.com.easypasse.config.EndPoints;
import br.com.easypasse.controller.UsuarioControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.UsuarioModelo;
import br.com.easypasse.utils.ObjetosTransitantes;
import br.com.easypasse.utils.Utilitarios;

public class LogarCadastrarActivity extends AppCompatActivity {

    private Button botaoacessar;
    private Button botaoCadastrar;

    private EditText cpf;
    private EditText senha;
    private RequestQueue mVolleyRequest;
    private String msgAlerta, retorno;
    private JSONArray jsonArray;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_cadastrar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (verificaUsuarioLogado()) {
            abrirTelaPrincipal();
        }

        mVolleyRequest = Volley.newRequestQueue(this);

        cpf = (EditText) findViewById(R.id.edtCpf);
        senha = (EditText) findViewById(R.id.edtSenha);
        botaoacessar = (Button) findViewById(R.id.btnAcessar);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastre_se);

        botaoacessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos()) {
                    fazerLogin();
                }
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastrarIntent = new Intent(LogarCadastrarActivity.this, CadastroActivity.class);
                startActivity(cadastrarIntent);
                finish();
            }
        });

    }

    private boolean validaCampos() {
        if (cpf.getText().toString().equals("")) {
            Toast.makeText(LogarCadastrarActivity.this, "Campo CPF vazio", Toast.LENGTH_LONG).show();
            return false;
        }
        if (senha.getText().toString().equals("")) {
            Toast.makeText(LogarCadastrarActivity.this, "Campo Senha vazio", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void dialog() {
        try {
            pDialog = new ProgressDialog(LogarCadastrarActivity.this);
            pDialog.setMessage("Enviando dados ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject fazerLogin() {
        dialog();
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
                            try {

                                retorno = response.getString("error");
                                msgAlerta = response.getString("msg");
                                pDialog.dismiss();

                                if (retorno.equals("Sucesso")) {
                                    jsonArray = new JSONArray(response.getString("usuario"));
                                    salvarUsuario();
                                    abrirTelaPrincipal();
                                } else {
                                    Toast.makeText(LogarCadastrarActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
                                }
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

    private void salvarUsuario() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                UsuarioModelo usuarioModelo = new UsuarioModelo();
                usuarioModelo.setId(jsonObject.getInt("idUsuario"));
                usuarioModelo.setNome(jsonObject.getString("nome"));
                usuarioModelo.setLogado(jsonObject.getString("logado"));
                usuarioModelo.setCpf(jsonObject.getString("cpf"));

                UsuarioModelo usuarioModelo1 = new UsuarioControle().buscarUsuarioId(usuarioModelo.getId());
                if (usuarioModelo1 == null) {
                    new UsuarioControle().inserirUsuario(usuarioModelo);
                } else {
                    new UsuarioControle().atualizarUsuario(usuarioModelo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaPrincipal() {
        try {
            Intent principalIntent = new Intent(LogarCadastrarActivity.this, PrincipalActivity.class);
            startActivity(principalIntent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean verificaUsuarioLogado() {
        DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

        UsuarioModelo usuarioModelo = null;
        try {
            usuarioModelo = new UsuarioControle().buscarUsuarioLogado();
            if (usuarioModelo != null) {
                ObjetosTransitantes.USUARIO_MODELO = usuarioModelo;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
