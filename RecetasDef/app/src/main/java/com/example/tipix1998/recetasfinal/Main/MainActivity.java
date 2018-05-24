package com.example.tipix1998.recetasfinal.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tipix1998.recetasfinal.Main.Blogger.LoginBlogger_;

import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainOne;
import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainThree;
import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainTwo;
import com.example.tipix1998.recetasfinal.Main.Usuario.UsrIngredient;
import com.example.tipix1998.recetasfinal.Main.Usuario.UsrIngredient_;
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

@EActivity(R.layout.screen_main)
public class MainActivity extends AppCompatActivity {
    ///////////////////////////////
    SQLiteDatabase db;
    @ViewById ViewPager viewPager;
    @ViewById RelativeLayout rlPopUp;
    WebServiceAsyncTask hiloconexion;
    public static ArrayList<String> allProduct = new ArrayList<String>();
    ////////////////////////////////

    @AfterViews
    void afterViews() {
        createBd();
        launchExplain();
        //AutoCompletar
        hiloconexion = new WebServiceAsyncTask();
        String cadenaLlamada = "https://tipix.000webhostapp.com/ObtenerProductos.php?op=0";
        hiloconexion.execute(cadenaLlamada,"obtenerProductos"); //lo ejecutas pasandole como parametros la url y la operación que quieres que haga
    }



    ///////////////////////
    //Lanzar explicación
    ///////////////////////
    private void launchExplain(){
        db = openOrCreateDatabase( "User", Context.MODE_PRIVATE, null );
        Cursor c = db.rawQuery( "select * from usr", null );
        c.moveToNext();
        int show = c.getInt(1);
        c.close();
        if(show == 1){
            rlPopUp.setVisibility( View.VISIBLE );
            viewPager.setAdapter( new SamplePagerAdapter( getSupportFragmentManager() ) );
            db.execSQL( "update usr set show=0" );
        }
        db.close();
    }

    private void createBd(){
        db = openOrCreateDatabase( "User", Context.MODE_PRIVATE, null );
        try{
            db.execSQL( "create table usr(id INT(3), show int(1) , constraint pk primary key(id))" );
            db.execSQL( "insert into usr values(1,1)" );
        }catch (Exception e){ }
        db.close();
    }


    /////////////////////////////
    //Lanzar Activity User
    /////////////////////////////
    @Click(R.id.lySearchIngredient)
    void lySearch(){
        Intent intent = new Intent( MainActivity.this, UsrIngredient_.class );
        startActivity( intent );
    }

    /////////////////////////////
    //Lanzar Activirt Registro
    /////////////////////////////
    @Click(R.id.lyBlogger)
    void lyBlogg(){
        Intent intent = new Intent( MainActivity.this, LoginBlogger_.class );
        startActivity( intent );
    }



    //////////////////////////
    //Clase Para Mostrar Explicacion
    //////////////////////////
    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new PopUpExplainOne();
            } else if(position==1)
                return new PopUpExplainTwo();
            else if(position==2)
                return new PopUpExplainThree();
            else{}
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Click(R.id.fabClose)
    void ivPop(){
        rlPopUp.setVisibility( View.GONE );
    }

    ////////////////////////////////
    //Cargar todos los ingredientes
    ////////////////////////////////
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
