package com.plexosysconsult.hellomartmobile;


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
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    View v;
    RecyclerView rvCart;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;
    Button bCheckOut;
    TextView tvGrandTotal;
    String testVCS;
    RecyclerViewAdapterCart adapter;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    Cart cart;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cart, container, false);

        tvGrandTotal = (TextView) v.findViewById(R.id.tv_grand_total);
        bCheckOut = (Button) v.findViewById(R.id.b_checkout);
        rvCart = (RecyclerView) v.findViewById(R.id.recycler_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cart = myApplicationClass.getCart();


        rvCart.hasFixedSize();

        linearLayoutManager = new LinearLayoutManager(getActivity());

        rvCart.setLayoutManager(linearLayoutManager);
        rvCart.addItemDecoration(new ListDividerDecoration(getActivity()));

        adapter = new RecyclerViewAdapterCart(getActivity());

        adapter.setCartDataChangedListener(new CartDataChanged() {
            @Override
            public void onCartDataChanged() {
                tvGrandTotal.setText(cart.getCartGrandTotal());
            }
        });

        rvCart.setAdapter(adapter);

        tvGrandTotal.setText(cart.getCartGrandTotal());

        bCheckOut.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == bCheckOut) {

            //   Toast.makeText(getActivity(),"Go to checkout", Toast.LENGTH_LONG).show();


            Intent i = new Intent(getActivity(), BillingDetails.class);
            startActivity(i);

        }
    }
}
