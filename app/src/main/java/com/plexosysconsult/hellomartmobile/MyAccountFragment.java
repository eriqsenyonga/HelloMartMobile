package com.plexosysconsult.hellomartmobile;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import im.delight.android.webview.AdvancedWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment implements AdvancedWebView.Listener {

    ProgressBar pbLoading;
    WebView webView;
    View v;
    AdvancedWebView aWebView;
    String callbackUrl = "yourdomain.com";


    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_account, container, false);

        webView = (WebView) v.findViewById(R.id.webview);
        aWebView = (AdvancedWebView) v.findViewById(R.id.awebview);
        pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);

        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webView.setWebViewClient(new MyBrowser());

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        webView.loadUrl("http://hellomartug.com/pesapal/Pesapal-iframe.php");


        aWebView.setListener(getActivity(), this);
        aWebView.loadUrl("http://hellomartug.com/pesapal/Pesapal-iframe.php");


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

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
