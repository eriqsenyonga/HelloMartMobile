package com.plexosysconsult.hellomartmobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;
    View v;
    String URL_GET_ITEMS = "http://hellomartug.com/example/getAllProducts.php";
    String URL_GET_ITEMS_IN_CATEGORY = "http://hellomartug.com/example/getProductsInCategory.php";
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Item> itemsToShow;
    SwipeRefreshLayout swipeRefreshLayout;
    UsefulFunctions usefulFunctions;
    ProgressBar pbLoading;
    LinearLayout errorLayout;
    Button bReload;
    TextView tvErrorMsg;
    String categorySlug;
    int KEY_WHICH_GET;
    int ALL_ITEMS = 1;
    int CATEGORY_ITEMS = 2;
    //  String jsonFileName = "fruits.json";


    public ShopFragment() {
        // Required empty public constructor
    }

    public static ShopFragment newInstance(String categorySlug) {
        Bundle bundle = new Bundle();
        bundle.putString("categorySlug", categorySlug);


        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);
        errorLayout = (LinearLayout) v.findViewById(R.id.error_layout);
        bReload = (Button) v.findViewById(R.id.b_reload);
        tvErrorMsg = (TextView) v.findViewById(R.id.tv_error_message);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        usefulFunctions = new UsefulFunctions(getActivity());

        recyclerView.hasFixedSize();
        //  recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));

        itemsToShow = new ArrayList();


        Bundle bundle = getArguments();

        if (bundle == null) {
            KEY_WHICH_GET = ALL_ITEMS;
            fetchItemsJson();
        } else {

            KEY_WHICH_GET = CATEGORY_ITEMS;

            categorySlug = bundle.getString("categorySlug");

            fetchItemsInCategoryJson();

        }

        bReload.setOnClickListener(this);


    }

    private void fetchItemsJson() {

        StringRequest itemRequest = new StringRequest(Request.Method.POST, URL_GET_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //  usefulFunctions.mCreateAndSaveFile(jsonFileName, response);

                        try {

                            // JSONObject jsonResponse = new JSONObject(usefulFunctions.mReadJsonData(jsonFileName));

                            JSONObject jsonResponse = new JSONObject(response);
                            putJsonIntoList(jsonResponse);
                            pbLoading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//
                        pbLoading.setVisibility(View.GONE);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            tvErrorMsg.setText("Connection could not be established");


                        } else if (error instanceof ParseError) {

                            tvErrorMsg.setText("Oops! Something went wrong. Data unreadable");

                        }
                        errorLayout.setVisibility(View.VISIBLE);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("page", "1");

                return map;
            }
        };

        itemRequest.setRetryPolicy(new RetryPolicy() {
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


        myApplicationClass.add(itemRequest);

    }

    private void fetchItemsInCategoryJson() {

        StringRequest itemRequest = new StringRequest(Request.Method.POST, URL_GET_ITEMS_IN_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //  usefulFunctions.mCreateAndSaveFile(jsonFileName, response);

                        try {

                            // JSONObject jsonResponse = new JSONObject(usefulFunctions.mReadJsonData(jsonFileName));

                            JSONObject jsonResponse = new JSONObject(response);
                            putJsonIntoList(jsonResponse);
                            pbLoading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//
                        pbLoading.setVisibility(View.GONE);

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            tvErrorMsg.setText("Connection could not be established");


                        } else if (error instanceof ParseError) {

                            tvErrorMsg.setText("Oops! Something went wrong. Data unreadable");

                        }
                        errorLayout.setVisibility(View.VISIBLE);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cat", categorySlug);

                return map;
            }
        };

        itemRequest.setRetryPolicy(new RetryPolicy() {
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


        myApplicationClass.add(itemRequest);

    }


    private void putJsonIntoList(JSONObject jsonResponse) {


        try {

            JSONArray fruits = jsonResponse.getJSONArray("products");

            for (int i = 0; i < fruits.length(); i++) {

                JSONObject fruitJSON = fruits.getJSONObject(i);

                Item fruit = new Item();

                fruit.setItemName(fruitJSON.getString("title"));
                fruit.setImageUrl(fruitJSON.getJSONArray("images").getJSONObject(0).getString("src"));
                fruit.setItemId(fruitJSON.getInt("id"));
                fruit.setItemPrice(fruitJSON.getString("price"));

                fruit.setItemShortDescription(usefulFunctions.stripHtml(fruitJSON.getString("short_description")));

                JSONArray variationArray = fruitJSON.getJSONArray("variations");

                if (variationArray.length() > 0) {

                    fruit.setHasVariations(true);

                    List<Item> variations = new ArrayList<>();

                    for (int j = 0; j < variationArray.length(); j++) {

                        JSONObject variationJSONObject = variationArray.getJSONObject(j);

                        Item variationFruit = new Item();
                        variationFruit.setItemId(variationJSONObject.getInt("id"));
                        variationFruit.setItemPrice(variationJSONObject.getString("price"));
                        variationFruit.setOptionUnit(variationJSONObject.getJSONArray("attributes").getJSONObject(0).getString("option"));

                        variations.add(variationFruit);

                    }

                    fruit.setItemVariations(variations);


                } else {

                    fruit.setHasVariations(false);
                }

                itemsToShow.add(fruit);

            }


        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();

        }


        recyclerView.setAdapter(new RecyclerViewAdapterVegetable(getActivity(), itemsToShow));


    }

    @Override
    public void onClick(View v) {
        if (v == bReload) {

            if (KEY_WHICH_GET == ALL_ITEMS) {
                fetchItemsJson();
            } else if (KEY_WHICH_GET == CATEGORY_ITEMS) {
                fetchItemsInCategoryJson();
            }


        }
    }
}
