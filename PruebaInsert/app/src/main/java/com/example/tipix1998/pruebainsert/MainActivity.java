package com.example.tipix1998.pruebainsert;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    WebServiceAsyncTask hiloconexion;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        
        txt = (TextView)findViewById( R.id.txt );
    }

    public void btn(View view) {
        hiloconexion = new WebServiceAsyncTask();
        String cadenaLlamada = "https://tipix.000webhostapp.com/InsertarProductos.php?op=0";
        hiloconexion.execute( cadenaLlamada, "insertar" );
    }

    //Tarea asincrona que sirve para obtener tanto los productos como las categorias del web service.
//En el PostExecute se muestra en el textview si la operación se ha realizado correctamente.
    public class WebServiceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String devuelve = "";

            if (params[1].equals( "insertar" )) {    // mostrar tickets sin leer
                devuelve = insertar( cadena, "prueba8" );
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