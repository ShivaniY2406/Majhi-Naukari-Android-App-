package com.majhinaukari.majhinaukari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imgView;
    TextView tag,marathi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

          imgView = findViewById(R.id.imageView);

          marathi = findViewById(R.id.text_marathi);
          tag = findViewById(R.id.tag_line);

          imgView.setAnimation(topAnim);


          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                  startActivity(intent);
                  finish();
              }
          },5000);

    }
}