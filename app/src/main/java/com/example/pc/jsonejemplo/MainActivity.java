package com.example.pc.jsonejemplo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*
    Variables globales
     */
    ListView lista;
    ArrayAdapter adaptador;
    HttpURLConnection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista= (ListView) findViewById(R.id.listaAnimales);

        /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new JsonTask().
                        execute(
                                new URL("http://servidorexterno.site90.com/datos/get_all_animals.php"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public class JsonTask extends AsyncTask<URL, Void, List<Animal>>{

        @Override
        protected List<Animal> doInBackground(URL... urls) {
            List<Animal> animales = null;

            try {

                // Establecer la conexión
                con = (HttpURLConnection)urls[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                // Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if(statusCode!=200) {
                    animales = new ArrayList<>();
                    animales.add(new Animal("Error",null,null));

                } else {

                    // Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    // JsonAnimalParser parser = new JsonAnimalParser();
                    GsonAnimalParser parser = new GsonAnimalParser();

                    animales = parser.leerFlujoJson(in);


                }

            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                con.disconnect();
            }
            return animales;
        }

        @Override
        protected void onPostExecute(List<Animal> animales) {
            /*
            Asignar los objetos de Json parseados al adaptador
             */
            if(animales!=null) {
                adaptador = new AdaptadorDeAnimales(getBaseContext(), animales);
                lista.setAdapter(adaptador);
            }else{
                Toast.makeText(
                        getBaseContext(),
                        "Ocurrió un error de Parsing Json",
                        Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }
}
