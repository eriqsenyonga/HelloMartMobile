package com.plexosysconsult.hellomartmobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {


TextView tvLogo;
    Typeface lobsterTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      //  tvLogo = (TextView) findViewById(R.id.tv_logo);
        lobsterTwo = Typeface.createFromAsset(this.getAssets(), "fonts/LobsterTwo-Regular.otf");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra("beginning", 1);
                startActivity(i);

                finish();

                //  tvLogo.startAnimation(myAnim);


            }
        }, 2000);

    }
}
