package com.plexosysconsult.hellomartmobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


TextView tvTagline;
    Typeface productSansBold;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvTagline = findViewById(R.id.tv_tagline);

        productSansBold = Typeface.createFromAsset(this.getAssets(), "fonts/ProductSansBold.ttf");

        tvTagline.setTypeface(productSansBold);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.putExtra("beginning", 1);
                startActivity(i);

                finish();

                //  tvLogo.startAnimation(myAnim);


            }
        }, 2500);

    }
}
