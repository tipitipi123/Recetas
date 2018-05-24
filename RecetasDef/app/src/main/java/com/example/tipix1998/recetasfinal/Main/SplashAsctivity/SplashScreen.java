package com.example.tipix1998.recetasfinal.Main.SplashAsctivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.tipix1998.recetasfinal.Main.MainActivity_;
import com.example.tipix1998.recetasfinal.R;

public class SplashScreen extends Activity {
    private final int DURACION_SPLASH = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView( R.layout.screen_splash);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(SplashScreen.this, MainActivity_.class);
                startActivity(intent);
                finish();
            };
        }, DURACION_SPLASH);
    }
}
