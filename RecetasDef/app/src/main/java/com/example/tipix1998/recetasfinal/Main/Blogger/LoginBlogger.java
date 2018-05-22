package com.example.tipix1998.recetasfinal.Main.Blogger;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tipix1998.recetasfinal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
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

@EActivity(R.layout.screen_login)
public class LoginBlogger extends AppCompatActivity {
    ///////////////////////////////////////
    @ViewById
    TextInputEditText etLogin;
    @ViewById TextInputEditText etPass;
    @ViewById
    TextView et_title;
    @ViewById TextView etError;
    WebServiceAsyncTask hiloconexion;
    ///////////////////////////////////////

    @AfterViews
    void afterViews(){
        //Font
        Typeface typeFaceTittle=Typeface.createFromAsset(getAssets(),"fonts/Chalkduster.ttf");
        et_title.setTypeface(typeFaceTittle);
    }

    @Click(R.id.btnNext)
    void btnNext(){
        if(checkEt()){
            longear();
        }
    }

    @Click(R.id.btnRegister)
    void btnRegistrer(){
        Intent intent = new Intent( LoginBlogger.this,RegistroBlogger_.class );
        startActivity( intent );
    }

    private boolean checkEt(){
        if((etLogin.getText().toString().equals( "" )) || (etPass.getText().toString().equals( "" ))){
            etError.setVisibility( View.VISIBLE );
            return false;
        }
        etError.setVisibility( View.INVISIBLE );
        return true;
    }


    //////////////////////////////
    //Comprobar si puede longear
    /////////////////////////////
    private void longear(){
        hiloconexion = new WebServiceAsyncTask();
        String cadenaLlamada = String.format("https://tipix.000webhostapp.com/login.php?op=0&email=%s&pass=%s",etLogin.getText().toString(),etPass.getText().toString());
        hiloconexion.execute(cadenaLlamada,"obtenerDatos");
    }


    public class WebServiceAsyncTask extends AsyncTask<String,Void,String> {
        String devuelve = "";

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String devuelve = "";

            if (params[1].equals( "obtenerDatos" )) {    // mostrar tickets sin leer
                devuelve = obtenerDatos( cadena );
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
            if (devuelve.equals( "1" )) {
                Toast.makeText( LoginBlogger.this, "Dentro", Toast.LENGTH_SHORT ).show();
                etError.setVisibility( View.INVISIBLE );
            }else{
                etError.setVisibility( View.VISIBLE );
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate( values );
        }

        public String obtenerDatos(String cadena) {

            URL url;

            try {
                url = new URL( cadena ); //creamos la url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                connection.setConnectTimeout( 15000 );//parametros por defecto
                connection.setReadTimeout( 10000 );

                int respuesta = connection.getResponseCode(); //almacenamos la respuesta a la conexion
                StringBuilder result = new StringBuilder();

                if (respuesta == 200) { //200 si la conexion ha sido exitosa

                    InputStream in = new BufferedInputStream( connection.getInputStream() );  // preparo la cadena de entrada

                    BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );  // la introduzco en un BufferedReader

                    // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                    // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                    // StringBuilder.
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append( line );        // Paso toda la entrada al StringBuilder
                    }

                    //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                    JSONObject respuestaJSON = new JSONObject( result.toString() );   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                    //Accedemos al vector de resultados

                    String resultJSON = respuestaJSON.getString( "estado" );   // estado es el nombre del campo en el JSON

                    if (resultJSON.equals( "1" )) { //comprueba el estado de la consulta
                        String nombre;

                        //obtengo el array JSON de productos
                        JSONArray ticketsJSON = respuestaJSON.getJSONArray( "usuario" );
                        devuelve = ticketsJSON.getJSONObject( 0 ).getString( "permisos" );

                    } else if (resultJSON.equals( "2" )) {
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
}
