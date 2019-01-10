package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.appevents.AppEventsConstants;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    Context context;
    List<Category> mainCategories;
    List<Category> allCategories;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    MainActivity mainActivity;


    public RecyclerViewAdapterHome(Context c, List<Category> categoryList) {

        context = c;
        mainCategories = new ArrayList<>();
        allCategories = new ArrayList<>();

        allCategories = categoryList;

        for (Category cat : allCategories) {

            if (cat.getParentId() == 0) {

                mainCategories.add(cat);

            }

        }

        mainActivity = (MainActivity) context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_shop, parent, false);
            return new RecyclerViewAdapterHome.HeaderViewHolder(v, context);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category_grid, parent, false);
            return new RecyclerViewAdapterHome.CategoryItemViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewAdapterHome.HeaderViewHolder) {
            RecyclerViewAdapterHome.HeaderViewHolder headerHolder = (RecyclerViewAdapterHome.HeaderViewHolder) holder;

        } else {

            RecyclerViewAdapterHome.CategoryItemViewHolder categoryItemViewHolder = (RecyclerViewAdapterHome.CategoryItemViewHolder) holder;

            final Category category = mainCategories.get(position - 1);

            categoryItemViewHolder.tvCategoryName.setText(category.getName());


            Glide
                    .with(context)
                    .load(category.getImageUrl())
                    .apply(new RequestOptions().centerCrop())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }


                    })
                    .into(categoryItemViewHolder.ivCategoryImage);


            categoryItemViewHolder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {

                    int categoryId = category.getId();

                    logViewedContentEvent(category.getName());

                    List<Category> subCategoriesList = new ArrayList<>();

                    //get all subcategories for this category with id _
                    for (Category cat : allCategories) {

                        if (cat.getParentId() == categoryId) {

                            subCategoriesList.add(cat);

                        }


                    }

                    //check if there are subcategories or not
                    if (subCategoriesList.isEmpty()) {


                        //here we will display the products for this particular category

                        mainActivity.showProductsInCategory(category.getSlug(), category.getName());


                    } else {

                        //if subcategories exist, save them in MyApplicationClass and show in subCategoryFragment
                        myApplicationClass.setSubCategoryList(subCategoriesList);
                        myApplicationClass.setLastSubCategory(category.getName());

                        mainActivity.showSubcategories(category.getName());

                    }


                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        //   if (isPositionHeader(position)) {
        //     return TYPE_HEADER;
        //  } else

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position) {

        if (position == 0) {

            return true;
        } else {
            return false;
        }

    }


    @Override
    public int getItemCount() {
        return mainCategories.size() + 1;
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvGroceryHousehold, tvAtYourDoorstep, tvOnDemand;
        EditText etSearch;
        MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
        ImageButton ibSearch;
        Context context;

        public HeaderViewHolder(View itemView, Context c) {
            super(itemView);

            context = c;
            tvGroceryHousehold = itemView.findViewById(R.id.tv_grocery_household);
            tvAtYourDoorstep = itemView.findViewById(R.id.tv_at_your_doorstep);
            tvOnDemand = itemView.findViewById(R.id.tv_on_demand);
            etSearch = itemView.findViewById(R.id.et_search);
            ibSearch = itemView.findViewById(R.id.ib_search);


            tvGroceryHousehold.setTypeface(myApplicationClass.getBoldTypeface());
            tvAtYourDoorstep.setTypeface(myApplicationClass.getRegularTypeface());
            tvOnDemand.setTypeface(myApplicationClass.getBoldTypeface());
            etSearch.setTypeface(myApplicationClass.getRegularTypeface());

            etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (!etSearch.getText().toString().isEmpty()) {
                            performSearch();
                            return true;
                        }
                    }
                    return false;
                }
            });

            ibSearch.setOnClickListener(this);

        }

        private void performSearch() {

            String queryString = etSearch.getText().toString().trim();
            Intent i = new Intent(context, SearchableActivity.class);
            i.putExtra("queryString", queryString);
            context.startActivity(i);

        }

        @Override
        public void onClick(View v) {
            if (v == ibSearch) {

                if (!etSearch.getText().toString().isEmpty()) {
                    //if etSearch is not empty

                    performSearch();


                }else{
                    etSearch.setError("Search item");
                }


            }
        }
    }

    private class CategoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCategoryName;
        ItemClickListener itemClickListener;
        ImageView ivCategoryImage;
        MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


        public CategoryItemViewHolder(View itemView) {
            super(itemView);

            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            ivCategoryImage = itemView.findViewById(R.id.iv_category_image);
            tvCategoryName.setTypeface(myApplicationClass.getBoldTypeface());
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    public void logViewedContentEvent (String category) {
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, "Category");
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, category);
        myApplicationClass.getLogger().logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,  params);
    }
}
