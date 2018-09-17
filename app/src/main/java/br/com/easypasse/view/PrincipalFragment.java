package br.com.easypasse.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.com.easypasse.R;
import br.com.easypasse.controller.CartaoPagamentoControle;
import br.com.easypasse.controller.FormaPagamentoControle;
import br.com.easypasse.dao.DatabaseHelper;
import br.com.easypasse.dao.DatabaseManager;
import br.com.easypasse.model.CartaoPagamentoModelo;
import br.com.easypasse.model.FormaPagamentoModelo;
import br.com.easypasse.utils.ObjetosTransitantes;


public class PrincipalFragment extends Fragment {
    private Typeface raleway;
    private TextView txtSaldo, txtSaldoUsuario, txtCartao, txtMaskCartao, txtQuantidadePassagem;
    private Button btnFazerRecarga, btnTransferirRecarga, btnPagarPassagem;
    private AlertDialog alert;
    private CartaoPagamentoModelo cartaoModelo;
    private RadioButton rbUm, rbDois, rbTres;

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);

        txtSaldo = (TextView) rootView.findViewById(R.id.txtSaldo);
        txtSaldoUsuario = (TextView) rootView.findViewById(R.id.txtSaldoUsuario);

        btnFazerRecarga = (Button) rootView.findViewById(R.id.btnFazerRecarga);
        btnFazerRecarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCompraCreditos(0);
            }
        });


        btnPagarPassagem = (Button) rootView.findViewById(R.id.btnPagarPassagem);
        btnPagarPassagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), PrincipalActivity.class));
                getActivity().finish();
            }
        });

        btnTransferirRecarga = (Button) rootView.findViewById(R.id.btnTransferirRecarga);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private Boolean buscarFormaPagamento() {
        FormaPagamentoModelo formaPagamentoModelo = null;
        try {
            DatabaseManager.initializeInstance(new DatabaseHelper(getActivity().getApplicationContext()));

            formaPagamentoModelo = new FormaPagamentoControle().buscarFormaPagamentoPeloUsuario(ObjetosTransitantes.USUARIO_MODELO.getId());
            if (formaPagamentoModelo == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    protected void abrirCompraCreditos(int animation) {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_compra_passagem, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);

        alert = alertDialogBuilder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogThemeLeft;
        alert.show();

        rbUm = (RadioButton) promptView.findViewById(R.id.rbUm);
        rbDois = (RadioButton) promptView.findViewById(R.id.rbDois);
        rbTres = (RadioButton) promptView.findViewById(R.id.rbTres);

        RelativeLayout rlUm = (RelativeLayout) promptView.findViewById(R.id.rlUm);
        rlUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarRadioButon(1);
            }
        });

        RelativeLayout rlDois = (RelativeLayout) promptView.findViewById(R.id.rlDois);
        rlDois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarRadioButon(2);
            }
        });

        RelativeLayout rlTres = (RelativeLayout) promptView.findViewById(R.id.rlTres);
        rlTres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarRadioButon(3);
            }
        });
        Button btnProsseguir = (Button) promptView.findViewById(R.id.btnProsseguir);
        btnProsseguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                if(buscarFormaPagamento()) {
                    abrirEscolhaCartao();
                }else{
                    startActivity(new Intent(getActivity(),DadosCartaoPagamentoActivity.class));
                }
            }
        });

        Button btnSair = (Button) promptView.findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    protected void abrirEscolhaCartao() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.dialog_selecionar_cartao, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(true);

        alert = alertDialogBuilder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogThemeLeft;
        alert.show();

        RelativeLayout rlAdicionarCartao = (RelativeLayout) promptView.findViewById(R.id.rlAdicionarCartao);
        rlAdicionarCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),DadosCartaoPagamentoActivity.class));
            }
        });

        RelativeLayout rlCartao = (RelativeLayout) promptView.findViewById(R.id.rlCartao);
        rlCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtMaskCartao = (TextView) promptView.findViewById(R.id.txtMaskCartao);
        txtCartao = (TextView) promptView.findViewById(R.id.txtCartao);
        txtQuantidadePassagem = (TextView) promptView.findViewById(R.id.txtQuantidadePassagem);

        if (rbUm.isChecked()) {
            txtQuantidadePassagem.setText("10 Passagens X de R$ 36,00");
        } else if (rbDois.isChecked()) {
            txtQuantidadePassagem.setText("20 Passagens X de R$ 72,99");
        } else if (rbTres.isChecked()) {
            txtQuantidadePassagem.setText("30 Passagens X de R$ 108,99");
        }

        Button btnVoltar = (Button) promptView.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                abrirCompraCreditos(1);
            }
        });

        buscarCartao();
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

    private void selecionarRadioButon(Integer checked) {
        try {
            if (checked == 1) {
                rbUm.setChecked(true);
                rbDois.setChecked(false);
                rbTres.setChecked(false);
            } else if (checked == 2) {
                rbDois.setChecked(true);
                rbUm.setChecked(false);
                rbTres.setChecked(false);
            } else if (checked == 3) {
                rbTres.setChecked(true);
                rbUm.setChecked(false);
                rbDois.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
