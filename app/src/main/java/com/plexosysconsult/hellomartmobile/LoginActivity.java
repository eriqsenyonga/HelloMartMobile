package com.plexosysconsult.hellomartmobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView tvLogo;
    Typeface lobsterTwo;
    Button bLogin;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLogo = (TextView) findViewById(R.id.tv_logo);
        lobsterTwo = Typeface.createFromAsset(this.getAssets(), "fonts/LobsterTwo-Regular.otf");
        bLogin = (Button) findViewById(R.id.btn_login);
       iv = (ImageView) findViewById(R.id.iv_splash);


        tvLogo.setTypeface(lobsterTwo);



        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("beginning", 1);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        LoginActivity.this, iv, ViewCompat.getTransitionName(iv)
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(i, options.toBundle());
                } else {

                    startActivity(i);

                }

                finish();


            }
        });
    }
}
