package com.example.tipix1998.mainactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewById LinearLayout lySearchIngredient;
    @ViewById LinearLayout lyBlogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

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
