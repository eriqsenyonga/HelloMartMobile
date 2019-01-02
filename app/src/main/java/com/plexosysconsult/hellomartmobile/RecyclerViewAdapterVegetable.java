package com.plexosysconsult.hellomartmobile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by senyer on 6/12/2016.
 */
public class RecyclerViewAdapterVegetable extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public static int KEY_MAIN_ACTIVITY = 1;
    public static int KEY_SEARCHABLE_ACTIVITY = 2;

    Context context;
    List<Item> items;
    BigDecimalClass bigDecimalClass;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    MainActivity mainActivity;
    SearchableActivity searchableActivity;
    boolean isMainActivityContext = false;
    Cart cart;

    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;


    public RecyclerViewAdapterVegetable(Context context, int whichActivity) {

        this.context = context;

        cart = myApplicationClass.getCart();

        if (whichActivity == KEY_MAIN_ACTIVITY) {

            isMainActivityContext = true;
            mainActivity = (MainActivity) context;


        } else {
            isMainActivityContext = false;
        }

        bigDecimalClass = new BigDecimalClass(context);

        items = new ArrayList();



        // items = itemsToShow;


    }


    public List<Item> getItems() {
        return items;
    }

    public void setMovies(List<Item> itemsToShow) {
        this.items = itemsToShow;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.loading_spinner, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.list_item, parent, false);
        viewHolder = new ItemViewHolder(v1, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Item veggie = (Item) this.items.get(position);


        if (getItemViewType(position) == ITEM) {


            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            itemViewHolder.tvItemName.setText(veggie.getItemName());
            itemViewHolder.tvItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(veggie.getItemPrice()));

            //  final int selectedVariation = 0;

            Glide
                    .with(context)
                    .load(veggie.getImageUrl())
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
                    .into(itemViewHolder.ivItemImage);


            itemViewHolder.setClickListener(new ItemClickListener() {
                                                @Override
                                                public void onClick(View view, final int position, boolean isLongClick) {

                                                    //  Toast.makeText(context, items.get(position).getItemName() + ": " + items.get(position).getItemPrice(), Toast.LENGTH_LONG).show();


                                                  //  LayoutInflater inflater = LayoutInflater.from(context);
                                                    LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                                                //    LayoutInflater inflater = context.getLayoutInflater();

                                                    View addToCartDialog = inflater.inflate(R.layout.dialog_add_item_to_cart_details, null);

                                                    TextView tvLabelAmount =  addToCartDialog.findViewById(R.id.tv_label_amount);
                                                    TextView tvCartItemName =  addToCartDialog.findViewById(R.id.tv_item_name);
                                                    TextView tvLabelOptions = addToCartDialog.findViewById(R.id.tv_label_options);
                                                    final Spinner spnVariations =  addToCartDialog.findViewById(R.id.spn_variations);

                                                    final TextView tvCartItemPrice =  addToCartDialog.findViewById(R.id.tv_item_unit_price);
                                                    final TextView tvAmount =  addToCartDialog.findViewById(R.id.tv_amount);
                                                    final TextInputLayout tilQuantity =  addToCartDialog.findViewById(R.id.til_quantity);
                                                    FloatingActionButton fabAddToCart =  addToCartDialog.findViewById(R.id.fab_add_to_cart);
                                                    ImageView ivItemImage = addToCartDialog.findViewById(R.id.iv_item_image);

                                                    tvLabelAmount.setTypeface(myApplicationClass.getRegularTypeface());
                                                    tvCartItemName.setTypeface(myApplicationClass.getBoldTypeface());
                                                    tvLabelOptions.setTypeface(myApplicationClass.getBoldTypeface());
                                                    tvCartItemPrice.setTypeface(myApplicationClass.getRegularTypeface());
                                                    tvAmount.setTypeface(myApplicationClass.getRegularTypeface());
                                                    tilQuantity.setTypeface(myApplicationClass.getBoldTypeface());
                                                    tilQuantity.getEditText().setTypeface(myApplicationClass.getRegularTypeface());


                                                    tvCartItemName.setText(items.get(position).getItemName());
                                                    tvCartItemPrice.setText(items.get(position).getItemPrice());
                                                    Glide
                                                            .with(context)
                                                            .load(items.get(position).getImageUrl())
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
                                                            .into(ivItemImage);


                                                    if (items.get(position).getItemShortDescription().contains("Kilogram")) {

                                                        tilQuantity.setHint("Weight");

                                                    }


                                                    tilQuantity.getEditText().addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable s) {

                                                            if (tilQuantity.getEditText().getText().toString().isEmpty()) {

                                                                tvAmount.setText("UGX 0");
                                                            } else {

                                                                tvAmount.setText(bigDecimalClass.convertLongToDisplayCurrencyString((bigDecimalClass.multiplyParameters(tvCartItemPrice.getText().toString(), tilQuantity.getEditText().getText().toString()))));
                                                            }


                                                        }


                                                    });


                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setView(addToCartDialog);

                                                    final Dialog d = builder.create();


                                                    if (items.get(position).getHasVariations()) {

                                                        tvLabelOptions.setVisibility(View.VISIBLE);
                                                        spnVariations.setVisibility(View.VISIBLE);

                                                        final List<Item> variationsList = items.get(position).getItemVariations();

                                                        AdapterSpinnerVariations adapter = new AdapterSpinnerVariations(context, android.R.layout.simple_spinner_item, variationsList);

                                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                        spnVariations.setAdapter(adapter);

                                                        spnVariations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                            @Override
                                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                                                                Item item = variationsList.get(i);

                                                                // tvCartItemPrice.setText(bigDecimalClass.convertStringToDisplayCurrencyString(item.getItemPrice()));
                                                                tvCartItemPrice.setText(item.getItemPrice());

                                                                if (tilQuantity.getEditText().getText().toString().isEmpty()) {

                                                                    tvAmount.setText("UGX 0");
                                                                } else {

                                                                    tvAmount.setText(bigDecimalClass.convertLongToDisplayCurrencyString((bigDecimalClass.multiplyParameters(tvCartItemPrice.getText().toString(), tilQuantity.getEditText().getText().toString()))));
                                                                }


                                                            }

                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                                            }
                                                        });


                                                    } else {
                                                        tvLabelOptions.setVisibility(View.GONE);
                                                        spnVariations.setVisibility(View.GONE);
                                                    }


                                                    fabAddToCart.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {


                                                            if (tilQuantity.getEditText().getText().toString().isEmpty()) {
                                                                tilQuantity.getEditText().setError("Enter Quantity");
                                                            } else {
                                                                CartItem cartItem = new CartItem(context);

                                                                cartItem.setItemName(items.get(position).getItemName());
                                                                cartItem.setItemId(items.get(position).getItemId());
                                                                cartItem.setItemImageUrl(items.get(position).getImageUrl());

                                                                cartItem.setQuantity(tilQuantity.getEditText().getText().toString());
                                                                cartItem.setItemUnitPrice(items.get(position).getItemPrice());

                                                                if (items.get(position).getHasVariations()) {

                                                                    cartItem.setIsVariation(true);

                                                                    int selectedVariationPosition = spnVariations.getSelectedItemPosition();


                                                                    //this will be used when creating the order request as variation_id
                                                                    cartItem.setItemVariationId(items.get(position)
                                                                            .getItemVariations()
                                                                            .get(selectedVariationPosition)
                                                                            .getItemId());

                                                                    //we use the unit price for the variation
                                                                    cartItem.setItemUnitPrice(items.get(position)
                                                                            .getItemVariations()
                                                                            .get(selectedVariationPosition)
                                                                            .getItemPrice());

                                                                    //name of the item is the parent + the variation item name
                                                                    cartItem.setItemName(items.get(position).getItemName() + " (" + items.get(position)
                                                                            .getItemVariations()
                                                                            .get(selectedVariationPosition)
                                                                            .getOptionUnit() + ")");


                                                                }

                                                                cart.addItemToCart(cartItem);

                                                                myApplicationClass.updateCart(cart);


                                                                if (isMainActivityContext) {
                                                                    mainActivity.checkCartForItems();
                                                                    mainActivity.updateCartCount();
                                                                }

                                                                d.dismiss();
                                                            }


                                                        }
                                                    });

                                                    d.show();

                                                }
                                            }
            );

        } else if (getItemViewType(position) == LOADING) {

            //Do nothing

        }


    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public void add(Item item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void addAll(List<Item> itemList) {
        for (Item item : itemList) {
            add(item);
        }
    }

    public void remove(Item city) {
        int position = items.indexOf(city);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Item());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = items.size() - 1;
        Item item = getItem(position);

        if (item != null) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == items.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvItemName, tvItemPrice;
        ImageView ivItemImage;
        ItemClickListener itemClickListener;
        ProgressBar loading;
        Typeface productSansBold, productSansRegular;

        public ItemViewHolder(View itemView, Context c) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = (TextView) itemView.findViewById(R.id.tv_item_price);
            ivItemImage = (ImageView) itemView.findViewById(R.id.iv_item_image);
            loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);

            productSansBold = Typeface.createFromAsset(c.getAssets(), "fonts/ProductSansBold.ttf");
            productSansRegular = Typeface.createFromAsset(c.getAssets(), "fonts/ProductSansRegular.ttf");

            tvItemName.setTypeface(productSansBold);
            tvItemPrice.setTypeface(productSansRegular);

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

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}
