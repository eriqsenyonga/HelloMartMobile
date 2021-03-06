package com.plexosysconsult.hellomartmobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FragmentManager fm;
    DrawerLayout drawer;
    NavigationView navigationView;
    SharedPreferences mPositionSavedPrefs;
    SharedPreferences.Editor posSavedEditor;
    View navigationHeader;
    TextView tvClientName;
    TextView tvClientEmail;
    ImageView ivClientProfilePic;
    ImageView redDot;
    MyApplicationClass myApplicationClass = MyApplicationClass.getInstance();
    SharedPreferences userSharedPrefs;
    TextView tvCartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationHeader = navigationView.getHeaderView(0);
        ivClientProfilePic = (ImageView) navigationHeader.findViewById(R.id.iv_client_profile_pic);
        tvClientName = (TextView) navigationHeader.findViewById(R.id.tv_display_name);
        tvClientEmail = (TextView) navigationHeader.findViewById(R.id.tv_email);
        tvCartCount = (TextView) navigationView.getMenu().findItem(R.id.nav_cart).getActionView();


        tvCartCount.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        tvCartCount.setGravity(Gravity.CENTER_VERTICAL);
        tvCartCount.setTypeface(myApplicationClass.getBoldTypeface());

        tvClientName.setTypeface(myApplicationClass.getBoldTypeface());
        tvClientEmail.setTypeface(myApplicationClass.getRegularTypeface());

        updateCartCount();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mPositionSavedPrefs = getSharedPreferences("mPositionSaved",
                Context.MODE_PRIVATE);
        posSavedEditor = mPositionSavedPrefs.edit();

        userSharedPrefs = getSharedPreferences("USER_DETAILS",
                Context.MODE_PRIVATE);
        // editor = userSharedPrefs.edit();

        if (userSharedPrefs.getBoolean("available", false)) {

            String fname = userSharedPrefs.getString("fname", "");
            String lname = userSharedPrefs.getString("lname", "");
            String email = userSharedPrefs.getString("email", "");


            tvClientName.setText("Welcome " + fname);
            tvClientEmail.setText(email);
        } else {

            tvClientName.setText("Welcome Guest");
            tvClientEmail.setText("groceries@hellomart.ug");

        }

        fm = getSupportFragmentManager();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (getIntent().hasExtra("beginning")) {

            fm.beginTransaction().replace(R.id.contentMain, new HomeFragment()).commit();
            drawer.openDrawer(GravityCompat.START);
            posSavedEditor.putInt("last_main_position", R.id.nav_shop).apply();
            getSupportActionBar().setTitle("Shop");
            setNavigationViewCheckedItem(R.id.nav_shop);

        } else {

            Fragment fragment = null;
            CharSequence title = null;

            int id = mPositionSavedPrefs.getInt(
                    "last_main_position", 1);


            if (id == R.id.nav_shop) {
                fragment = new HomeFragment();
                title = "Shop";
            }


            if (id == R.id.nav_cart) {
                fragment = new CartFragment();
                title = "Cart";
            }

            if (id == R.id.nav_categories) {


                fragment = new CategoriesFragment();
                title = "Categories";
            }


            if (id == R.id.nav_account) {
                fragment = new MyAccountFragment();
                title = "My Account";

            }

            if (id == R.id.nav_orders) {
                fragment = new OrdersFragment();
                title = "Orders";
            }


            if (fragment != null) {

                fm.beginTransaction().replace(R.id.contentMain, fragment).commit();
                getSupportActionBar().setTitle(title);
                setNavigationViewCheckedItem(id);
                drawer.closeDrawer(GravityCompat.START);

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        final View notifications = menu.findItem(R.id.action_cart).getActionView();

        redDot = (ImageView) notifications.findViewById(R.id.iv_red_notification);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {

            openCart();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateCartCount();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_help) {


        }

        if (id == R.id.nav_shop) {

            fragment = new HomeFragment();

        } else if (id == R.id.nav_categories) {

            fragment = new CategoriesFragment();

        } else if (id == R.id.nav_cart) {

            fragment = new CartFragment();

        } else if (id == R.id.nav_account) {

            fragment = new MyAccountFragment();

        } else if (id == R.id.nav_orders) {

            fragment = new OrdersFragment();

        }
        /*else if (id == R.id.nav_settings) {

        }*/


        else if (id == R.id.nav_about) {

            fragment = new AboutUsFragment();

        }

        if (fragment != null) {

            fm.beginTransaction().replace(R.id.contentMain, fragment).commit();

            removeSubtitle(item.getTitle().toString());
            posSavedEditor.putInt("last_main_position", id).apply();
            drawer.closeDrawer(GravityCompat.START);

        }

        return true;
    }


    public void showSubcategories(String subTitle) {


        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.contentMain, new SubCategoriesFragment()).addToBackStack(null).commit();
        getSupportActionBar().setSubtitle(subTitle);


    }

    public void openCart() {

        fm.beginTransaction().replace(R.id.contentMain, new CartFragment()).addToBackStack(null).commit();
        setActionBarTitleAndSubtitle("Cart", "");
        setNavigationViewCheckedItem(R.id.nav_cart);


    }


    public void showProductsInCategory(String categorySlug, String subTitle) {

        fm.beginTransaction().replace(R.id.contentMain, ShopFragment.newInstance(categorySlug)).addToBackStack(null).commit();
        getSupportActionBar().setTitle(R.string.shop);
        getSupportActionBar().setSubtitle(subTitle);
        setNavigationViewCheckedItem(R.id.nav_shop);


    }

    public void removeSubtitle(String title) {

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle("");

    }

    public void setActionBarTitleAndSubtitle(String title, String subTitle) {

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subTitle);
    }


    public void setNavigationViewCheckedItem(int itemId) {


        navigationView.setCheckedItem(itemId);
        posSavedEditor.putInt("last_main_position", itemId).apply();

    }

    public void showShopFragment() {

        fm.beginTransaction().replace(R.id.contentMain, new HomeFragment()).commit();
        getSupportActionBar().setTitle(R.string.shop);
        getSupportActionBar().setSubtitle("");
        setNavigationViewCheckedItem(R.id.nav_shop);


    }


    public void checkCartForItems() {

        if (!myApplicationClass.getCart().getCurrentCartItems().isEmpty()) {
            redDot.setVisibility(View.VISIBLE);
        } else {
            redDot.setVisibility(View.GONE);
        }


    }

    public void OpenCart(View v) {

        //  Toast.makeText(this, "Open cart on click", Toast.LENGTH_LONG).show();

        if (myApplicationClass.getCart().getCurrentCartItems().isEmpty()) {

            Toast.makeText(this, "Cart is empty", Toast.LENGTH_LONG).show();


        } else {

            openCart();
        }

    }

    public void updateCartCount() {

        int count = myApplicationClass.getCart().getCurrentCartItems().size();

        tvCartCount.setText("" + count);


    }


}
