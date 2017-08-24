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

        tilFirstName.getEditText().setText(myApplicationClass.getBillingDetails().getFirstName());
        tilSurName.getEditText().setText(myApplicationClass.getBillingDetails().getSurname());
        tilDeliveryAddress.getEditText().setText(myApplicationClass.getBillingDetails().getDeliveryAddress());
        tilEmail.getEditText().setText(myApplicationClass.getBillingDetails().getEmailAddress());
        tilPhoneNumber.getEditText().setText(myApplicationClass.getBillingDetails().getPhoneNumber());
        tilTownCity.getEditText().setText(myApplicationClass.getBillingDetails().getTownCity());

    }

    @Override
    public void onClick(View view) {
        if (view == bGoToPaymentMethods) {


            if (checkBillingDetailsComplete() == true) {


                BillingDetails mBillingDetails = new BillingDetails();
                mBillingDetails.setFirstName(tilFirstName.getEditText().getText().toString());
                mBillingDetails.setSurname(tilSurName.getEditText().getText().toString());
                mBillingDetails.setDeliveryAddress(tilDeliveryAddress.getEditText().getText().toString());
                mBillingDetails.setEmailAddress(tilEmail.getEditText().getText().toString());
                mBillingDetails.setPhoneNumber(tilPhoneNumber.getEditText().getText().toString());
                mBillingDetails.setTownCity(tilTownCity.getEditText().getText().toString());

                myApplicationClass.setBillingDetails(mBillingDetails);


                checkoutActivity.showPaymentMethods();

            } else {

                //if billing details are not complete

            }

        }
    }


    private boolean checkBillingDetailsComplete() {

//double check the entries...if they are all complete then return true
        if (tilFirstName.getEditText().getText().toString().isEmpty()) {

            tilFirstName.getEditText().setError("Enter First Name");

            return false;

        }

        if (tilSurName.getEditText().getText().toString().isEmpty()) {

            tilSurName.getEditText().setError("Enter surname");

            return false;

        }

        if (tilPhoneNumber.getEditText().getText().toString().isEmpty()) {

            tilPhoneNumber.getEditText().setError("Enter Phone Number");

            return false;

        }
        if (tilEmail.getEditText().getText().toString().isEmpty()) {

            tilEmail.getEditText().setError("Enter E-mail");

            return false;

        }
        if (tilDeliveryAddress.getEditText().getText().toString().isEmpty()) {

            tilDeliveryAddress.getEditText().setError("Enter Delivery Address");


            return false;

        }
        if (tilTownCity.getEditText().getText().toString().isEmpty()) {

            tilTownCity.getEditText().setError("Enter Town or City");

            return false;

        }


        return true;
    }
}
