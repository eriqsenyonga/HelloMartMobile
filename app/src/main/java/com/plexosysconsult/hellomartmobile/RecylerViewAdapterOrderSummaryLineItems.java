package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapterOrderSummaryLineItems extends RecyclerView.Adapter<RecylerViewAdapterOrderSummaryLineItems.ViewHolder> {

    List<OrderLineItem> orderLineItemList;
    Context context;
    /* NewOrderActivity newOrderActivity;
     NewOrderObject newOrderObject;*/
    BigDecimalClass bigDecimalClass;


    public RecylerViewAdapterOrderSummaryLineItems(Context c, List<OrderLineItem> lineItems) {

        context = c;
        orderLineItemList = new ArrayList<>();
        orderLineItemList = lineItems;
        bigDecimalClass = new BigDecimalClass(context);
      /*  newOrderActivity = (NewOrderActivity) context;
        newOrderObject = newOrderActivity.getNewOrderObject();*/


    }


    @NonNull
    @Override
    public RecylerViewAdapterOrderSummaryLineItems.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary_line_item, parent, false);

        RecylerViewAdapterOrderSummaryLineItems.ViewHolder viewHolder = new RecylerViewAdapterOrderSummaryLineItems.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewAdapterOrderSummaryLineItems.ViewHolder holder, int position) {

        final OrderLineItem orderLineItem = orderLineItemList.get(position);
        holder.tvItemName.setText(orderLineItem.getItemName());
        holder.tvItemQuantity.setText("x" + orderLineItem.getItemQuantity());
        holder.tvTotalAmount.setText(bigDecimalClass.convertStringToDisplayCurrencyString(orderLineItem.getItemTotalPrice()));


    }

    @Override
    public int getItemCount() {
        return orderLineItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName, tvItemQuantity, tvTotalAmount;

        ItemClickListener itemClickListener;

        MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();


        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvItemQuantity = (TextView) itemView.findViewById(R.id.tv_qty);
            tvTotalAmount = (TextView) itemView.findViewById(R.id.tv_total);

            tvItemName.setTypeface(myApplicationClass.getRegularTypeface());
            tvItemQuantity.setTypeface(myApplicationClass.getRegularTypeface());
            tvTotalAmount.setTypeface(myApplicationClass.getRegularTypeface());

        }
/*
        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(v, getAdapterPosition(), false);
        }*/
    }
}
