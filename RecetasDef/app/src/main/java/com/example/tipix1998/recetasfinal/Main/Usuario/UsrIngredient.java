package com.example.tipix1998.recetasfinal.Main.Usuario;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.example.tipix1998.recetasfinal.Main.MainActivity;
import com.example.tipix1998.recetasfinal.Main.MainActivity_;
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
    @ViewById
    FloatingActionButton btnNext;
    @ViewById
    TextView toolbar_title;

    UsrIngredientAdapter adapter;
    //Autocompletar
    private ArrayAdapter<String> adapterAutoComplete;
    private ArrayList<String> allProduct = MainActivity.allProduct;



    @AfterViews
    void afterView() {
        //Adaptador AutoComplete Text
        adapter = new UsrIngredientAdapter( this );
        adapterAutoComplete = new ArrayAdapter<String>( UsrIngredient.this, android.R.layout.simple_list_item_1, MainActivity.allProduct );
        txtAddIngredient.setAdapter( adapterAutoComplete );
        txtAddIngredient.setThreshold( 1 );

        txtAddIngredient.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (checkIngredient()) {
                    adapter.setIngredient( txtAddIngredient.getText().toString() );
                    configRecycler();
                    txtAddIngredient.setTextColor( getResources().getColor( R.color.colorBlack ) );
                    initialState();
                } else {
                    txtAddIngredient.setTextColor( getResources().getColor( R.color.colorError ) );
                }
                return true;
            }
        } );

        //Font
        Typeface typeFaceTittle = Typeface.createFromAsset( getAssets(), "fonts/Chalkduster.ttf" );
        toolbar_title.setTypeface( typeFaceTittle );

    }

    public void configRecycler() {
        rvListProduct.setHasFixedSize( true );
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );
        rvListProduct.setLayoutManager( mLayoutManager );
        rvListProduct.setAdapter( adapter );
    }

    private void initialState() {
        InputMethodManager imm = (InputMethodManager) getSystemService( Activity.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput( InputMethodManager.HIDE_IMPLICIT_ONLY, 0 );
        btnNext.setVisibility( View.VISIBLE );
        txtAddIngredient.setText( "" );
    }

    //////////////////////////////////////////
    //BTN ADD
    //////////////////////////////////////////
    @Click(R.id.btnAdd)
    void buttonAdd() {
        btnAdd.setVisibility( View.INVISIBLE );
        btnNext.setVisibility( View.VISIBLE );
        txtAddIngredient.setVisibility( View.VISIBLE );
        txtAddIngredient.setSingleLine( true );
        txtAddIngredient.requestFocus(); //Asegurar que editText tiene focus
        InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(txtAddIngredient, InputMethodManager.SHOW_IMPLICIT);
    }


    //////////////////////////////////////////
    //COMPRUEBA SI LOS PRODUCTOS SON CORRECTOS
    //////////////////////////////////////////
    private Boolean checkIngredient() {
        if ((allProduct.indexOf( txtAddIngredient.getText().toString() ) == -1) || adapter.getIngredient().indexOf( txtAddIngredient.getText().toString() ) != -1) {
            return false;
        }
        return true;
    }

    //////////////////////////////////////////
    //CARGA RECETAS
    /////////////////////////////////////////
    @Click(R.id.btnNext)
    void buttonNext() {
        if (adapter.getIngredient().size() >= 1) {
            Intent intent = new Intent( UsrIngredient.this, UsrRecipe_.class );
            intent.putExtra( "ingredientSend", adapter.getIngredient() );
            startActivity( intent );
        } else {
            Utils.showAlert( this, "Debe introducir por lo menos un ingrediente", "Aceptar" );
        }

    }
}
