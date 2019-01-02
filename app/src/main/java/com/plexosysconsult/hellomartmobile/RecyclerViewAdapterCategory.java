package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 7/28/2017.
 */

class RecyclerViewAdapterCategory extends RecyclerView.Adapter<RecyclerViewAdapterCategory.ViewHolder> {

    Context context;
    List<Category> categoryList;
    List<Category> categoriesToShow;
    MainActivity mainActivity;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


    public RecyclerViewAdapterCategory(Context c, List<Category> categoryList) {

        context = c;
        this.categoryList = categoryList;
        categoriesToShow = new ArrayList<>();

        for (Category cat : categoryList) {

            if (cat.getParentId() == 0) {


                categoriesToShow.add(cat);

            }


        }

        mainActivity = (MainActivity) context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Category category = (Category) categoriesToShow.get(position);

        holder.tvName.setText(category.getName());
        holder.tvCount.setText("(" + category.getCount() + ")");

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                int categoryId = category.getId();

                List<Category> subCategoriesList = new ArrayList<>();

                //get all subcategories for this category with id _
                for (Category cat : categoryList) {

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

    @Override
    public int getItemCount() {
        return categoriesToShow.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName, tvCount;
        ItemClickListener itemClickListener;
        MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);

            tvName.setTypeface(myApplicationClass.getRegularTypeface());
            tvCount.setTypeface(myApplicationClass.getRegularTypeface());

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
}
