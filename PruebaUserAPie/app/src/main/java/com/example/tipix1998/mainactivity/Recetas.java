package com.example.tipix1998.mainactivity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_recetas)
public class Recetas extends AppCompatActivity {
    @ViewById RecyclerView rvListRecet;
    @ViewById TextView toolbar_ingredient;
    @ViewById TextView toolbar_title;

    private ArrayList<String> ingredientInsert;
    RecetasAdapter adapter;
    @AfterViews
    void afterViews(){
        getBundle();
        showIngredientToolBar();
        adapter = new RecetasAdapter(this);
        configRecycler();
        //Font
        Typeface typeFaceTittle=Typeface.createFromAsset(getAssets(),"fonts/Chalkduster.ttf");
        toolbar_title.setTypeface(typeFaceTittle);
        Typeface typeFaceText=Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue_Bold.ttf");
        toolbar_ingredient.setTypeface(typeFaceText);
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

    public void configRecycler(){
        rvListRecet.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvListRecet.setLayoutManager(mLayoutManager);
        rvListRecet.setAdapter(adapter);
    }
}
