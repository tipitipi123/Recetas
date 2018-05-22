package com.example.tipix1998.recetasfinal.Main;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tipix1998.recetasfinal.Main.Blogger.LoginBlogger_;

import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainOne;
import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainThree;
import com.example.tipix1998.recetasfinal.Main.PopUpExplain.PopUpExplainTwo;
import com.example.tipix1998.recetasfinal.Main.Usuario.UsrIngredient_;
import com.example.tipix1998.recetasfinal.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.screen_main)
public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @ViewById ImageView ivInit;
    @ViewById ViewPager viewPager;
    @ViewById RelativeLayout rlPopUp;



    @AfterViews
    void afterViews() {
        showImage();
        createBd();
    }

    ////////////////////////
    //Show InitImage
    ////////////////////////
    private void showImage(){
        new CountDownTimer(2000, 1000) { // 5000 = 5 sec

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ivInit.setVisibility( View.GONE );
                launchExplain();
            }
        }.start();
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
}
