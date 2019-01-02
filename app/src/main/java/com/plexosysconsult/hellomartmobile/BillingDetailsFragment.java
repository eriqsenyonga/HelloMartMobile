package com.plexosysconsult.hellomartmobile;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillingDetailsFragment extends Fragment implements View.OnClickListener {

    View v;
    TextInputLayout tilFirstName, tilSurName, tilEmail, tilPhoneNumber,
            tilPassword, tilReenterPassword, tilDeliveryAddress, tilTownCity, tilOrderNotes;
    Button bGoToPaymentMethods;

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();

    // String URL_PLACE_ORDER = "http://hellomartug.com/example/placeOrder.php";
    // ProgressDialog progressDialog;

    CheckoutActivity checkoutActivity;

    SharedPreferences userSharedPrefs;
    int customerId = 0;


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
        tilOrderNotes = v.findViewById(R.id.til_order_notes);
        bGoToPaymentMethods = (Button) v.findViewById(R.id.b_go_to_payment_methods);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTypefaceForViews();

        checkoutActivity = (CheckoutActivity) getActivity();

        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        // editor = userSharedPrefs.edit();

     //   tilFirstName.getEditText().setText(userSharedPrefs.getString("fname", ""));

        if (userSharedPrefs.getBoolean("available", false)) {

            populateFields();
            customerId = userSharedPrefs.getInt("customerId", 0);

        }

        bGoToPaymentMethods.setOnClickListener(this);


    }

    private void setTypefaceForViews() {

        tilOrderNotes.setTypeface(myApplicationClass.getBoldTypeface());
        tilOrderNotes.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilFirstName.setTypeface(myApplicationClass.getBoldTypeface());
        tilFirstName.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilSurName.setTypeface(myApplicationClass.getBoldTypeface());
        tilSurName.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilEmail.setTypeface(myApplicationClass.getBoldTypeface());
        tilEmail.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilPhoneNumber.setTypeface(myApplicationClass.getBoldTypeface());
        tilPhoneNumber.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilDeliveryAddress.setTypeface(myApplicationClass.getBoldTypeface());
        tilDeliveryAddress.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilPassword.setTypeface(myApplicationClass.getBoldTypeface());
        tilPassword.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        tilTownCity.setTypeface(myApplicationClass.getBoldTypeface());
        tilTownCity.getEditText().setTypeface(myApplicationClass.getRegularTypeface());

        bGoToPaymentMethods.setTypeface(myApplicationClass.getBoldTypeface());

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
        tilOrderNotes.getEditText().setText(myApplicationClass.getBillingDetails().getOrderNotes());

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
                mBillingDetails.setOrderNotes(tilOrderNotes.getEditText().getText().toString());

                myApplicationClass.setBillingDetails(mBillingDetails);


                checkoutActivity.showPaymentMethods();

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

    public void populateFields() {

        String fname = userSharedPrefs.getString("fname", "");
        String lname = userSharedPrefs.getString("lname", "");
        String email = userSharedPrefs.getString("email", "");

        myApplicationClass.getBillingDetails().setFirstName(fname);
        myApplicationClass.getBillingDetails().setSurname(lname);
        myApplicationClass.getBillingDetails().setEmailAddress(email);

     //   tilFirstName.getEditText().setText(fname);
     //   tilSurName.getEditText().setText(lname);
     //   tilEmail.getEditText().setText(email);



    }
}
