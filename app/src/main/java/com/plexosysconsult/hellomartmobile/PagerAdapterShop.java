package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by senyer on 6/12/2016.
 */
public class PagerAdapterShop extends FragmentStatePagerAdapter {

    Context context;
    public static int SHOPADAPTER = 1;
    public static int ABOUTADAPTER = 2;
    int which;
    int tabNumber;

    public PagerAdapterShop(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;

        tabNumber = 2;

    }

    @Override
    public Fragment getItem(int position) {


        if (position == 0) {
            return new AboutHelloMartFragment();
        }
        if (position == 1) {
            return new AboutDevFragment();
        }


        return null;
    }

    @Override
    public int getCount() {
        return tabNumber;
    }

    @Override
    public CharSequence getPageTitle(int position) {


        if (position == 0) {
            return "Hello Mart Ug";
        }
        if (position == 1) {
            return "Developers";
        }


        return super.getPageTitle(position);
    }
}
