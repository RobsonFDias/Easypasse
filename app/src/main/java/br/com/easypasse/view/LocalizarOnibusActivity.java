package br.com.easypasse.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import br.com.easypasse.R;
import br.com.easypasse.config.EndPoints;
import br.com.easypasse.model.PosicaoOnibus;
import br.com.easypasse.utils.WebService;

public class LocalizarOnibusActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean isGPSEnabled;
    private static final int REQUEST_CODE_LOCATION = 2;
    private LocationManager locationManager;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private ArrayList<PosicaoOnibus> jsonPosicoes;
    private ProgressDialog pDialog;
    private EditText etLinha;
    private String linha;
    private int cont = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar_onibus);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        etLinha = (EditText) findViewById(R.id.etLinha);
        Button btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etLinha.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Favor preencher o campo Cod. Linha !", Toast.LENGTH_LONG).show();
                } else {
                    linha = etLinha.getText().toString();
                    new BuscarOnibus().execute();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);

        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        int cont = 0;
        for (PosicaoOnibus posicao : jsonPosicoes) {
            /*
                dateString = formatter.format(posicao.getDataHora());

                Log.i("consumidorREST", "Data/Hora  : " + dateString + "");
                Log.i("consumidorREST", "Veículo    : " + posicao.getOrdem() + "");
                Log.i("consumidorREST", "Linha      : " + posicao.getLinha() + "");
                Log.i("consumidorREST", "Latitude   : " + posicao.getLatitude() + "");
                Log.i("consumidorREST", "Longitude  : " + posicao.getLongitude() + "");
                Log.i("consumidorREST", "Velocidade : " + posicao.getVelocidade() + "");
                Log.i("consumidorREST", "Direção    : " + posicao.getDirecao() + "");
            */

            LatLng latLngOnibus = new LatLng(posicao.getLatitude(), posicao.getLongitude());
            if (cont == 0) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOnibus, 15));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLngOnibus)
                        .zoom(15)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            int icOnibus = (posicao.getVelocidade() == 0 ? R.drawable.ic_onibus : R.drawable.ic_onibus_movimento);
            mMap.addMarker(new MarkerOptions()
                    .position(latLngOnibus)
                    .title(posicao.getOrdem()))
                    .setIcon(BitmapDescriptorFactory.fromResource(icOnibus));

            cont++;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LocalizarOnibusActivity.this, MainActivity.class));
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // success!
            } else {
                // Permission was denied or request was cancelled
            }
        }
    }

    public void verificaGPS() {
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Alerta");
            alertDialogBuilder
                    .setMessage("Deseja Ligar Sua Localização?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            mapFragment.getMapAsync(this);
        }
    }

    private class BuscarOnibus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (cont == 0) {
                pDialog = new ProgressDialog(LocalizarOnibusActivity.this);
                pDialog.setMessage("Buscando Onibus ......");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                jsonPosicoes = extrairPosicoesDeOnibus(new WebService().obterPosicoesDeOnibus("/" + linha, EndPoints.URL_OBTER_POSICOES_ONIBUS));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (cont == 0) {
                pDialog.dismiss();
                timer();
            }

            if (jsonPosicoes.size() > 0) {
                verificaGPS();
            } else {
                cont = 0;
                if (mMap != null) {
                    mMap.clear();
                }
                timer.cancel();
                Toast.makeText(getApplicationContext(), "Dados não encontrado!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<PosicaoOnibus> extrairPosicoesDeOnibus(String jsonString) {
        ArrayList<PosicaoOnibus> listaDePosicoesDeOnibus = new ArrayList<PosicaoOnibus>();
        PosicaoOnibus posicaoOnibusDTO;
        JSONObject jObj;
        try {
            jObj = new JSONObject(jsonString);

            JSONArray colunas = jObj.getJSONArray("COLUMNS");
            JSONArray posicoesOnibus = jObj.getJSONArray("DATA");

            JSONArray posicaoOnibus;

            String dateString;
            SimpleDateFormat formatter;

            String direcao = "";

            for (int i = 0; i < posicoesOnibus.length(); i++) {
                try {
                    posicaoOnibus = posicoesOnibus.getJSONArray(i);
                    posicaoOnibusDTO = new PosicaoOnibus();

                    dateString = (String) posicaoOnibus.getString(0);
                    formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US);
                    posicaoOnibusDTO.setDataHora(formatter.parse(dateString));
                    posicaoOnibusDTO.setOrdem(posicaoOnibus.getString(1));
                    posicaoOnibusDTO.setLinha(posicaoOnibus.getString(2).replace(".0", ""));
                    posicaoOnibusDTO.setLatitude(Double.valueOf(posicaoOnibus.getString(3)));
                    posicaoOnibusDTO.setLongitude(Double.valueOf(posicaoOnibus.getString(4)));
                    posicaoOnibusDTO.setVelocidade(Double.parseDouble(posicaoOnibus.getString(5)));
                    direcao = ((posicaoOnibus.getString(6)).length() == 0) ? "-1" : posicaoOnibus.getString(6);
                    direcao = direcao.replace(".0", "");
                    posicaoOnibusDTO.setDirecao(Integer.parseInt(direcao));

                    listaDePosicoesDeOnibus.add(posicaoOnibusDTO);

                } catch (Exception e) {
                    Log.i("consumidorREST", "Erro 5:" + e.getMessage());
                }
            }
        } catch (JSONException f) {
            Log.i("consumidorREST", "Erro 6:" + f.getMessage());
        }

        return listaDePosicoesDeOnibus;
    }

    private void timer() {
        try {
            int delay = 5000;   // delay de 5 seg.
            int interval = 3000;  // intervalo de 1 seg.
            timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    if (!linha.equals("")) {
                        cont = 1;
                        new BuscarOnibus().execute();
                    }
                }
            }, delay, interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
