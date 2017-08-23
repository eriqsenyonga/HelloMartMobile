package com.plexosysconsult.hellomartmobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillingDetailsFragment extends Fragment implements View.OnClickListener {

    View v;
    TextInputLayout tilFirstName, tilSurName, tilEmail, tilPhoneNumber,
            tilPassword, tilReenterPassword, tilDeliveryAddress, tilTownCity;
    Button bGoToPaymentMethods;

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;
    // String URL_PLACE_ORDER = "http://hellomartug.com/example/placeOrder.php";
    // ProgressDialog progressDialog;

    CheckoutActivity checkoutActivity;

    public BillingDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_billing_details, container, false);

        tilFirstName = (TextInputLayout) v.findViewById(R.id.til_firstname);
        tilSurName = (TextInputLayout) v.findViewById(R.id.til_surname);
        tilEmail = (TextInputLayout) v.findViewById(R.id.til_email);
        tilPhoneNumber = (TextInputLayout) v.findViewById(R.id.til_phone_number);
        tilDeliveryAddress = (TextInputLayout) v.findViewById(R.id.til_address_line_1);
        tilPassword = (TextInputLayout) v.findViewById(R.id.til_password);
        tilTownCity = (TextInputLayout) v.findViewById(R.id.til_city_town);
        tilReenterPassword = (TextInputLayout) v.findViewById(R.id.til_reenter_password);
        bGoToPaymentMethods = (Button) v.findViewById(R.id.b_go_to_payment_methods);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        checkoutActivity = (CheckoutActivity) getActivity();

        bGoToPaymentMethods.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();

        checkoutActivity.setActionBarSubtitle("Billing Details");

    }

    @Override
    public void onClick(View view) {
        if (view == bGoToPaymentMethods) {

            checkoutActivity.showPaymentMethods();

        }
    }
}
