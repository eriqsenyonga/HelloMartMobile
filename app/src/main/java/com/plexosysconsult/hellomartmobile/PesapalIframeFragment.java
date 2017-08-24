package com.plexosysconsult.hellomartmobile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import im.delight.android.webview.AdvancedWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PesapalIframeFragment extends Fragment implements AdvancedWebView.Listener {

    ProgressBar pbLoading;
    View v;
    AdvancedWebView aWebView;
    String callbackUrl = "pesaOrderSuccess";

    Cart cart;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    BillingDetails billingDetails;

    public PesapalIframeFragment() {
        // Required empty public constructor
    }


    public static PesapalIframeFragment newInstance(Long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong("order_id", orderId);


        PesapalIframeFragment fragment = new PesapalIframeFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_pesapal_iframe, container, false);


        aWebView = (AdvancedWebView) v.findViewById(R.id.awebview);
        pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aWebView.setListener(getActivity(), this);
        aWebView.setVerticalScrollBarEnabled(true);

        billingDetails = myApplicationClass.getBillingDetails();
        cart = myApplicationClass.getCart();


        Bundle bundle = getArguments();

        Long orderId = bundle.getLong("order_id", 1234);

        Long grandTotal = cart.getCartGrandTotalLong();
        String firstName = billingDetails.getFirstName();
        String lastName = billingDetails.getSurname();
        String email = billingDetails.getEmailAddress();
        String phoneNumber = billingDetails.getPhoneNumber();

        aWebView.loadUrl("http://hellomartug.com/pesapal/Pesapal-iframe.php?amount=" + grandTotal
                + "&order_id=" + orderId
                + "&first_name=" + firstName
                + "&last_name=" + lastName
                + "&email=" + email
                + "&phonenumber=" + phoneNumber);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        aWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        pbLoading.setVisibility(View.VISIBLE);


    }

    @Override
    public void onPageFinished(String url) {
        pbLoading.setVisibility(View.GONE);

        if (url.contains(callbackUrl)) {

            Intent i = new Intent(getActivity(), OrderSuccessActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }


}
