package com.example.tipix1998.mainactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_recetas)
public class Recetas extends AppCompatActivity {

    @ViewById TextView toolbar_ingredient;
    private ArrayList<String> ingredientInsert;

    @AfterViews
    void afterViews(){
        getBundle();
        showIngredientToolBar();
    }

    private void getBundle(){
     ingredientInsert = getIntent().getExtras().getStringArrayList( "ingredientSend" );
    }

    private void showIngredientToolBar(){
        String ingredient="";

        for(int i =0;i<ingredientInsert.size();i++){
            if(ingredient.length()+ingredientInsert.get(i).length()>55){
                ingredient+="...";
                break;
            }else{
                ingredient+=ingredientInsert.get( i )+"    ";
            }
        }

        toolbar_ingredient.setText( ingredient );
    }
}
