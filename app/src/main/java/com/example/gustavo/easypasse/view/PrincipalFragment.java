package com.example.gustavo.easypasse.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gustavo.easypasse.R;


public class PrincipalFragment extends Fragment {
    private Typeface raleway;
    private TextView txtSaldo, txtSaldoUsuario;
    private Button btnFazerRecarga, btnTransferirRecarga, btnPagarPassagem;

    public PrincipalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_principal, container, false);

        txtSaldo = (TextView) rootView.findViewById(R.id.txtSaldo);
        txtSaldoUsuario = (TextView) rootView.findViewById(R.id.txtSaldoUsuario);
        btnFazerRecarga = (Button) rootView.findViewById(R.id.btnFazerRecarga);
        btnTransferirRecarga = (Button) rootView.findViewById(R.id.btnTransferirRecarga);
        btnPagarPassagem = (Button) rootView.findViewById(R.id.btnPagarPassagem);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
