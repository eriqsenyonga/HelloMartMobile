package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    TextView tvTagline, tvLabelRegister, tvAlreadyHaveAnAccount;
    Button bRegister;
    TextInputLayout tilFname, tilLname, tilPhone, tilEmail, tilPassword;
    EditText etFname, etLname, etPhone, etEmail, etPassword;
    private static final String REGISTER_URL = "createCustomer.php";
    SharedPreferences userSharedPrefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvTagline = findViewById(R.id.tv_tagline);
        tvLabelRegister = findViewById(R.id.tv_label_register);
        bRegister = findViewById(R.id.btn_register);
        tilFname = findViewById(R.id.til_firstname);
        tilLname = findViewById(R.id.til_lastname);
        tilEmail = findViewById(R.id.til_email);
        tilPhone = findViewById(R.id.til_phone_number);
        tilPassword = findViewById(R.id.til_password);
        etFname = findViewById(R.id.et_first_name);
        etLname = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone_number);
        etPassword = findViewById(R.id.et_password);
        tvAlreadyHaveAnAccount = findViewById(R.id.tv_already_have_account);

        setTypefacesForViews();

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        editor = userSharedPrefs.edit();

        bRegister.setOnClickListener(this);

    }

    private void setTypefacesForViews() {

        tvTagline.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelRegister.setTypeface(myApplicationClass.getBoldTypeface());
        bRegister.setTypeface(myApplicationClass.getBoldTypeface());
        tilFname.setTypeface(myApplicationClass.getBoldTypeface());
        etFname.setTypeface(myApplicationClass.getRegularTypeface());
        tilLname.setTypeface(myApplicationClass.getBoldTypeface());
        etLname.setTypeface(myApplicationClass.getRegularTypeface());
        tilEmail.setTypeface(myApplicationClass.getBoldTypeface());
        etEmail.setTypeface(myApplicationClass.getRegularTypeface());
        tilPhone.setTypeface(myApplicationClass.getBoldTypeface());
        etPhone.setTypeface(myApplicationClass.getRegularTypeface());
        tilPassword.setTypeface(myApplicationClass.getBoldTypeface());
        etPassword.setTypeface(myApplicationClass.getBoldTypeface());
        tvAlreadyHaveAnAccount.setTypeface(myApplicationClass.getBoldTypeface());

    }

    @Override
    public void onClick(View v) {
        if (v == bRegister) {

            if (validEntries()) {

                bRegister.setActivated(false);
                bRegister.setText("Please Wait...");

                String fname = tilFname.getEditText().getText().toString().trim();
                String lname = tilLname.getEditText().getText().toString().trim();
                String email = tilEmail.getEditText().getText().toString().trim();
                String password = tilPassword.getEditText().getText().toString().trim();
                String username = email;

                //get nonce
                // getNonce();

                //attempt to register the ninja
                //    registerNewPerson(fname, lname, email, password, username);
                registerNewPersonWooCommerceVersion(fname, lname, email, username, password);

            }

        }
    }

    private void registerNewPersonWooCommerceVersion(final String fname, final String lname, final String email, String username, String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                MyApplicationClass.generalUrl + REGISTER_URL
                        //  + "&nonce=" + nonce
                        + "?username=" + username
                        + "&display_name=" + fname
                        + "&first_name=" + fname
                        + "&last_name=" + lname
                        + "&email=" + email
                        + "&password=" + password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        try {

                            JSONObject jsonResponse = new JSONObject(response);

                            int customerId = jsonResponse.getJSONObject("customer").getInt("id");
                            saveUserDetailsInSharedPrefs(fname, lname, email, customerId);
                            editor.apply();
                            goToMainActivity();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Account already exists", Toast.LENGTH_LONG).show();
                            showDialogMessageToGoToLogin("Email: " + email + " already exists.\nGo to login!");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(RegisterActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Try again!");

                } else if (error instanceof ParseError) {

                    Toast.makeText(RegisterActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Parse Error. Try again!");


                } else if (error instanceof ServerError) {

                    //user already exists
                    Toast.makeText(RegisterActivity.this, "User already exists. Go to login", Toast.LENGTH_LONG).show();
                    showDialogMessageToGoToLogin("User already exists. Go to login!");

                } else {

                    showDialogMessage("Oops! Something went wrong. Please try again!");


                }
                // LoginManager.getInstance().logOut();

                // bRegister.setActivated(true);
                //  bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {


            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myApplicationClass.add(stringRequest);


    }


    private boolean validEntries() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email

        if (tilFname.getEditText().getText().toString().trim().isEmpty()) {

            tilFname.getEditText().setError("Please enter your first name");


            return false;
        }

        if (tilLname.getEditText().getText().toString().trim().isEmpty()) {

            tilLname.getEditText().setError("Please enter your surname");


            return false;
        }


        if (tilEmail.getEditText().getText().toString().trim().isEmpty()) {

            tilEmail.getEditText().setError("Please enter your email");


            return false;
        }

        if (isValidEmaillId(tilEmail.getEditText().getText().toString().trim()) == false) {
            tilEmail.getEditText().setError("Enter valid email");
            return false;

        }

        if (tilPassword.getEditText().getText().toString().trim().isEmpty()) {

            tilPassword.getEditText().setError("Please enter your password");

            return false;
        }


        return true;
    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void saveUserDetailsInSharedPrefs(String fname, String lname, String email, int customerId) {

        editor.putString("fname", fname);
        editor.putString("lname", lname);
        editor.putString("email", email);
        editor.putBoolean("available", true);
        editor.putInt("customerId", customerId);
        editor.apply();

    }

    public void showDialogMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void showDialogMessageToGoToLogin(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void goToLoginActivity() {

        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    private void goToMainActivity() {

        logCompletedRegistrationEvent("email");
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        i.putExtra("beginning", 1);
        startActivity(i);
        finish();
    }

    public void logCompletedRegistrationEvent (String registrationMethod) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD, registrationMethod);
        myApplicationClass.getLogger().logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, params);
    }
}
