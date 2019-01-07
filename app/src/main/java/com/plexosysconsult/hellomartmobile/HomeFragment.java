package com.plexosysconsult.hellomartmobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    String URL_GET_CATEGORIES = "http://www.hellomart.ug/example/getCategories.php";
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Category> categoryList;
    SwipeRefreshLayout swipeRefreshLayout;
    UsefulFunctions usefulFunctions;
    ProgressBar pbLoading;
    LinearLayout errorLayout;
    Button bReload;
    TextView tvErrorMsg;
    MainActivity mainActivity;
    GridLayoutManager gridLayoutManager;
    View v;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        pbLoading = v.findViewById(R.id.pb_loading);
        errorLayout = v.findViewById(R.id.error_layout);
        bReload = v.findViewById(R.id.b_reload);
        tvErrorMsg = v.findViewById(R.id.tv_error_message);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivity = (MainActivity) getActivity();


        usefulFunctions = new UsefulFunctions(getActivity());
        pbLoading.setVisibility(View.GONE);


        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (position) {
                    case 0:
                        return 3;

                    default:
                        return 1;
                }
            }
        });


        recyclerView.setLayoutManager(gridLayoutManager);
        //  recyclerView.addItemDecoration(new ListDividerDecoration(this));

        categoryList = new ArrayList<>();


        if (myApplicationClass.getCategoryList().isEmpty()) {

            fetchCategoryJson();

        } else {

            recyclerView.setAdapter(new RecyclerViewAdapterHome(getActivity(), myApplicationClass.getCategoryList()));
        }

        bReload.setOnClickListener(this);

    }


    private void fetchCategoryJson() {

        pbLoading.setVisibility(View.VISIBLE);

        StringRequest categoryRequest = new StringRequest(Request.Method.POST, URL_GET_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   usefulFunctions.mCreateAndSaveFile(jsonFileName, response);
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();

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
                map.put("category", "Fruits");

                return map;
            }
        };

        categoryRequest.setRetryPolicy(new RetryPolicy() {
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


        myApplicationClass.add(categoryRequest);

    }

    private void putJsonIntoList(JSONObject jsonResponse) {


        try {

            JSONArray categories = jsonResponse.getJSONArray("product_categories");

            for (int i = 0; i < categories.length(); i++) {

                JSONObject catJSON = categories.getJSONObject(i);

                Category category = new Category(catJSON.getInt("id"), usefulFunctions.stripHtml(catJSON.getString("name")), catJSON.getInt("parent"), catJSON.getInt("count"), catJSON.getString("slug"));
                category.setImageUrl(catJSON.getString("image"));
                categoryList.add(category);

            }


        } catch (JSONException localJSONException) {
            localJSONException.printStackTrace();

        }

        myApplicationClass.setCategoryList(categoryList);
        recyclerView.setAdapter(new RecyclerViewAdapterHome(getActivity(), categoryList));


    }


    @Override
    public void onClick(View view) {

        if (view == bReload) {

            errorLayout.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            fetchCategoryJson();


        }


    }


    @Override
    public void onResume() {
        super.onResume();


        mainActivity.removeSubtitle(getString(R.string.shop));
        mainActivity.setNavigationViewCheckedItem(R.id.nav_shop);


    }
}