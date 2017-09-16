package com.plexosysconsult.hellomartmobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoriesFragment extends Fragment {

    RecyclerView recyclerView;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    List<Category> subCategoryList;
    View v;
    MainActivity mainActivity;


    public SubCategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sub_categories, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        subCategoryList = new ArrayList<>();

        subCategoryList = myApplicationClass.getSubCategoryList();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       recyclerView.addItemDecoration(new ListDividerDecoration(getActivity()));

        recyclerView.setAdapter(new RecyclerViewAdapterSubCategory(getActivity(), subCategoryList));


    }


    @Override
    public void onResume() {
        super.onResume();

        mainActivity.setActionBarTitleAndSubtitle(getString(R.string.categories), myApplicationClass.getLastSubCategory());
        mainActivity.setNavigationViewCheckedItem(R.id.nav_categories);
    }
}
