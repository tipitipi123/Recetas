package com.example.tipix1998.recetasfinal.Main.Usuario;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tipix1998.recetasfinal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.screen_recipe)
public class UsrRecipe extends AppCompatActivity {
    @ViewById
    RecyclerView rvListRecet;
    @ViewById
    TextView toolbar_ingredient;
    @ViewById TextView toolbar_title;

    private ArrayList<String> ingredientInsert;
    UsrRecipeAdapter adapter;
    @AfterViews
    void afterViews(){
        getBundle();
        showIngredientToolBar();
        adapter = new UsrRecipeAdapter(this);
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
