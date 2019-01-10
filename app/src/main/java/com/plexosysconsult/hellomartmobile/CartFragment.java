package com.plexosysconsult.hellomartmobile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsConstants;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    View v;
    RecyclerView rvCart;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    Button bCheckOut, bShopNow;
    TextView tvGrandTotal, tvLabelGrandTotal, tvLabelDelivery, tvDeliveryAmount;
    TextView tvEmptyMsg;
    String testVCS;
    RecyclerViewAdapterCart adapter;
    LinearLayout emptyLayout;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;
    MainActivity mainActivity;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart, container, false);
        tvLabelDelivery = (TextView) v.findViewById(R.id.tv_label_delivery);
        tvDeliveryAmount = (TextView) v.findViewById(R.id.tv_delivery_amount);
        tvGrandTotal = (TextView) v.findViewById(R.id.tv_grand_total);
        tvLabelGrandTotal = (TextView) v.findViewById(R.id.tv_label_grand_total);
        tvGrandTotal = (TextView) v.findViewById(R.id.tv_grand_total);
        bCheckOut = (Button) v.findViewById(R.id.b_checkout);
        rvCart = (RecyclerView) v.findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) v.findViewById(R.id.empty_layout);
        tvEmptyMsg = (TextView) v.findViewById(R.id.tv_empty_message);
        bShopNow = (Button) v.findViewById(R.id.b_shop_now);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bCheckOut.setTypeface(myApplicationClass.getBoldTypeface());
        tvGrandTotal.setTypeface(myApplicationClass.getBoldTypeface());
        tvLabelDelivery.setTypeface(myApplicationClass.getRegularTypeface());
        tvLabelGrandTotal.setTypeface(myApplicationClass.getRegularTypeface());
        tvDeliveryAmount.setTypeface(myApplicationClass.getBoldTypeface());


        cart = myApplicationClass.getCart();
        mainActivity = (MainActivity) getActivity();


        rvCart.hasFixedSize();

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rvCart.setLayoutManager(linearLayoutManager);
//        rvCart.addItemDecoration(new ListDividerDecoration(getActivity()));

        adapter = new RecyclerViewAdapterCart(getActivity());

        adapter.setCartDataChangedListener(new CartDataChanged() {
            @Override
            public void onCartDataChanged() {
                tvGrandTotal.setText(cart.getCartGrandTotal());
            }
        });

        rvCart.setAdapter(adapter);

        if (adapter.getItemCount() > 1) {
            rvCart.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            bCheckOut.setEnabled(true);
        } else {
            rvCart.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            tvEmptyMsg.setText("Your cart is empty");
            bShopNow.setVisibility(View.VISIBLE);
            bCheckOut.setEnabled(false);
        }

        tvGrandTotal.setText(cart.getCartGrandTotal());

        bCheckOut.setOnClickListener(this);
        bShopNow.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == bCheckOut) {

            //   Toast.makeText(getActivity(),"Go to checkout", Toast.LENGTH_LONG).show();

            if (cart.getCartGrandTotalLong() < 35000) {
                //if the cart total is less than 35000 riyaleh, then chill

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Our minimum order value is UGX 35,000");
                builder.setNeutralButton("Continue Shopping", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mainActivity.showShopFragment();

                    }
                });

                builder.create().show();


            } else {

                logInitiatedCheckoutEvent(cart.getCurrentCartItems().size(), cart.getCartGrandTotalLong());
                Intent i = new Intent(getActivity(), CheckoutActivity.class);
                startActivity(i);
            }

        }

        if (v == bShopNow) {


            mainActivity.showShopFragment();


        }
    }

    public void logInitiatedCheckoutEvent(int numItems, long totalPrice) {
        Bundle params = new Bundle();

        params.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, numItems);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "UGX");
        myApplicationClass.getLogger().logEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT, totalPrice, params);
    }

}
