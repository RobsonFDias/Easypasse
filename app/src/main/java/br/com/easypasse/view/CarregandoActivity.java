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

public class CarregandoActivity extends AppCompatActivity {

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


    }

}
