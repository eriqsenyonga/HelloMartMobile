package com.plexosysconsult.hellomartmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    List<OrderLineItem> orderLineItems;


    TextView tvOrderNumber, tvOrderAmount, tvOrderDate, tvOrderStatus, tvOrderNumberOfItems, tvOrderTime, tvLabelDeliveryDetails,
            tvAddress1, tvAddress2, tvDeliveryFee;
    Toolbar toolbar;
    Intent i;
    Order order;
    BigDecimalClass bigDecimalClass;
    ConversionClass mCC;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    RecyclerView recyclerView;
    RecylerViewAdapterOrderSummaryLineItems adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvAddress1 = findViewById(R.id.tv_address_1);
        tvAddress2 = findViewById(R.id.tv_address_2);
        tvDeliveryFee = findViewById(R.id.tv_delivery_fee);
        tvLabelDeliveryDetails = findViewById(R.id.tv_label_delivery_details);
        recyclerView = findViewById(R.id.recycler_view);
        tvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
        tvOrderAmount = (TextView) findViewById(R.id.tv_order_amount);
        tvOrderDate = (TextView) findViewById(R.id.tv_order_date);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvOrderNumberOfItems = (TextView) findViewById(R.id.tv_number_of_items);


        tvAddress1.setTypeface(myApplicationClass.getRegularTypeface());
        tvAddress2.setTypeface(myApplicationClass.getRegularTypeface());
        tvDeliveryFee.setTypeface(myApplicationClass.getRegularTypeface());
        tvLabelDeliveryDetails.setTypeface(myApplicationClass.getBoldTypeface());
        tvOrderNumber.setTypeface(myApplicationClass.getBoldTypeface());
        tvOrderAmount.setTypeface(myApplicationClass.getRegularTypeface());
        tvOrderDate.setTypeface(myApplicationClass.getRegularTypeface());
        tvOrderTime.setTypeface(myApplicationClass.getRegularTypeface());
        tvOrderStatus.setTypeface(myApplicationClass.getBoldTypeface());
        tvOrderNumberOfItems.setTypeface(myApplicationClass.getRegularTypeface());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");

        orderLineItems = new ArrayList<>();

        i = getIntent();

        order = (Order) i.getParcelableExtra("order");

        bigDecimalClass = new BigDecimalClass(OrderDetailsActivity.this);
        mCC = new ConversionClass(OrderDetailsActivity.this);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecylerViewAdapterOrderSummaryLineItems(this, order.getLineItems());
        recyclerView.setAdapter(adapter);

        if (order.getStatus().equalsIgnoreCase("cancelled")) {
            tvOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        if (order.getStatus().equalsIgnoreCase("processing")) {
            tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }


        if (order.getStatus().equalsIgnoreCase("on-hold")) {
            tvOrderStatus.setTextColor(getResources().getColor(R.color.black));
        }

        tvOrderNumber.setText("Order #" + order.getOrderId());
        tvOrderDate.setText(mCC.formatDateForDisplayInOrders(order.getDateCreated()));
        tvOrderTime.setText("@ " + mCC.formatTimeForDisplayInOrders(order.getDateCreated()));
        tvOrderNumberOfItems.setText(order.getNumberOfItems() + " items");
        tvOrderStatus.setText(order.getStatus());
        tvOrderAmount.setText(bigDecimalClass.convertStringToDisplayCurrencyString(order.getTotalAmount()));
        tvDeliveryFee.setText(bigDecimalClass.convertStringToDisplayCurrencyString(order.getDeliveryCharge()) + " (Delivery Fee)");
        tvAddress1.setText(order.getAddressLine1());
        tvAddress2.setText(order.getAddressLine2() + " " + order.getCity());

    }


    public List<OrderLineItem> getOrderLineItems() {


        return orderLineItems;
    }

    public Order getOrder() {
        return order;
    }
}
