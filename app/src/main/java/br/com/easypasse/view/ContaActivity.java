package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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

import br.com.easypasse.R;
import br.com.easypasse.config.EndPoints;
import br.com.easypasse.controller.CartaoPagamentoControle;
import br.com.easypasse.controller.FormaPagamentoControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.CartaoPagamentoModelo;
import br.com.easypasse.model.FormaPagamentoModelo;
import br.com.easypasse.utils.ObjetosTransitantes;

public class ContaActivity extends AppCompatActivity {

    private TextView txtCartao, txtMaskCartao;
    private CartaoPagamentoModelo cartaoModelo;
    private RequestQueue mVolleyRequest;
    private ProgressDialog pDialog;
    private RelativeLayout rlAdicionarCartao, rlCartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVolleyRequest = Volley.newRequestQueue(this);

        txtMaskCartao = (TextView) findViewById(R.id.txtMaskCartao);
        txtCartao = (TextView) findViewById(R.id.txtCartao);

        RelativeLayout rlMinhaConta = (RelativeLayout) findViewById(R.id.rlMinhaConta);
        rlMinhaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContaActivity.this, CadastroComplementoActivity.class));
                finish();
            }
        });

        rlAdicionarCartao = (RelativeLayout) findViewById(R.id.rlAdicionarCartao);
        rlAdicionarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FormaPagamentoActivity.class).putExtra("voltar", "0"));
                finish();
            }
        });

        rlCartao = (RelativeLayout) findViewById(R.id.rlCartao);
        rlCartao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialogExclusao();
                return true;
            }
        });

        buscarFormaPagamento();
        buscarCartao();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContaActivity.this, ConfiguracaoActivity.class));
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

    private void buscarFormaPagamento() {
        FormaPagamentoModelo formaPagamentoModelo = null;
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));

            formaPagamentoModelo = new FormaPagamentoControle().buscarFormaPagamentoPeloUsuario(ObjetosTransitantes.USUARIO_MODELO.getId());
            if (formaPagamentoModelo == null) {
                rlAdicionarCartao.setVisibility(View.VISIBLE);
                rlCartao.setVisibility(View.GONE);
            } else {
                rlCartao.setVisibility(View.VISIBLE);
                rlAdicionarCartao.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarCartao() {
        try {
            cartaoModelo = new CartaoPagamentoControle().buscarCartaoPeloUsuario(ObjetosTransitantes.USUARIO_MODELO.getId());
            if (cartaoModelo != null) {
                String maskedCartao = "";
                for (int i = 0; i < cartaoModelo.getNumero().length() - 4; i++) {
                    maskedCartao += "X";
                }
                txtMaskCartao.setText(maskedCartao);
                txtCartao.setText(cartaoModelo.getNumero().substring(cartaoModelo.getNumero().length() - 4, cartaoModelo.getNumero().length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dialogExclusao() {
        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ContaActivity.this);
            builder1.setMessage("Deseja realmente excluir a forma pagamento ?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Sim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deletarCartaoServidor();
                        }
                    });

            builder1.setNegativeButton(
                    "Não",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dialog() {
        try {
            pDialog = new ProgressDialog(ContaActivity.this);
            pDialog.setMessage("Excluindo Cartão ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletarCartaoServidor() {
        dialog();
        JSONObject dadosJsonObject = new JSONObject();
        try {

            dadosJsonObject.put("idCartaoPagamento", cartaoModelo.getId());
            dadosJsonObject.put("method", "app-delete-cartaopagamento");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_CADASTRAR, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
                    try {
                        String retorno = response.getString("error");
                        String msgAlerta = response.getString("msg");

                        pDialog.dismiss();

                        if (retorno.equals("Sucesso")) {
                            deletarCartao();
                            deletarFormaPagamento();
                        } else {
                            Toast.makeText(ContaActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
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

    private void deletarCartao() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
            new CartaoPagamentoControle().deletarCartao(ObjetosTransitantes.USUARIO_MODELO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletarFormaPagamento() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
            new FormaPagamentoControle().deletarFormaPagamento(ObjetosTransitantes.USUARIO_MODELO.getId());
            buscarFormaPagamento();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
