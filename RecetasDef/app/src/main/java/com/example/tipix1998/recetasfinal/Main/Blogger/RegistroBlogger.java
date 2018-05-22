package com.example.tipix1998.recetasfinal.Main.Blogger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tipix1998.recetasfinal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
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

import Source.ScreenNavigator;
import Source.Utils;

@EActivity(R.layout.screen_registro)
public class RegistroBlogger extends AppCompatActivity {
    //////////////////////////////////////
    @ViewById
    TextInputEditText etUsr;
    @ViewById TextInputEditText etMail;
    @ViewById TextInputEditText etBlog;
    @ViewById TextInputEditText etPass;
    WebServiceAsyncTask hiloconexion;
    ///////////////////////////////////////


    @AfterViews
    void afterViews(){

    }

    @Click(R.id.btnRegistro)
    void btnRegistro(){
        try{
            if(checkEt()){
                insertBlogger();
            }else{
                Utils.showAlert(this, "Debe completar todos los campos para continuar","Aceptar");
            }
        }catch (java.lang.IllegalStateException e){

        }


    }

    private boolean checkEt(){
        if(etUsr.getText().toString().equals( "" ) || etMail.getText().toString().equals( "" ) ||
                etBlog.getText().toString().equals( "" ) || etPass.getText().toString().equals( "" ))
            return false;
        return true;
    }

    /////////////////////////////
    //Insertar blogger nuevo
    /////////////////////////////
    private void insertBlogger(){
        hiloconexion = new WebServiceAsyncTask(this);
        String cadenaLlamada = "https://tipix.000webhostapp.com/insertarBlogger.php?op=0";
        hiloconexion.execute(cadenaLlamada,"insertar"); //lo ejecutas pasandole como parametros la url y la operación que quieres que haga
    }

    public class WebServiceAsyncTask extends AsyncTask<String, Void, String> {
        String devuelve = "";
        Context context;
        public WebServiceAsyncTask(RegistroBlogger context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String cadena = params[0];
            String devuelve;

            if (params[1].equals( "insertar" )) {    // mostrar tickets sin leer
                devuelve = insertar( cadena );
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
            if (devuelve.equals( "1" )){
                PopUpRegistro fragment = PopUpRegistro_.builder().build();
                ScreenNavigator.openDialogFragment( (FragmentActivity) context, fragment);
            }else{
                Utils.showAlert(context, "No se ha podido completar el registro","Aceptar");
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



        public String insertar(String cadena) {

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
                jsonParam.put( "email", etMail.getText().toString());
                jsonParam.put( "nombre", etUsr.getText().toString());
                jsonParam.put( "enlace", etBlog.getText().toString());
                jsonParam.put( "pass" , etPass.getText().toString());

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
                    devuelve = resultJSON;

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
