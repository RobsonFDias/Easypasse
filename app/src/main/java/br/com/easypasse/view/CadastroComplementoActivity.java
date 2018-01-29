package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import br.com.easypasse.R;
import br.com.easypasse.config.EndPoints;
import br.com.easypasse.controller.UsuarioControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.UsuarioModelo;
import br.com.easypasse.utils.ObjetosTransitantes;

public class CadastroComplementoActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText telefone;
    private EditText cidade;
    private ProgressDialog pDialog;
    private String retorno, msgAlerta;
    private RequestQueue mVolleyRequest;
    private Spinner spSexo;
    private String spnGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_complemento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVolleyRequest = Volley.newRequestQueue(this);

        nome = (EditText) findViewById(R.id.edtNome);
        email = (EditText) findViewById(R.id.edtEmail);
        telefone = (EditText) findViewById(R.id.edtTelefone);
        cidade = (EditText) findViewById(R.id.edtCidade);
        spSexo = (Spinner) findViewById(R.id.spSexo);

        spSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnGenero = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnSalvarUsuario = (Button) findViewById(R.id.btnSalvar);
        btnSalvarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos()) {
                    enviarDadosUsuario();
                }
            }
        });

        preencheCampos();
        buscarGenero();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CadastroComplementoActivity.this, MainActivity.class));
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

    private void preencheCampos() {
        try {
            if (ObjetosTransitantes.USUARIO_MODELO != null) {
                nome.setText(ObjetosTransitantes.USUARIO_MODELO.getNome());
                telefone.setText(ObjetosTransitantes.USUARIO_MODELO.getTelefone());
                email.setText(ObjetosTransitantes.USUARIO_MODELO.getEmail());
                cidade.setText(ObjetosTransitantes.USUARIO_MODELO.getCidade());
                spnGenero = ObjetosTransitantes.USUARIO_MODELO.getSexo();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarGenero() {
        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("method", "app-get-genero");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_GENERO, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("json", response.toString());
                    try {
                        retorno = response.getString("isThereMore");

                        if (retorno.equals("false")) {
                            preecnherGenero(new JSONArray(response.getString("genero")));
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

    private void preecnherGenero(JSONArray jsonArray) {
        try {
            List<CharSequence> listGenero = new ArrayList<CharSequence>();
            if (spnGenero != null) {
                listGenero.add(spnGenero);
            }
            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listGenero.add(jsonObject.getString("idGenero") + "-" + jsonObject.getString("descricao"));
            }
            ArrayAdapter adapterGenero = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listGenero);
            adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spSexo.setAdapter(adapterGenero);
            spSexo.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dialog() {
        try {
            pDialog = new ProgressDialog(CadastroComplementoActivity.this);
            pDialog.setMessage("Enviando dados ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarDadosUsuario() {
        dialog();

        JSONObject dadosJsonObject = new JSONObject();
        try {
            String[] idGenero = spnGenero.split("-");
            dadosJsonObject.put("idUsuario", ObjetosTransitantes.USUARIO_MODELO.getId());
            dadosJsonObject.put("nome", nome.getText().toString());
            dadosJsonObject.put("email", email.getText().toString());
            dadosJsonObject.put("telefone", telefone.getText().toString());
            dadosJsonObject.put("cidade", cidade.getText().toString());
            dadosJsonObject.put("genero", Integer.valueOf(idGenero[0].trim()));
            dadosJsonObject.put("cpf", ObjetosTransitantes.USUARIO_MODELO.getCpf().toString());
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
                            atualizarUsuario();
                            Intent principalIntent = new Intent(CadastroComplementoActivity.this, PrincipalActivity.class);
                            startActivity(principalIntent);
                            finish();
                        } else {
                            Toast.makeText(CadastroComplementoActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
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
        if (nome.getText().toString().equals("")) {
            Toast.makeText(CadastroComplementoActivity.this, "Campo Nome vazio", Toast.LENGTH_LONG).show();
            nome.setFocusable(true);
            return false;
        }
        if (email.getText().toString().equals("")) {
            Toast.makeText(CadastroComplementoActivity.this, "Campo e-mail vazio", Toast.LENGTH_LONG).show();
            email.setFocusable(true);
            return false;
        }
        if (telefone.getText().toString().equals("")) {
            Toast.makeText(CadastroComplementoActivity.this, "Campo telefone vazio", Toast.LENGTH_LONG).show();
            telefone.setFocusable(true);
            return false;
        }

        if (cidade.getText().toString().equals("")) {
            Toast.makeText(CadastroComplementoActivity.this, "Campo cidade vazio", Toast.LENGTH_LONG).show();
            cidade.setFocusable(true);
            return false;
        }

        return true;
    }

    private void atualizarUsuario() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

            UsuarioModelo usuarioModelo = new UsuarioModelo();
            usuarioModelo.setId(ObjetosTransitantes.USUARIO_MODELO.getId());
            usuarioModelo.setNome(nome.getText().toString());
            usuarioModelo.setEmail(email.getText().toString());
            usuarioModelo.setTelefone(telefone.getText().toString());
            usuarioModelo.setSexo(spnGenero);
            usuarioModelo.setCidade(cidade.getText().toString());
            usuarioModelo.setLogado(ObjetosTransitantes.USUARIO_MODELO.getLogado());
            usuarioModelo.setCpf(ObjetosTransitantes.USUARIO_MODELO.getCpf());

            new UsuarioControle().atualizarUsuario(usuarioModelo);

            ObjetosTransitantes.USUARIO_MODELO = usuarioModelo;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
