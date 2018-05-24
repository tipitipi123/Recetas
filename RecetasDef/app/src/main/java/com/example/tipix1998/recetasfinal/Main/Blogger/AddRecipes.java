package com.example.tipix1998.recetasfinal.Main.Blogger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tipix1998.recetasfinal.Main.MainActivity;
import com.example.tipix1998.recetasfinal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import Source.Utils;

import static com.example.tipix1998.recetasfinal.R.drawable.btnivok_tag;

@EActivity(R.layout.screen_addrecipes)
public class AddRecipes extends AppCompatActivity {

    //////////////////////////////////////
    @ViewById LinearLayout llViewTag;
    @ViewById TextInputEditText etUrl;
    @ViewById TextInputEditText etName;
    @ViewById TextInputEditText etNameDis;
    @ViewById ImageButton btnImage;
    @ViewById ImageButton btnImageView;
    @ViewById TextInputLayout etNameDisLayout;
    @ViewById TextInputLayout etNameLayout;
    String imagen;
    final ArrayList<TagLayout.ItemTag> arr = new ArrayList<>();
    boolean show=false;
    //////////////////////////////////////

    @AfterViews
    void afterViews(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    private void addTags(){

        llViewTag.removeAllViews();

        final TagLayout tagLayout = new TagLayout(this);
        tagLayout.setListTag(arr);
        tagLayout.attackToView(llViewTag);
    }

    public TagLayout.ItemTag getDefaultTagItem() {
        int textSize = (int) getResources().getDimension(R.dimen.text_size_normal);
        int textMarrgin = (int) getResources().getDimension(R.dimen.text_marrgin);
        Rect margin = new Rect(textMarrgin, textMarrgin, textMarrgin, textMarrgin);
        return new TagLayout.ItemTag("", textSize, getResources().getColor( R.color.txtorange ), Typeface.BOLD, R.drawable.btnivdisabble_tag, margin, null);
    }


    @Click(R.id.btnAdd)
    void btnAdd(){
        if(etUrl.equals( "" )){
            Utils.showAlert(this, "Debe introducir una url para continuar","Aceptar");
        }else{
            if(show){
                Toast.makeText( this, "Eo", Toast.LENGTH_SHORT ).show();
            }else{
                showOpen();
                obtenerIngredient();
                addTags();
                cargarImagen( imagen );
                show=true;
            }
        }
    }

    ///////////////////////
    //Mostrar Datos
    //////////////////////
    private void showOpen(){
        etNameLayout.setVisibility( View.VISIBLE );
        etNameDisLayout.setVisibility( View.GONE );
        //btnImage.setImageResource( R.drawable.ic_fotok );
        btnImage.setBackgroundResource( R.drawable.btnivok_tag );
        etUrl.setEnabled(false);
    }

    private void obtenerIngredient(){
        //Sin Lactosa
        if(etUrl.getText().toString().indexOf( "www.recetassinlactosa.com" )>0){
            sinLactosa();
        }


    }

    /////////////////////////
    //SinLactosa
    /////////////////////////
    private void sinLactosa(){
        URL direccion;
        URLConnection cnn;
        BufferedReader reader;
        try {
            direccion = new URL(etUrl.getText().toString());
            cnn = direccion.openConnection();
            String texto = "";
            reader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            String cad;
            while ( (cad = reader.readLine()) != null){
                texto += cad ;
            }

            //sinLactosa

            //Imagen (Depuracion de String)
            String imagenNoDep[] = texto.split("<meta property=\"og:image\" content=\"");
            String imagenNoDepDos = imagenNoDep[1].split(">")[0].replace( "\"","").replace(" ","");
            imagen = imagenNoDepDos.substring(0, imagenNoDepDos.length()-1 );

            //Productos
            String[] productos = texto.split( "<meta property=\"article:tag\" content=\"");
             for(int i=1; i<productos.length;i++){
                 String producto="";
                 producto=productos[i].split( ">" )[0].replace( "/","" ).replace( "\"","");
                 String uperCase=ponerMayus(producto);
                 int posicion=MainActivity.allProduct.indexOf( uperCase );
                 if(posicion>=0)
                    arr.add(getDefaultTagItem().setText( MainActivity.allProduct.get(posicion)));
             }
        } catch (Exception ex) {
            Toast.makeText( this, ""+ex,Toast.LENGTH_LONG).show();
        }
    }

    private String ponerMayus(String texto){
        String resultado = "";
        String mayus = texto.charAt( 0 )+"";
            for(int i = 0; i<texto.length()-1; i++){
                if(i==0){
                    resultado += mayus.toUpperCase();
                }else{
                    resultado += texto.charAt( i );
                }
            }

        return  resultado;
    }

    private void cargarImagen(String urlFoto){
        InputStream in = null;
        try {
            in = new URL(urlFoto).openStream();
            btnImageView.setImageBitmap((Bitmap) BitmapFactory.decodeStream(in));
            btnImage.setVisibility( View.GONE );
            btnImageView.setVisibility( View.VISIBLE );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
