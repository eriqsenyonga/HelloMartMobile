package com.plexosysconsult.hellomartmobile;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodsFragment extends Fragment implements View.OnClickListener {

    RadioButton rbCOD, rbPesapal;
    CheckoutActivity checkoutActivity;
    Button bPlaceOrder;
    Cart cart;
    String URL_PLACE_ORDER_COD = "placeOrder.php";
    String URL_PLACE_ORDER_PESAPAL = "https://hellomart.ug/pesapal/placeOrderPesapal.php";
    View v;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    ProgressDialog progressDialog;
    String KEY_COD = "cash_on_delivery";
    String KEY_PESAPAL = "pesapal";
    BillingDetails billingDetails;
    String paymentMethod = KEY_COD;
    TextView tvPesapalDescription;
    SharedPreferences userSharedPrefs;


    int customerId = 0;

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
        tvPesapalDescription = v.findViewById(R.id.tv_description_pesapal);


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rbCOD.setTypeface(myApplicationClass.getBoldTypeface());
        bPlaceOrder.setTypeface(myApplicationClass.getBoldTypeface());
        rbPesapal.setTypeface(myApplicationClass.getBoldTypeface());
        tvPesapalDescription.setTypeface(myApplicationClass.getRegularTypeface());

        userSharedPrefs = getActivity().getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        // editor = userSharedPrefs.edit();

        if (userSharedPrefs.getBoolean("available", false)) {

            customerId = userSharedPrefs.getInt("customerId", 0);

        }

        checkoutActivity = (CheckoutActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Placing order");
        progressDialog.setIndeterminate(true);

        rbCOD.setOnClickListener(this);
        rbPesapal.setOnClickListener(this);
        bPlaceOrder.setOnClickListener(this);


    }

    @Override
    public void onResume() {
        super.onResume();

        checkoutActivity.setActionBarSubtitle("Payment Method");

        billingDetails = myApplicationClass.getBillingDetails();

    }

    @Override
    public void onClick(View view) {
        if (view == rbCOD) {

            bPlaceOrder.setText("Place Order");
            paymentMethod = KEY_COD;


        }

        if (view == rbPesapal) {

            bPlaceOrder.setText("Pay Online");
            paymentMethod = KEY_PESAPAL;

        }


        if (view == bPlaceOrder) {

            if (rbPesapal.isChecked()) {
                //place order through pesapal order, get the order number and pass to iframe

                placeOrder();
                //show pesapal iframe

                //

            } else if (rbCOD.isChecked()) {
                //place order and kiwedde

                placeOrder();


            }


        }


    }

    private void placeOrder() {

        progressDialog.show();

        bPlaceOrder.setEnabled(false);

        try {
            JSONObject orderObject = new JSONObject();

            if (paymentMethod.equals(KEY_COD)) {

                orderObject.put("payment_method", "COD");
                orderObject.put("payment_method_title", "Cash on Delivery");
                orderObject.put("set_paid", true);
                //   orderObject.put("status", "processing");

            } else if (paymentMethod.equals(KEY_PESAPAL)) {

                orderObject.put("payment_method", "pesapal");
                orderObject.put("payment_method_title", "Pesapal Payment");
                orderObject.put("set_paid", false);
                //   orderObject.put("status", "processing");

            }


            orderObject.put("shipping_total", 5000);

            //add billing jsonArray

            JSONArray billingJsonArray = new JSONArray();

            JSONObject billingJson = new JSONObject();
            billingJson.put("first_name", billingDetails.getFirstName());
            billingJson.put("last_name", billingDetails.getSurname());
            billingJson.put("address_1", billingDetails.getDeliveryAddress());
            billingJson.put("address_2", "");
            billingJson.put("email", billingDetails.getEmailAddress());
            billingJson.put("phone", billingDetails.getPhoneNumber());
            billingJson.put("city", billingDetails.getTownCity());
            billingJson.put("country", "UG");
            billingJson.put("state", "Uganda");
            billingJson.put("postcode", "256");

            billingJsonArray.put(billingJson);

            orderObject.put("billing_address", billingJson);

            //add shipping jsonArray

            JSONArray shippingJsonArray = new JSONArray();

            JSONObject shippingJson = new JSONObject();
            shippingJson.put("first_name", billingDetails.getFirstName());
            shippingJson.put("last_name", billingDetails.getSurname());
            shippingJson.put("address_1", billingDetails.getDeliveryAddress());
            shippingJson.put("address_2", "");
            shippingJson.put("city", billingDetails.getTownCity());
            shippingJson.put("country", "UG");
            shippingJson.put("state", "Uganda");
            shippingJson.put("postcode", "256");

            shippingJsonArray.put(shippingJson);

            orderObject.put("shipping_address", shippingJson);

            //add line_items json array
            JSONArray lineItemsJsonArray = new JSONArray();

            cart = myApplicationClass.getCart();

            List<CartItem> cartItems = cart.getCurrentCartItems();

            for (CartItem cartItem : cartItems) {

                JSONObject lineItem = new JSONObject();

                if (cartItem.isVariation()) {
                    lineItem.put("product_id", cartItem.getItemVariationId());
                } else {

                    lineItem.put("product_id", cartItem.getItemId());
                }

                lineItem.put("quantity", cartItem.getQuantity());

                lineItemsJsonArray.put(lineItem);
            }

            orderObject.put("line_items", lineItemsJsonArray);


            //add shipping_lines object

            JSONArray shippingLinesJsonArray = new JSONArray();

            JSONObject shippingLinesObject = new JSONObject();

            shippingLinesObject.put("method_id", "Flat Rate");
            shippingLinesObject.put("method_title", "Delivery Fee");
            shippingLinesObject.put("total", 5000);

            shippingLinesJsonArray.put(shippingLinesObject);


            orderObject.put("shipping_lines", shippingLinesJsonArray);


            Log.d("order", orderObject.toString());

            if (paymentMethod.equals(KEY_COD)) {

                placeOrderOnlineCOD(orderObject);

            } else if (paymentMethod.equals(KEY_PESAPAL)) {

                placeOrderOnlinePesapal(orderObject);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void placeOrderOnlineCOD(final JSONObject orderObject) {

        StringRequest placeOrderOnlineRequest = new StringRequest(Request.Method.POST, MyApplicationClass.generalUrl + URL_PLACE_ORDER_COD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //Log.d("COD ORDER", response);

                            JSONObject jsonResponse = new JSONObject(response);
                            logPurchasedEvent(myApplicationClass.getCart().getCurrentCartItems().size(), myApplicationClass.getCart().getCartGrandTotalLong());

                            Intent i = new Intent(getActivity(), OrderSuccessActivity.class);
                            progressDialog.cancel();
                            startActivity(i);
                            getActivity().finish();
                            bPlaceOrder.setEnabled(true);
                        } catch (JSONException e) {

                            e.printStackTrace();

                            bPlaceOrder.setEnabled(true);
                            progressDialog.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setMessage("JSON error response");

                            Dialog dialog = builder.create();
                            dialog.show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.cancel();

                        bPlaceOrder.setEnabled(true);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            builder.setMessage("Order could not be placed \n \nConnection timed out!");


                        } else if (error instanceof NoConnectionError) {

                            builder.setMessage("Order could not be placed \n \nCheck internet connection!");

                        } else if (error instanceof ParseError) {

                            builder.setMessage("Oops! Something went wrong. Data unreadable");

                        }


                        Dialog dialog = builder.create();
                        dialog.show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("order_details_json_string", orderObject.toString());
                map.put("customer_id", "" + customerId);
                map.put("order_notes", myApplicationClass.getBillingDetails().getOrderNotes());

                return map;
            }
        };


        placeOrderOnlineRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        myApplicationClass.add(placeOrderOnlineRequest);


    }

    private void placeOrderOnlinePesapal(final JSONObject orderObject) {

        StringRequest placeOrderOnlineRequest = new StringRequest(Request.Method.POST, URL_PLACE_ORDER_PESAPAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.d("PESAPAL ORDER", response);

                            JSONObject jsonResponse = new JSONObject(response);

                            Long orderId = jsonResponse.getJSONObject("order").getLong("id");


                            //  logPurchase(jsonResponse.getJSONObject("order").getString("total"));
                            logPurchasedEvent(myApplicationClass.getCart().getCurrentCartItems().size(), myApplicationClass.getCart().getCartGrandTotalLong());
                            checkoutActivity.showPesapalIframe(orderId);


                            //   Intent i = new Intent(getActivity(), OrderSuccessActivity.class);
                            progressDialog.cancel();
                            //  startActivity(i);
                            //  getActivity().finish();
                            bPlaceOrder.setEnabled(true);
                        } catch (JSONException e) {

                            e.printStackTrace();

                            bPlaceOrder.setEnabled(true);
                            progressDialog.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.setMessage("JSON error response");

                            Dialog dialog = builder.create();
                            dialog.show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.cancel();

                        bPlaceOrder.setEnabled(true);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });


                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            builder.setMessage("Order could not be placed \n \nConnection timed out!");


                        } else if (error instanceof NoConnectionError) {

                            builder.setMessage("Order could not be placed \n \nCheck internet connection!");

                        } else if (error instanceof ParseError) {

                            builder.setMessage("Oops! Something went wrong. Data unreadable");

                        }


                        Dialog dialog = builder.create();
                        dialog.show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("order_details_json_string", orderObject.toString());
                map.put("customer_id", "" + customerId);
                map.put("order_notes", myApplicationClass.getBillingDetails().getOrderNotes());

                return map;
            }
        };


        placeOrderOnlineRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        myApplicationClass.add(placeOrderOnlineRequest);


    }

    public void logPurchasedEvent (int numItems , long price) {
        Bundle params = new Bundle();
        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "UGX");
        myApplicationClass.getLogger().logPurchase(BigDecimal.valueOf(price), Currency.getInstance("UGX"),params);
    }


}
