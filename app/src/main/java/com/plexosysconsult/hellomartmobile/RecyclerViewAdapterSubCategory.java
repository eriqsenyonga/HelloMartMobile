package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eric on 7/28/2017.
 */

public class RecyclerViewAdapterSubCategory extends RecyclerView.Adapter<RecyclerViewAdapterSubCategory.ViewHolder> {


    Context context;
    List<Category> subCategories;

    MainActivity mainActivity;


    public RecyclerViewAdapterSubCategory(Context c, List<Category> subCategoryList) {


        context = c;
        subCategories = subCategoryList;
        mainActivity = (MainActivity) context;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category, parent, false);

        RecyclerViewAdapterSubCategory.ViewHolder viewHolder = new RecyclerViewAdapterSubCategory.ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final Category category = (Category) subCategories.get(position);

        holder.tvName.setText(category.getName());
        holder.tvCount.setText("(" + category.getCount() + ")");


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                mainActivity.showProductsInCategory(category.getSlug(), category.getName());


            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName, tvCount;
        ItemClickListener itemClickListener;
        MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvCount = itemView.findViewById(R.id.tv_count);

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
