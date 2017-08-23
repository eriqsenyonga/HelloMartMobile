package com.plexosysconsult.hellomartmobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodsFragment extends Fragment implements View.OnClickListener {

    RadioButton rbCOD, rbPesapal;
    CheckoutActivity checkoutActivity;
    Button bPlaceOrder;
    View v;


    public PaymentMethodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_payment_methods, container, false);
        rbCOD = (RadioButton) v.findViewById(R.id.rb_cod);
        rbPesapal = (RadioButton) v.findViewById(R.id.rb_pesapal);
        bPlaceOrder = (Button) v.findViewById(R.id.b_place_order);


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkoutActivity = (CheckoutActivity) getActivity();

        rbCOD.setOnClickListener(this);
        rbPesapal.setOnClickListener(this);
        bPlaceOrder.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        checkoutActivity.setActionBarSubtitle("Payment Method");

    }

    @Override
    public void onClick(View view) {
        if (view == rbCOD) {

            bPlaceOrder.setText("Place Order");


        }

        if (view == rbPesapal) {


            bPlaceOrder.setText("Pay Online");


        }


        if (view == bPlaceOrder) {

            if (rbPesapal.isChecked()) {
                //place order through pesapal order, get the order number and pass to iframe


                //show pesapal iframe

                checkoutActivity.showPesapalIframe();

            } else if (rbCOD.isChecked()) {
                //place order and kiwedde


            }


        }


    }
}
