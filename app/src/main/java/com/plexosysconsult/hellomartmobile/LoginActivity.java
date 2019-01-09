package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogin, bRegister;
    TextView tvLabelEmail, tvLabelPassword, tvTagline, tvForgotPassword, tvLabelNotRegistered, tvContinueAsGuest, tvLabelSignIn;
    EditText etEmail, etPassword;
    private static final String LOGIN_URL = "https://www.hellomart.ug/api/user/generate_auth_cookie/";
    SharedPreferences userSharedPrefs;
    SharedPreferences.Editor editor;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvLabelSignIn = findViewById(R.id.tv_label_sign_in);
        bLogin = findViewById(R.id.btn_login);
        bRegister = findViewById(R.id.btn_register);
        tvContinueAsGuest = findViewById(R.id.tv_continue_as_guest);
        tvLabelNotRegistered = findViewById(R.id.tv_label_not_registered);
        tvTagline = findViewById(R.id.tv_tagline);
        tvLabelEmail = findViewById(R.id.tv_label_email);
        tvLabelPassword = findViewById(R.id.tv_label_password);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        setTypefacesForViews();

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        editor = userSharedPrefs.edit();


        if (userSharedPrefs.getBoolean("available", false)) {

            goToMainActivity();


        }

        bLogin.setOnClickListener(this);
        bRegister.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvContinueAsGuest.setOnClickListener(this);
    }

    private void setTypefacesForViews() {
        tvTagline.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelEmail.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelPassword.setTypeface(myApplicationClass.getBoldTypeface());
        tvForgotPassword.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelSignIn.setTypeface(myApplicationClass.getBoldTypeface());

        etPassword.setTypeface(myApplicationClass.getRegularTypeface());
        etEmail.setTypeface(myApplicationClass.getRegularTypeface());
        bLogin.setTypeface(myApplicationClass.getBoldTypeface());
        bRegister.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelNotRegistered.setTypeface(myApplicationClass.getBoldTypeface());
        tvContinueAsGuest.setTypeface(myApplicationClass.getBoldTypeface());
    }

    @Override
    public void onClick(View v) {

        if (v == bLogin) {
             /*
            1. First check whether the form is full with email and password
            2. Submit email and password to server
            3. Receive response from the server to know whether details are there or not
            4. If details are there, save them to SharedPrefs and move on to MainActivity
            5. If details not there, show error and do nothing
            6.
             */

            bLogin.setActivated(false);
            bLogin.setText("Please Wait...");

            if (validEntries()) {

                validateDetailsWithServer();
                //                finish();
            }
        }

        if (v == bRegister) {
            //open Registration page
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);

        }

        if (v == tvContinueAsGuest) {
            //continue without logging in
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            i.putExtra("beginning", 1);
            startActivity(i);
        }

        if (v == tvForgotPassword) {
            //open forgot password page and reset the password
            Intent i = new Intent(LoginActivity.this, ResetPassword.class);
            startActivity(i);
        }

    }

    private boolean validEntries() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email

        if (etEmail.getText().toString().trim().isEmpty()) {

            etEmail.setError("Please enter your email");
            bLogin.setActivated(true);
            bLogin.setText("Sign In Now");

            return false;
        }

        if (isValidEmaillId(etEmail.getText().toString().trim()) == false) {
            etEmail.setError("Enter valid email");
            bLogin.setActivated(true);
            bLogin.setText("Sign In Now");
            return false;

        }

        if (etPassword.getText().toString().trim().isEmpty()) {

            etPassword.setError("Please enter your password");
            bLogin.setActivated(true);
            bLogin.setText("Sign In Now");
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

    private void validateDetailsWithServer() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                LOGIN_URL + "?email=" + etEmail.getText().toString().trim() + "&password=" + etPassword.getText().toString().trim(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        Log.d("jsonresponse", response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            checkTheReturnedJson(jsonResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Email not available", Toast.LENGTH_LONG).show();
                            bLogin.setActivated(true);
                            bLogin.setText("Login");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(LoginActivity.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Check your internet connection!");
                    bLogin.setActivated(true);
                    bLogin.setText("Sign In");

                } else if (error instanceof ParseError) {

                    Toast.makeText(LoginActivity.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Try again!");
                    bLogin.setActivated(true);
                    bLogin.setText("Sign In");

                } else {
                    Toast.makeText(LoginActivity.this, "Email not available", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email not registered");
                    bLogin.setActivated(true);
                    bLogin.setText("Sign In");


                }

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

    private void checkTheReturnedJson(JSONObject jsonResponse) {

        try {
            String status = jsonResponse.getString("status");
            if (status.equalsIgnoreCase("error")) {
                //if details have a zib

                String errorDetails = jsonResponse.getString("error");

                if (errorDetails.equalsIgnoreCase("email does not exist.")) {
                    //if email does not exist
                    etEmail.setError("Email not registered");

                } else {

                    etEmail.setError("Wrong email or password");

                }

                bLogin.setActivated(true);
                bLogin.setText("Sign In");


            } else if (status.equalsIgnoreCase("ok")) {
                //if details are fine and the login is de
                /*
                1. Extract the details we want and then save them and move on to next step
                 */


                JSONObject userDetailsJson = jsonResponse.getJSONObject("user");

                String fname = userDetailsJson.getString("firstname");
                String lname = userDetailsJson.getString("lastname");
                String email = userDetailsJson.getString("email");
                int customerId = userDetailsJson.getInt("id");

                Toast.makeText(this, "user authenticated", Toast.LENGTH_LONG).show();

                saveUserDetailsInSharedPrefs(fname, lname, email, customerId);
                editor.putBoolean("facebooklogin", false);
                bLogin.setActivated(true);
                bLogin.setText("Login");

                goToMainActivity();


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void goToMainActivity() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("beginning", 1);
        startActivity(i);
        finish();
    }


}
