package br.com.easypasse.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.easypasse.R;
import br.com.easypasse.adapter.HistoricoAdapter;
import br.com.easypasse.model.HistoricoModelo;

public class HistoricoFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<CharSequence> adapter;

    public HistoricoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        Spinner spnMes = (Spinner) view.findViewById(R.id.spMes);
        adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.meses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMes.setAdapter(adapter);

        Spinner spnAno = (Spinner) view.findViewById(R.id.spAno);
        adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(), R.array.ano, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAno.setAdapter(adapter);

        ArrayList<HistoricoModelo> modelos = new ArrayList<>();
        modelos.add(new HistoricoModelo());

        listView = (ListView) view.findViewById(R.id.lvHistorico);
        listView.setAdapter(new HistoricoAdapter(getActivity().getApplicationContext(), modelos));

        return view;
    }
}
