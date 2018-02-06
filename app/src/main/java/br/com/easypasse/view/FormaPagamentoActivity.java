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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

public class FormaPagamentoActivity extends AppCompatActivity {


    private ProgressDialog pDialog;
    private String retorno, msgAlerta;
    private RequestQueue mVolleyRequest;
    private Spinner spFormaPagamento;
    private String spnFormaPagamento;
    private AlertDialog alert;
    private List<CharSequence> listFormaPagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_pagamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVolleyRequest = Volley.newRequestQueue(this);

        final LinearLayout llCartao = (LinearLayout) findViewById(R.id.llCartao);
        final LinearLayout llBoleto = (LinearLayout) findViewById(R.id.llBoleto);

        spFormaPagamento = (Spinner) findViewById(R.id.spFormaPagamento);
        spFormaPagamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spnFormaPagamento = parent.getItemAtPosition(position).toString();
                if (spnFormaPagamento.equals("CARTÃO DE CRÉDITO")) {
                    llCartao.setVisibility(View.VISIBLE);
                    llBoleto.setVisibility(View.GONE);
                } else if (spnFormaPagamento.equals("BOLETO")) {
                    showAviso();
                    llCartao.setVisibility(View.GONE);
                    llBoleto.setVisibility(View.VISIBLE);
                } else {
                    llBoleto.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buscarFormaPagamento();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FormaPagamentoActivity.this, MainActivity.class));
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
                            preecnherFormaPagamento(new JSONArray(response.getString("formapagamento")));
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
            if (spnFormaPagamento != null) {
                listFormaPagamento.add(spnFormaPagamento);
            }
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

    protected void showAviso() {
        LayoutInflater layoutInflater = LayoutInflater.from(FormaPagamentoActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_aviso, null);

        TextView txtTituloAviso = (TextView) promptView.findViewById(R.id.txtTituloAviso);
        txtTituloAviso.setText("Aviso");
        TextView txtMsgAviso = (TextView) promptView.findViewById(R.id.txtMsgAviso);
        txtMsgAviso.setText("Preencha o campo quantidade com valor minimo de R$ 30,00!");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FormaPagamentoActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);

        alert = alertDialogBuilder.create();
        alert.show();


        Button addUsuario = (Button) promptView.findViewById(R.id.btnEstouCiente);
        addUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }
}
