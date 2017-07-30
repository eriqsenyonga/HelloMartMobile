package com.plexosysconsult.hellomartmobile;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FragmentManager fm;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fm = getSupportFragmentManager();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;


        if (id == R.id.nav_shop) {
            fragment = new ShopFragment();
        } else if (id == R.id.nav_categories) {

            fragment = new CategoriesFragment();

        } else if (id == R.id.nav_my_account) {

        }else if (id == R.id.nav_cart) {

            fragment = new CartFragment();

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        if (fragment != null) {

            fm.beginTransaction().replace(R.id.contentMain, fragment).commit();

            removeSubtitle(item.getTitle().toString());

            drawer.closeDrawer(GravityCompat.START);

        }

        return true;
    }


    public void showSubcategories(String subTitle) {


        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.contentMain, new SubCategoriesFragment()).addToBackStack(null).commit();
        getSupportActionBar().setSubtitle(subTitle);


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

    }


}
