package com.plexosysconsult.hellomartmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Button bProceed, bResetPassword;
    TextInputLayout tilEmail, tilPassword1, tilPassword2;
    LinearLayout linlayPasswords;
    TextView tvTagline, tvLabelResetPassword;

    private static final String URL_GET_CUSTOMER_BY_EMAIL = "http://www.hellomartug.com/example/getCustomerByEmail.php";
    private static final String URL_RESET_PASSWORD = "http://www.hellomartug.com/example/resetPassword.php";


    int customerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        bProceed = findViewById(R.id.btn_proceed);
        bResetPassword = findViewById(R.id.btn_reset_password);
        tilEmail = findViewById(R.id.til_email);
        tilPassword1 = findViewById(R.id.til_password_1);
        tilPassword2 = findViewById(R.id.til_password_2);
        linlayPasswords = findViewById(R.id.linlay_passwords);
        tvTagline = findViewById(R.id.tv_tagline);
        tvLabelResetPassword = findViewById(R.id.tv_label_reset_password);

        setTypefacesForViews();

        bProceed.setOnClickListener(this);

    }

    private void setTypefacesForViews() {

        bProceed.setTypeface(myApplicationClass.getBoldTypeface());
        bResetPassword.setTypeface(myApplicationClass.getBoldTypeface());
        tilEmail.setTypeface(myApplicationClass.getBoldTypeface());
        tilEmail.getEditText().setTypeface(myApplicationClass.getRegularTypeface());
        tilPassword1.setTypeface(myApplicationClass.getBoldTypeface());
        tilPassword1.getEditText().setTypeface(myApplicationClass.getRegularTypeface());
        tilPassword2.setTypeface(myApplicationClass.getBoldTypeface());
        tilPassword2.getEditText().setTypeface(myApplicationClass.getRegularTypeface());
        tvTagline.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelResetPassword.setTypeface(myApplicationClass.getBoldTypeface());

    }

    @Override
    public void onClick(View v) {
        if (v == bProceed) {
            //validate entRy whether it is empty or has valid email address
            //check whether email is there, if yes, continue to get new passwords..if no, go to register now


            bProceed.setActivated(false);

            if (validateEmail()) {

                //do something online
                bProceed.setText("Please Wait...");
                checkEmailAvailability(tilEmail.getEditText().getText().toString().trim());

            }

        }

        if(v == bResetPassword){

            //update customer password. show success. go back to login page

            bResetPassword.setActivated(false);

            if (validatePasswords()) {


                bResetPassword.setText("Please Wait...");

                resetPasswordForUserWithId(tilPassword2.getEditText().getText().toString().trim());


            }

        }
    }

    private void resetPasswordForUserWithId(final String password) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_RESET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        //Log.d("jsonresponse", response);

                        bResetPassword.setText("Success");
                        showDialogSuccessPasswordChange("Password reset successfully");
/*
                            JSONObject jsonResponse = new JSONObject(response);

                            customerId = jsonResponse.getJSONObject("customer").getInt("id");
                            Toast.makeText(PasswordResetActivity.this, "customer id: " + customerId, Toast.LENGTH_LONG).show();

                            bProceed.setVisibility(View.GONE);
                            linlayNewPassword.setVisibility(View.VISIBLE);
*/


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(ResetPassword.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Check your internet connection and try again!");

                } else if (error instanceof ParseError) {

                    Toast.makeText(ResetPassword.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Parse Error. Try again!");


                } else {

                    showDialogMessage("Oops! Something went wrong. Please try again!");


                }
                bResetPassword.setText("Reset Password");
                bResetPassword.setActivated(true);
                //   LoginManager.getInstance().logOut();

                // bRegister.setActivated(true);
                //  bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer_id", "" + customerId);
                map.put("password", password);

                return map;
            }

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


    private boolean validateEmail() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email


        if (tilEmail.getEditText().getText().toString().trim().isEmpty()) {

            tilEmail.getEditText().setError("Please enter your email");
            bProceed.setActivated(true);

            return false;
        }

        if (isValidEmaillId(tilEmail.getEditText().getText().toString().trim()) == false) {
            tilEmail.getEditText().setError("Enter valid email");
            bProceed.setActivated(true);

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

    private boolean validatePasswords() {
        //method to validate that the input fields are valid eg if email field then it user should have entered a valid email


        if (tilPassword1.getEditText().getText().toString().trim().isEmpty()) {

            tilPassword1.getEditText().setError("Please enter new password");
            bResetPassword.setActivated(true);


            return false;
        }

        if (tilPassword2.getEditText().getText().toString().trim().isEmpty()) {

            tilPassword2.getEditText().setError("Please confirm password");
            bResetPassword.setActivated(true);

            return false;
        }

        if (!tilPassword2.getEditText().getText().toString().trim().equals(tilPassword1.getEditText().getText().toString().trim())) {

            tilPassword2.getEditText().setError("Passwords dont match");
            bResetPassword.setActivated(true);

            return false;
        }


        return true;
    }

    private void checkEmailAvailability(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL_GET_CUSTOMER_BY_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        // mTextView.setText("Response is: "+ response.substring(0,500));

                        //Log.d("jsonresponse", response);

                        try {


                            JSONObject jsonResponse = new JSONObject(response);

                            customerId = jsonResponse.getJSONObject("customer").getInt("id");
                            Toast.makeText(ResetPassword.this, "customer id: " + customerId, Toast.LENGTH_LONG).show();

                            bProceed.setVisibility(View.GONE);
                            linlayPasswords.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(RegisterActivity.this, "Account already exists", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");

                //   pbLoading.setVisibility(View.GONE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(ResetPassword.this, "Connection could not be established", Toast.LENGTH_LONG).show();
                    showDialogMessage("Connection could not be established. Check your internet connection and try again!");

                } else if (error instanceof ParseError) {

                    Toast.makeText(ResetPassword.this, "Oops! Something went wrong. Data unreadable", Toast.LENGTH_LONG).show();
                    showDialogMessage("Oops! Something went wrong. Parse Error. Try again!");


                } else if (error instanceof ServerError) {

                    //user already exists
                    Toast.makeText(ResetPassword.this, "Email doesnot exist. Register now", Toast.LENGTH_LONG).show();
                    showDialogMessageToGoToRegister("Email doesnot exist. Register now");

                } else {

                    showDialogMessage("Oops! Something went wrong. Please try again!");


                }
                bProceed.setText("Proceed");
                bProceed.setActivated(true);
                //   LoginManager.getInstance().logOut();

                // bRegister.setActivated(true);
                //  bRegister.setText("Register");
                //  errorLayout.setVisibility(View.VISIBLE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);

                return map;
            }

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

    public void showDialogMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void showDialogSuccessPasswordChange(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
        builder.setMessage(message);
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent i = new Intent(ResetPassword.this, LoginActivity.class);
                startActivity(i);
                finish();


            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void showDialogMessageToGoToRegister(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPassword.this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent i = new Intent(ResetPassword.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        builder.setCancelable(false);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent i = new Intent(ResetPassword.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent i = new Intent(ResetPassword.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
