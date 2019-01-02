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
    public static int ORDERDETAILSADAPTER = 3;
    int which;
    int tabNumber;

    public PagerAdapterShop(FragmentManager fm, Context context, int whichAdapter) {
        super(fm);

        this.context = context;

        which = whichAdapter;

        if (which == SHOPADAPTER) {
            tabNumber = 4;
        }

        if (which == ABOUTADAPTER) {
            tabNumber = 2;
        }
        if (which == ORDERDETAILSADAPTER) {

            tabNumber = 2;
        }

    }

    @Override
    public Fragment getItem(int position) {

        if (which == ABOUTADAPTER) {


            if (position == 0) {
                return new AboutHelloMartFragment();
            }
            if (position == 1) {
                return new AboutDevFragment();
            }
        }

        if (which == ORDERDETAILSADAPTER) {


            if (position == 0) {
                return new OrderItemsFragment();
            }
            if (position == 1) {
                return new OrderInfoFragment();
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabNumber;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (which == ABOUTADAPTER) {

            if (position == 0) {
                return "HelloMart";
            }
            if (position == 1) {
                return "Developers";
            }
        }

        if (which == ORDERDETAILSADAPTER) {

            if (position == 0) {
                return "ITEMS";
            }
            if (position == 1) {
                return "INFO";
            }
        }


        return super.getPageTitle(position);
    }
}
