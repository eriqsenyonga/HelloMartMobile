package com.plexosysconsult.hellomartmobile;

import android.app.Application;
import android.graphics.Typeface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 7/28/2017.
 */

public class MyApplicationClass extends Application {

    private RequestQueue mRequestQueue;
    private static MyApplicationClass mInstance;
    public static final String TAG = MyApplicationClass.class.getName();
    static String generalUrl = "https://hellomart.ug/example/";
    List<Category> categoryList;
    List<Category> subCategoryList;
    String lastSubCategory;
    Cart cart;
    BillingDetails billingDetails;
    Typeface productSansBold, productSansRegular;
    List<OrderLineItem> orderLineItems;
    AppEventsLogger logger;

    @Override
    public void onCreate() {
        super.onCreate();

        logger = AppEventsLogger.newLogger(this);

        mInstance = this;

        productSansBold = Typeface.createFromAsset(this.getAssets(), "fonts/ProductSansBold.ttf");
        productSansRegular = Typeface.createFromAsset(this.getAssets(), "fonts/ProductSansRegular.ttf");

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        billingDetails = new BillingDetails();
        cart = new Cart(getApplicationContext());
        orderLineItems = new ArrayList<>();
    }


    public static synchronized MyApplicationClass getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {

        mRequestQueue.cancelAll(TAG);
    }


    public void setCategoryList(List<Category> categories) {


        categoryList = categories;


    }


    public List<Category> getCategoryList() {

        return categoryList;


    }

    public void setSubCategoryList(List<Category> subCategories) {

        subCategoryList = subCategories;

    }


    public List<Category> getSubCategoryList() {

        return subCategoryList;


    }

    public String getLastSubCategory() {
        return lastSubCategory;
    }

    public void setLastSubCategory(String lastSubCategory) {
        this.lastSubCategory = lastSubCategory;
    }

    public Cart getCart() {
        return cart;
    }

    public void updateCart(Cart updatedCart) {
        cart = updatedCart;
    }

    public BillingDetails getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(BillingDetails billingDetails) {
        this.billingDetails = billingDetails;
    }

    public Typeface getBoldTypeface() {
        return productSansBold;
    }

    public Typeface getRegularTypeface() {
        return productSansRegular;
    }

    public void setSelectedOrderLineItems(List<OrderLineItem> orderLineItems) {

        this.orderLineItems = orderLineItems;

    }

    public List<OrderLineItem> getSelectedOrderLineItems() {
        return orderLineItems;
    }

    public AppEventsLogger getLogger(){
        return logger;
    }

}
