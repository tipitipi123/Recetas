package com.example.tipix1998.recetasfinal.Main.Usuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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
import java.util.ArrayList;

import Source.ScreenNavigator;
import Source.Utils;

@EActivity(R.layout.screen_ingredient)
public class UsrIngredient extends AppCompatActivity {
    @ViewById
    RecyclerView rvListProduct;
    @ViewById
    AutoCompleteTextView txtAddIngredient;
    @ViewById
    FloatingActionButton btnAdd;
    @ViewById FloatingActionButton btnNext;
    @ViewById
    TextView toolbar_title;


    UsrIngredientAdapter adapter;
    //Autocompletar
    private ArrayAdapter<String> adapterAutoComplete;
    private ArrayList<String> allProduct = new ArrayList<String>();;
    WebServiceAsyncTask hiloconexion;

    @AfterViews
    void afterView(){
        adapter = new UsrIngredientAdapter(this);
        txtAddIngredient.setOnEditorActionListener( new TextView.OnEditorActionListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(checkIngredient()) {
                    adapter.setIngredient( txtAddIngredient.getText().toString() );
                    configRecycler();
                    txtAddIngredient.setBackground(getResources().getDrawable(R.drawable.edit_text));
                    txtAddIngredient.setTextColor( getColor( R.color.colorBlack ) );
                    initialState();
                }else{
                    txtAddIngredient.setBackground(getResources().getDrawable(R.drawable.edit_text));
                    txtAddIngredient.setTextColor( getColor( R.color.colorError ) );
                }
                return true;
            }
        });

        //Font
        Typeface typeFaceTittle=Typeface.createFromAsset(getAssets(),"fonts/Chalkduster.ttf");
        toolbar_title.setTypeface(typeFaceTittle);

        //AutoCompletar
        hiloconexion = new WebServiceAsyncTask();
        String cadenaLlamada = "https://tipix.000webhostapp.com/ObtenerProductos.php?op=0";
        hiloconexion.execute(cadenaLlamada,"obtenerProductos"); //lo ejecutas pasandole como parametros la url y la operación que quieres que haga

    }

    public void configRecycler(){
        rvListProduct.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvListProduct.setLayoutManager(mLayoutManager);
        rvListProduct.setAdapter(adapter);
    }

    private void initialState(){
        txtAddIngredient.setVisibility( View.INVISIBLE );
        InputMethodManager imm = (InputMethodManager) getSystemService( Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        btnAdd.setVisibility( View.VISIBLE );
        btnNext.setVisibility( View.VISIBLE );
        txtAddIngredient.setText( "" );
    }

    //////////////////////////////////////////
    //BTN ADD
    //////////////////////////////////////////
    @Click(R.id.btnAdd)
    void buttonAdd(){
        btnAdd.setVisibility( View.INVISIBLE );
        btnNext.setVisibility( View.INVISIBLE );
        txtAddIngredient.setVisibility( View.VISIBLE );
        txtAddIngredient.setSingleLine(true);
        txtAddIngredient.requestFocus(); //Asegurar que editText tiene focus
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtAddIngredient, InputMethodManager.SHOW_IMPLICIT);
    }

    //////////////////////////////////////////
    //AutoCompletar
    //////////////////////////////////////////
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
            adapterAutoComplete = new ArrayAdapter<String>( UsrIngredient.this,android.R.layout.simple_list_item_1, allProduct );
            txtAddIngredient.setAdapter( adapterAutoComplete );
            txtAddIngredient.setThreshold( 1 );
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

    //////////////////////////////////////////
    //COMPRUEBA SI LOS PRODUCTOS SON CORRECTOS
    //////////////////////////////////////////
    private Boolean checkIngredient(){
        if((allProduct.indexOf( txtAddIngredient.getText().toString() ) == -1) || adapter.getIngredient().indexOf( txtAddIngredient.getText().toString() ) != -1){
            return false;
        }
        return true;
    }

    //////////////////////////////////////////
    //CARGA RECETAS
    /////////////////////////////////////////
    @Click(R.id.btnNext)
    void buttonNext(){
        if(adapter.getIngredient().size()>=1){
            Intent intent = new Intent( UsrIngredient.this, UsrRecipe_.class );
            intent.putExtra( "ingredientSend",adapter.getIngredient());
            startActivity( intent );
        }else{
            Utils.showAlert(this, "Debe introducir por lo menos un ingrediente","Aceptar");
        }

    }
}
