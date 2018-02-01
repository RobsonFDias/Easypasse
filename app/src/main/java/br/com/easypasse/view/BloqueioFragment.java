package br.com.easypasse.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import br.com.easypasse.controller.UsuarioControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.UsuarioModelo;
import br.com.easypasse.utils.ObjetosTransitantes;
import br.com.easypasse.utils.Utilitarios;

public class BloqueioFragment extends Fragment {

    private RequestQueue mVolleyRequest;
    private AlertDialog alert;
    private EditText edtCpf, edtSenha;
    private ProgressDialog pDialog;

    public BloqueioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bloqueio, container, false);

        mVolleyRequest = Volley.newRequestQueue(getActivity().getApplicationContext());

        RelativeLayout rlBloqueioConta = (RelativeLayout) view.findViewById(R.id.rlBloqueioConta);
        rlBloqueioConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBloqueioConta();
            }
        });

        return view;
    }

    protected void showBloqueioConta() {
        try {
            LayoutInflater layoutInflater = getLayoutInflater();
            View promptView = layoutInflater.inflate(R.layout.dialog_bloqueio, null);

            edtCpf = (EditText) promptView.findViewById(R.id.edtCpf);
            edtSenha = (EditText) promptView.findViewById(R.id.edtSenha);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setView(promptView);
            alert = alertDialogBuilder.create();
            alert.setCancelable(true);

            alert.show();

            Button btnEnviarSpan = (Button) promptView.findViewById(R.id.btnBloquear);
            btnEnviarSpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validaCampos()) {
                        bloquearConta();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean validaCampos() {
        try {
            if (edtCpf.getText().equals("")) {
                Toast.makeText(getActivity().getApplicationContext(), "Campo CPF vazio!", Toast.LENGTH_LONG).show();
                edtCpf.setFocusable(true);
                return false;
            } else if (edtSenha.getText().equals("")) {
                Toast.makeText(getActivity().getApplicationContext(), "Campo SENHA vazio!", Toast.LENGTH_LONG).show();
                edtSenha.setFocusable(true);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void dialog() {
        try {
            pDialog = new ProgressDialog(getActivity().getApplicationContext());
            pDialog.setMessage("Enviando dados ......");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bloquearConta() {
        dialog();

        JSONObject dadosJsonObject = new JSONObject();
        try {
            dadosJsonObject.put("cpf", edtCpf.getText().toString());
            dadosJsonObject.put("senha", Utilitarios.md5(edtSenha.getText().toString()));
            dadosJsonObject.put("method", "app-set-bloqueioconta");
            dadosJsonObject.toString();

            JsonObjectRequest dadosObjReq = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_BLOQUEIO_CONTA, dadosJsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("json", response.toString());
                    try {
                        String retorno = response.getString("error");
                        String msgAlerta = response.getString("msg");

                        pDialog.dismiss();

                        if (retorno.equals("Sucesso")) {
                            alert.dismiss();
                            logout();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), msgAlerta, Toast.LENGTH_LONG).show();
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

    private void logout() {
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getActivity().getApplicationContext()));
            UsuarioModelo usuarioModelo = new UsuarioControle().buscarUsuarioId(ObjetosTransitantes.USUARIO_MODELO.getId());
            if (usuarioModelo != null) {
                usuarioModelo.setLogado("0");
                new UsuarioControle().atualizarUsuario(usuarioModelo);
            }

            startActivity(new Intent(getActivity(), LogarCadastrarActivity.class));
            getActivity().finish();

            ObjetosTransitantes.USUARIO_MODELO = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
