package br.com.easypasse.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.easypasse.R;
import br.com.easypasse.config.EndPoints;

public class CadastroComplementoActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText telefone;
    private EditText cidade;
    private Spinner sexo;
    private RequestQueue mVolleyRequest;
    private Button botaoAtualizarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_complemento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mVolleyRequest = Volley.newRequestQueue(this);

        nome = (EditText) findViewById(R.id.edtNome);
        email = (EditText) findViewById(R.id.edtEmail);
        telefone = (EditText) findViewById(R.id.edtTelefone);
        cidade = (EditText) findViewById(R.id.edtCidade);
        sexo = (Spinner) findViewById(R.id.spSexo);

        getSexo();

        botaoAtualizarUsuario = (Button) findViewById(R.id.btnAtualizarUsuario);



    }

    private void getSexo(){

        JSONObject sexoJsonObject = new JSONObject();
        try {
            sexoJsonObject.put("method", "app-get-genero");
            sexoJsonObject.toString();

            JsonObjectRequest dadoObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_GENERO, sexoJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
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

    private void atualizarUsuario(){

        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("nome", nome.getText().toString());
            dadosJsonObject.put("email", email.getText().toString());
            dadosJsonObject.put("telefone", telefone.getText().toString());
            dadosJsonObject.put("cidade", cidade.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
