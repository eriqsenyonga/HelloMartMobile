package com.plexosysconsult.hellomartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderSuccessActivity extends AppCompatActivity {

    Button bContinueShopping;
    TextView tvThankYou, tvSuccess;

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        tvThankYou = findViewById(R.id.tv_thank_you);
        tvSuccess = findViewById(R.id.tv_success);
        bContinueShopping = (Button) findViewById(R.id.b_continue_shopping);
        bContinueShopping.setTypeface(myApplicationClass.getBoldTypeface());
        tvSuccess.setTypeface(myApplicationClass.getBoldTypeface());
        tvThankYou.setTypeface(myApplicationClass.getBoldTypeface());



        bContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OrderSuccessActivity.this, MainActivity.class);
                i.putExtra("beginning", 1);
                startActivity(i);
            }
        });

        myApplicationClass.getCart().emptyCart();
    }
}
