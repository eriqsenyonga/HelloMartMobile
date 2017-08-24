package com.plexosysconsult.hellomartmobile;

import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CheckoutActivity extends AppCompatActivity {

    FragmentManager fm;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fm = getSupportFragmentManager();

        fm.beginTransaction().replace(R.id.contentMain, new BillingDetailsFragment()).commit();

        getSupportActionBar().setTitle("Checkout");


    }

    public void setActionBarSubtitle(String subTitle) {

        getSupportActionBar().setSubtitle(subTitle);
    }

    public void showPaymentMethods() {

        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.contentMain, new PaymentMethodsFragment()).addToBackStack(null).commit();

    }

    public void showPesapalIframe(Long orderId) {

        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.contentMain, PesapalIframeFragment.newInstance(orderId)).addToBackStack(null).commit();

    }
}
