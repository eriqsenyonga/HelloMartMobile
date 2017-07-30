package com.plexosysconsult.hellomartmobile;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 7/28/2017.
 */

public class MyApplicationClass extends Application {

    private RequestQueue mRequestQueue;
    private static MyApplicationClass mInstance;
    public static final String TAG = MyApplicationClass.class.getName();
    private String generalUrl = "http://hellomartug.com/";
    List<Category> categoryList;
    List<Category> subCategoryList;
    String lastSubCategory;
    Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        cart = new Cart(getApplicationContext());
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
}
