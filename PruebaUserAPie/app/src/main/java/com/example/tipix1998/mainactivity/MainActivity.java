package com.example.tipix1998.mainactivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById LinearLayout lySearchIngredient;
    @ViewById LinearLayout lyBlogger;
    @ViewById TextView toolbar_title_main;
    @ViewById TextView txtSearchIngredient;
    @ViewById TextView txtbloggerMain;

    @AfterViews
    void afterView(){
        Typeface typeFaceTittle=Typeface.createFromAsset(getAssets(),"fonts/Chalkduster.ttf");
        toolbar_title_main.setTypeface(typeFaceTittle);

        Typeface typeFaceText=Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue_Bold.ttf");
        txtSearchIngredient.setTypeface(typeFaceText);
        txtbloggerMain.setTypeface(typeFaceText);
    }

    @Click (R.id.lySearchIngredient)
    void lySearch(){
        Intent intent = new Intent( MainActivity.this, SoyUser_.class );
        startActivity( intent );
    }

    @Click(R.id.lyBlogger)
    void lyBlogg(){
        Toast.makeText( this, "Blogg", Toast.LENGTH_SHORT ).show();
    }

    @LongClick(R.id.lyBlogger)
    void longlyBlogg(){

    }
}
