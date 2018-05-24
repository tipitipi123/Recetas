package com.example.tipix1998.pruebaobteneringredientes;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText texto;
    ListView lv;
    WebServiceAsyncTask hiloconexion;
    List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        lv=(ListView)findViewById(R.id.lv);
        texto = (EditText)findViewById( R.id.txt );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

    public void btnSend(View view) {
        URL direccion;
        URLConnection cnn;
        BufferedReader reader;
        try {
            direccion = new URL(texto.getText().toString());
            cnn = direccion.openConnection();
            String texto = "";
            reader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            String cad;
            while ( (cad = reader.readLine()) != null){
                texto += cad ;
            }

            //sinLactosa
            String[] productos = texto.split( "<meta property=\"article:tag\" content=\"");
             for(int i=111; i<productos.length-120;i++){
                list.add(productos[i].split( ">" )[0].replace( "/","" ).replace( "\"","" ));
            }



        } catch (Exception ex) {
            Toast.makeText( this, ""+ex, Toast.LENGTH_SHORT ).show();
        }
        try{
            //Crea Adapter
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    list );
            //Configura Adapter a ListView.
            lv.setAdapter(arrayAdapter);
        }catch (Exception e){}
    }

    public void btnUpploader(View view) {
        for(int i=0;i<list.size();i++) {
            hiloconexion = new WebServiceAsyncTask();
            String cadenaLlamada = "https://tipix.000webhostapp.com/InsertarProductos.php?op=0";
            hiloconexion.execute( cadenaLlamada, "insertar" , list.get(i));
        }
    }

    public class WebServiceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String devuelve = "";

            if (params[1].equals( "insertar" )) {    // mostrar tickets sin leer
                devuelve = insertar( cadena, params[2] );
                return devuelve;
            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled( s );
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText( MainActivity.this, s, Toast.LENGTH_SHORT ).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate( values );
        }


        public String insertar(String cadena, String valorNombre) {
            String devuelve = "";
            URL url;
            try {
                HttpURLConnection urlConn;

                DataOutputStream printout;
                DataInputStream input;
                url = new URL( cadena );
                urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoInput( true );
                urlConn.setDoOutput( true );
                urlConn.setUseCaches( false );
                urlConn.setRequestProperty( "Content-Type", "application/json" );
                urlConn.setRequestProperty( "Accept", "application/json" );
                urlConn.connect();
                //Creo el Objeto JSON
                JSONObject jsonParam = new JSONObject();

                //En el put se introduce la clave y el valor de lo que quieres añadir al json
                jsonParam.put( "nombre", valorNombre );
                jsonParam.put( "nombre2", valorNombre );

                // Envio los parámetros post.
                OutputStream os = urlConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter( os, "UTF-8" ) );
                writer.write( jsonParam.toString() );
                writer.flush();
                writer.close();

                int respuesta = urlConn.getResponseCode();


                StringBuilder result = new StringBuilder();

                if (respuesta == 200) {

                    String line;
                    BufferedReader br = new BufferedReader( new InputStreamReader( urlConn.getInputStream() ) );
                    while ((line = br.readLine()) != null) {
                        result.append( line );
                        //response+=line;
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject( result.toString() );   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString( "estado" );   // estado es el nombre del campo en el JSON

                    if (resultJSON == "1") {      // hay un alumno que mostrar
                        devuelve = "insertado correctamente";

                    } else if (resultJSON == "2") {
                        devuelve = "no pudo insertarse";
                    }

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return devuelve;
        }

    }
}
