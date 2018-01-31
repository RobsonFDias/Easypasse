package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class CadastroActivity extends AppCompatActivity {

    private Button botaoSalvar;
    private EditText usuario;
    private EditText cpf;
    private EditText senha;
    private RequestQueue mVolleyRequest;
    private String msgAlerta, retorno;
    private JSONArray jsonArray;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVolleyRequest = Volley.newRequestQueue(this);

        usuario = (EditText) findViewById(R.id.edtUsuario);
        cpf = (EditText) findViewById(R.id.edtCpf);
        senha = (EditText) findViewById(R.id.edtSenha);
        botaoSalvar = (Button) findViewById(R.id.btnSalvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos()) {
                    cadastrarUsuario();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroActivity.this, LogarCadastrarActivity.class));
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

    private void dialog() {
        try {
            pDialog = new ProgressDialog(CadastroActivity.this);
            pDialog.setMessage("Enviando dados ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cadastrarUsuario() {
        dialog();

        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("nome", usuario.getText().toString());
            dadosJsonObject.put("cpf", cpf.getText().toString());
            dadosJsonObject.put("senha", Utilitarios.md5(senha.getText().toString()));
            dadosJsonObject.put("method", "app-set-usuario");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_CADASTRAR, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
                    try {
                        retorno = response.getString("error");
                        msgAlerta = response.getString("msg");

                        pDialog.dismiss();

                        if (retorno.equals("Sucesso")) {
                            jsonArray = new JSONArray(response.getString("usuario"));
                            salvarUsuario();
                            Intent principalIntent = new Intent(CadastroActivity.this, PrincipalActivity.class);
                            startActivity(principalIntent);
                        } else {
                            Toast.makeText(CadastroActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Erro ", error.toString());
                }
            });

            mVolleyRequest.add(dadosObjReq);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validaCampos() {
        if (usuario.getText().toString().equals("")) {
            Toast.makeText(CadastroActivity.this, "Campo Usuario vazio!", Toast.LENGTH_LONG).show();
            usuario.setFocusable(true);
            return false;
        }

        if (cpf.getText().toString().equals("")) {
            Toast.makeText(CadastroActivity.this, "Campo CPF vazio!", Toast.LENGTH_LONG).show();
            cpf.setFocusable(true);
            return false;
        }

        if (senha.getText().toString().equals("")) {
            Toast.makeText(CadastroActivity.this, "Campo Senha vazio!", Toast.LENGTH_LONG).show();
            senha.setFocusable(true);
            return false;
        }

        if (senha.getText().toString().length() < 4) {
            Toast.makeText(CadastroActivity.this, "Sua senha deve ter no minimo 4 caracteres!", Toast.LENGTH_LONG).show();
            senha.setFocusable(true);
            return false;
        }

        return true;
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

                new UsuarioControle().inserirUsuario(usuarioModelo);

                ObjetosTransitantes.USUARIO_MODELO = usuarioModelo;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
