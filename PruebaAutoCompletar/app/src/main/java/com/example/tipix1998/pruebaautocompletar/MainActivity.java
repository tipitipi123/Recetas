package com.example.tipix1998.pruebaautocompletar;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView autoComplete;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> allProduct = new ArrayList<String>();;
    WebServiceAsyncTask hiloconexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        hiloconexion = new WebServiceAsyncTask();
        String cadenaLlamada = "https://tipix.000webhostapp.com/ObtenerProductos.php?op=0";
        hiloconexion.execute(cadenaLlamada,"obtenerProductos"); //lo ejecutas pasandole como parametros la url y la operación que quieres que haga

        autoComplete = (AutoCompleteTextView)findViewById( R.id.txtAuto );


    }

    ////////////////////////////////////////
    //Comprueba si lo que introduce existe//
    ////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void btnComprobar(View view) {
        String texto = autoComplete.getText().toString();
        int numero = allProduct.indexOf( texto );
        if(numero==-1){
            autoComplete.setBackgroundColor( getColor(R.color.colorError));
        }else{
            autoComplete.setBackgroundColor( getColor(R.color.colorCorrecto));
        }

    }

    public class WebServiceAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String devuelve ="";

            if(params[1].equals("obtenerProductos")){    // mostrar tickets sin leer
                devuelve = obtenerProductos(cadena);
                return devuelve;
            }
            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            adapter = new ArrayAdapter<String>( MainActivity.this,android.R.layout.simple_list_item_1, allProduct );
            autoComplete.setAdapter( adapter );
            autoComplete.setThreshold( 1 );
            Toast.makeText( MainActivity.this, "Creado", Toast.LENGTH_SHORT ).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public String obtenerProductos(String cadena){
        String devuelve = "";
        URL url;

        try {
            url = new URL(cadena); //creamos la url
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
            connection.setConnectTimeout(15000);//parametros por defecto
            connection.setReadTimeout(10000);

            int respuesta = connection.getResponseCode(); //almacenamos la respuesta a la conexion
            StringBuilder result = new StringBuilder();

            if (respuesta == 200) { //200 si la conexion ha sido exitosa

                InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                // StringBuilder.
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);        // Paso toda la entrada al StringBuilder
                }

                //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                //Accedemos al vector de resultados

                String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                if (resultJSON.equals("1")) { //comprueba el estado de la consulta
                    String nombre;

                    //obtengo el array JSON de productos
                    JSONArray ticketsJSON = respuestaJSON.getJSONArray("productos");
                    for (int i = 0; i < ticketsJSON.length(); i++) {
                        nombre = ticketsJSON.getJSONObject(i).getString("nombre");
                        allProduct.add( nombre );
                    }

                    devuelve = "Productos cargados con exito";

                } else if (resultJSON.equals("2")) {
                    devuelve = "No hay productos";
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
