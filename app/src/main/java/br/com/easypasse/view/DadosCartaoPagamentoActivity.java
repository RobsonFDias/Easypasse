package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import br.com.easypasse.controller.CartaoPagamentoControle;
import br.com.easypasse.controller.FormaPagamentoControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.CartaoPagamentoModelo;
import br.com.easypasse.model.FormaPagamentoModelo;
import br.com.easypasse.utils.ObjetosTransitantes;
import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class DadosCartaoPagamentoActivity extends AppCompatActivity {


    private ProgressDialog pDialog;
    private String retorno, msgAlerta;
    private RequestQueue mVolleyRequest;
    private Spinner spFormaPagamento, spBandeiraCartao, spBancos;
    private String spnFormaPagamento = "Selecione", spnBandeiraCartao = "Selecione", spnBancos = "Selecione", voltar = "1";
    private AlertDialog alert;
    private List<CharSequence> listFormaPagamento, listBandeiraCartao, listBancos;
    private EditText edtDataValidade, edtNumeroCartao, edtCodigoSeguranca, edtNomeTitular, edtCpfTitular;
    private JSONArray jsonFormaPagamento, jsonBancos, jsonBandeiraCartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_pagamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVolleyRequest = Volley.newRequestQueue(this);

        edtCodigoSeguranca = (EditText) findViewById(R.id.edtCodigoSeguranca);
        edtNomeTitular = (EditText) findViewById(R.id.edtNomeTitular);
        edtCpfTitular = (EditText) findViewById(R.id.edtCpfTitular);

        edtNumeroCartao = (EditText) findViewById(R.id.edtNumeroCartao);
        MaskEditTextChangedListener maskNCartao = new MaskEditTextChangedListener("#### #### #### ####", edtNumeroCartao);
        edtNumeroCartao.addTextChangedListener(maskNCartao);

        edtDataValidade = (EditText) findViewById(R.id.edtDataValidade);
        MaskEditTextChangedListener maskDate = new MaskEditTextChangedListener("##/####", edtDataValidade);
        edtDataValidade.addTextChangedListener(maskDate);

        final LinearLayout llCartao = (LinearLayout) findViewById(R.id.llCartao);
        final LinearLayout llBoleto = (LinearLayout) findViewById(R.id.llBoleto);
        final LinearLayout llBanco = (LinearLayout) findViewById(R.id.llBanco);
        final Button btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos()) {

                }
            }
        });

        spFormaPagamento = (Spinner) findViewById(R.id.spFormaPagamento);
        spFormaPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnFormaPagamento = parent.getItemAtPosition(position).toString();
                if (spnFormaPagamento.equals("CARTÃO DE CRÉDITO")) {
                    llCartao.setVisibility(View.VISIBLE);
                    llBoleto.setVisibility(View.GONE);
                    llBanco.setVisibility(View.GONE);
                    btnSalvar.setVisibility(View.VISIBLE);
                } else if (spnFormaPagamento.equals("CARTÃO DE DÉBITO")) {
                    llCartao.setVisibility(View.VISIBLE);
                    llBanco.setVisibility(View.VISIBLE);
                    llBoleto.setVisibility(View.GONE);
                    btnSalvar.setVisibility(View.VISIBLE);
                } else {
                    llCartao.setVisibility(View.GONE);
                    llBoleto.setVisibility(View.GONE);
                    llBanco.setVisibility(View.GONE);
                    btnSalvar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBandeiraCartao = (Spinner) findViewById(R.id.spBandeiraCartao);
        spBandeiraCartao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnBandeiraCartao = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spBancos = (Spinner) findViewById(R.id.spBancos);
        spBancos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnBancos = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buscarFormaPagamento();
        buscarBandeiraCartao();
        buscarBanco();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DadosCartaoPagamentoActivity.this, MainActivity.class));
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
        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("method", "app-get-formapagamento");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_FORMA_PAGAMENTO, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("json", response.toString());
                    try {
                        retorno = response.getString("isThereMore");

                        if (retorno.equals("false")) {
                            jsonFormaPagamento = new JSONArray(response.getString("formapagamento"));
                            preecnherFormaPagamento(jsonFormaPagamento);
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

    private void buscarBandeiraCartao() {
        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("method", "app-get-bandeiras");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_BANDEIRA_CARTAO, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
                    try {
                        retorno = response.getString("isThereMore");

                        if (retorno.equals("false")) {
                            jsonBandeiraCartao = new JSONArray(response.getString("bandeiras"));
                            preecnherBandeiraCartao(jsonBandeiraCartao);
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

    private void buscarBanco() {
        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("method", "app-get-bancos");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_BANCOS, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
                    try {
                        retorno = response.getString("isThereMore");

                        if (retorno.equals("false")) {
                            jsonBancos = new JSONArray(response.getString("bancos"));
                            preecnherBancos(jsonBancos);
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

    private void preecnherFormaPagamento(JSONArray jsonArray) {
        try {
            listFormaPagamento = new ArrayList<CharSequence>();
            listFormaPagamento.add(spnFormaPagamento);

            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listFormaPagamento.add(jsonObject.getString("descricao"));
            }

            ArrayAdapter adapterGenero = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listFormaPagamento);
            adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spFormaPagamento.setAdapter(adapterGenero);
            spFormaPagamento.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preecnherBandeiraCartao(JSONArray jsonArray) {
        try {
            listBandeiraCartao = new ArrayList<CharSequence>();
            listBandeiraCartao.add(spnBandeiraCartao);

            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listBandeiraCartao.add(jsonObject.getString("idFlag") + "-" + jsonObject.getString("strImagem"));
            }

            ArrayAdapter adapterGenero = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listBandeiraCartao);
            adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spBandeiraCartao.setAdapter(adapterGenero);
            spBandeiraCartao.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preecnherBancos(JSONArray jsonArray) {
        try {
            listBancos = new ArrayList<CharSequence>();
            listBancos.add(spnBancos);

            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                listBancos.add(jsonObject.getString("idBanco") + "-" + jsonObject.getString("descricao"));
            }

            ArrayAdapter adapterGenero = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listBancos);
            adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spBancos.setAdapter(adapterGenero);
            spBancos.setSelection(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validaCampos() {

        if (spnBandeiraCartao.toString().equals("Selecione")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Selecione uma opção!", Toast.LENGTH_LONG).show();
            spBandeiraCartao.setFocusable(true);
            return false;
        }

        if (spnFormaPagamento.equals("CARTÃO DE DÉBITO")) {
            if (spnBancos.toString().equals("Selecione")) {
                Toast.makeText(DadosCartaoPagamentoActivity.this, "Selecione uma opção!", Toast.LENGTH_LONG).show();
                spBancos.setFocusable(true);
                return false;
            }
        }

        if (edtDataValidade.getText().toString().equals("")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Campo data validade vazio!", Toast.LENGTH_LONG).show();
            edtDataValidade.setFocusable(true);
            return false;
        }

        if (edtNumeroCartao.getText().toString().equals("")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Campo Número cartão vazio!", Toast.LENGTH_LONG).show();
            edtNumeroCartao.setFocusable(true);
            return false;
        }

        if (edtCodigoSeguranca.getText().toString().equals("")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Campo Código Segurança vazio!", Toast.LENGTH_LONG).show();
            edtCodigoSeguranca.setFocusable(true);
            return false;
        }

        if (edtNomeTitular.getText().toString().equals("")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Campo Nome vazio!", Toast.LENGTH_LONG).show();
            edtNomeTitular.setFocusable(true);
            return false;
        }

        if (edtCpfTitular.getText().toString().equals("")) {
            Toast.makeText(DadosCartaoPagamentoActivity.this, "Campo cpf vazio!", Toast.LENGTH_LONG).show();
            edtCpfTitular.setFocusable(true);
            return false;
        }

        return true;
    }

    private void dialog() {
        try {
            pDialog = new ProgressDialog(DadosCartaoPagamentoActivity.this);
            pDialog.setMessage("Enviando dados ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enviarDadosCartao() {
        dialog();
        JSONObject dadosJsonObject = new JSONObject();
        try {
            String[] idBandeira = spnBandeiraCartao.split("-");
            String[] date = edtDataValidade.getText().toString().split("/");
            String[] idBanco = spnBancos.split("-");

            dadosJsonObject.put("idUsuario", ObjetosTransitantes.USUARIO_MODELO.getId());
            dadosJsonObject.put("nometitular", edtNomeTitular.getText());
            dadosJsonObject.put("numero", edtNumeroCartao.getText().toString().replace(" ", "").trim());
            dadosJsonObject.put("mes", date[0].trim());
            dadosJsonObject.put("ano", date[1].trim());
            dadosJsonObject.put("cvc", edtCodigoSeguranca.getText());
            dadosJsonObject.put("cpftitular", edtCpfTitular.getText());
            dadosJsonObject.put("idFlag", idBandeira[0].trim());
            if (spnFormaPagamento.equals("CARTÃO DE DÉBITO")) {
                dadosJsonObject.put("codigo", idBanco[0].trim());
            }
            dadosJsonObject.put("ativo", 1);
            dadosJsonObject.put("method", "app-set-cartaopagamento");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_CADASTRAR, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Log.d("json", response.toString());
                    try {
                        retorno = response.getString("error");
                        msgAlerta = response.getString("msg");
                        pDialog.dismiss();

                        if (retorno.equals("Sucesso")) {
                            salvarCartao(new JSONArray(response.getString("cartaopagamento")));

                        } else {
                            Toast.makeText(DadosCartaoPagamentoActivity.this, msgAlerta, Toast.LENGTH_LONG).show();
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

    private void salvarCartao(JSONArray jsonArray) {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getApplicationContext()));
            for (Integer i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CartaoPagamentoModelo cartaoPagamentoModelo = new CartaoPagamentoModelo();
                cartaoPagamentoModelo.setId(jsonObject.getInt("idCartaoPagamento"));
                cartaoPagamentoModelo.setNomeTitular(jsonObject.getString("nometitular"));
                cartaoPagamentoModelo.setNumero(jsonObject.getString("numero"));
                cartaoPagamentoModelo.setMes(jsonObject.getString("mes"));
                cartaoPagamentoModelo.setAno(jsonObject.getString("ano"));
                cartaoPagamentoModelo.setCpf(jsonObject.getString("cpftitular"));
                cartaoPagamentoModelo.setIdFlag(jsonObject.getInt("idFlag"));
                cartaoPagamentoModelo.setCodigoBanco(jsonObject.getInt("codigo"));
                cartaoPagamentoModelo.setAtivo(jsonObject.getString("ativo"));
                cartaoPagamentoModelo.setIdUsuario(ObjetosTransitantes.USUARIO_MODELO.getId());

                new CartaoPagamentoControle().inserirCartao(cartaoPagamentoModelo);

                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
