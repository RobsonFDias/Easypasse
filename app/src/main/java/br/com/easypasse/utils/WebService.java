package br.com.easypasse.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Jargs on 19/06/2015.
 */
public class WebService {
    private static final String TAG = WebService.class.getSimpleName();

    public WebService() {
    }

    public String obterPosicoesDeOnibus(String linhaDeOnibus, String url) {

        String retorno = "";

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        InputStream inputStream = null;

        try {
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpGet httpget = new HttpGet(url + linhaDeOnibus);

            httpget.setHeader("Content-Type", "application/json");
            httpget.setHeader("Accept", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpget);
            inputStream = httpResponse.getEntity().getContent();

            String resultado = "";

            try {
                resultado = convertInputStreamToString(inputStream);

                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    retorno = resultado;
                } else {
                    try {
                        JSONObject jObj = new JSONObject(resultado);
                        JSONArray dados = null;

                        try {
                            dados = jObj.getJSONArray("DATA");
                            String msg = dados.get(0).toString().replace("[", "");
                            msg = msg.replace("]", "");
                            msg = msg.replace("\"", "");

                            Log.i("consumidorRest", "Ocorreu um erro ao consumir o serviço: " + msg);

                        } catch (Exception e) {
                            Log.i("consumidorREST", "Erro 1:" + e.getMessage() + " " + e.getLocalizedMessage());
                        }
                    } catch (Exception f) {
                        Log.i("consumidorREST", "Erro 2:" + f.getMessage() + " " + f.getLocalizedMessage());
                    }

                }
            } catch (Exception g) {
                Log.i("consumidorREST", "Erro 3:" + g.getMessage() + " " + g.getLocalizedMessage());
            }
        } catch (Exception h) {
            Log.i("consumidorREST", "Erro 4:" + h.getMessage() + " " + h.getLocalizedMessage());
        }

        return retorno;

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "ISO-8859-1"));

        int c;
        StringBuilder response = new StringBuilder();
        while ((c = bufferedReader.read()) != -1) {
            response.append((char) c);
        }
        String result = response.toString();
        inputStream.close();

        return result;
    }
}