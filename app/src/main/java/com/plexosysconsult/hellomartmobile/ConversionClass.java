package com.plexosysconsult.hellomartmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class ConversionClass {

    SharedPreferences mSharedPrefs, customCurrencyPrefs;

    Locale mLocale;

    String mLocaleCountryString;

    Currency mCurrency;

    String mCurrencyString;

    Context context;

    SimpleDateFormat sdf4Db = new SimpleDateFormat("yyyy-MM-dd",
            Locale.getDefault());

    SimpleDateFormat sdf4Display = new SimpleDateFormat("dd - MMM - yyyy",
            Locale.getDefault());

    SimpleDateFormat sdf4DisplayNew = new SimpleDateFormat("dd MMM yyyy",
            Locale.getDefault());


    SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    SimpleDateFormat outputFormatDate = new SimpleDateFormat("EEE dd MMM yyyy");
    SimpleDateFormat outputFormatTime = new SimpleDateFormat("h:mm a");

    public ConversionClass(Context c) {

        context = c;

    }

    public String formatDateForDisplayInOrders(String serverDate) {

        String returnDateString = "";

        //first get the server date and replace some things
        serverDate = serverDate.replace("T", " ");
        serverDate = serverDate.replace("Z", "");

        Date unformattedNew;
        try {
            unformattedNew = serverDateFormat.parse(serverDate);



            returnDateString = outputFormatDate.format(unformattedNew);

            return returnDateString;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.d("datePicker", e.toString());
        }


        return returnDateString;
    }

    public String formatTimeForDisplayInOrders(String serverDate) {

        String returnDateString = "";

        //first get the server date and replace some things
        serverDate = serverDate.replace("T", " ");
        serverDate = serverDate.replace("Z", "");

        Date unformattedNew;
        try {
            unformattedNew = serverDateFormat.parse(serverDate);

            Calendar c = Calendar.getInstance();
            c.setTime(unformattedNew);
            c.add(Calendar.HOUR, 3); //adding 3 hours to the date since it comes 3hours early
            unformattedNew = c.getTime();

            returnDateString = outputFormatTime.format(unformattedNew);

            return returnDateString;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.d("datePicker", e.toString());
        }


        return returnDateString;
    }


    public Long valueConverter(String inString) {

        BigDecimal bd = new BigDecimal(inString);

        BigDecimal multiplicand = new BigDecimal(100);

        bd = bd.multiply(multiplicand);

        Long lValue = bd.longValue();

        return lValue;

    }

    public String valueConverter(Long dbValue) {

        BigDecimal bd = new BigDecimal(dbValue);

        //	Log.d("bdLong", "" + bd);
        BigDecimal divisor = new BigDecimal(100);

        bd = bd.divide(divisor, 2, RoundingMode.HALF_EVEN);

        //	Log.d("bdAfterDivide", "" + bd);
        String r = bd.toString();
        //	Log.d("r", "" + r);

        final Double d = bd.doubleValue();
        //Log.d("d", "" + d);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        customCurrencyPrefs = context.getSharedPreferences("custom_currency_prefs", 0);


        Boolean useCustomCurrency = mSharedPrefs.getBoolean("pref_use_custom_currency", false);


        if (useCustomCurrency == true) {


            NumberFormat df = NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setCurrencySymbol(customCurrencyPrefs.getString("currencyCode",
                    "AED") + " ");

            //	Log.d("groupSeparator", customCurrencyPrefs.getString("groupSeparator", ","));

            if (customCurrencyPrefs.getString("groupSeparator", ",")
                    .equals(",")) {
                dfs.setGroupingSeparator(',');
            } else if (customCurrencyPrefs.getString("groupSeparator", ",")
                    .equals(".")) {
                dfs.setGroupingSeparator('.');
            }

            if (customCurrencyPrefs.getString("decimalSeparator", ".").equals(
                    ".")) {
                dfs.setMonetaryDecimalSeparator('.');
            } else if (customCurrencyPrefs.getString("decimalSeparator", ".")
                    .equals(",")) {
                dfs.setMonetaryDecimalSeparator(',');
            }

            df.setMaximumFractionDigits(customCurrencyPrefs.getInt(
                    "numberOfDecimals", 2));
            df.setRoundingMode(RoundingMode.HALF_EVEN);
            ((DecimalFormat) df).setDecimalFormatSymbols(dfs);

            String displayString = df.format(d);

            return displayString;
        } else {

            mLocaleCountryString = mSharedPrefs.getString("pref_default_country",
                    "US");

            mCurrencyString = mSharedPrefs
                    .getString("pref_default_currency", "USD");

            mLocale = new Locale("", mLocaleCountryString);

            mCurrency = Currency.getInstance(mCurrencyString);

            NumberFormat nf = NumberFormat.getCurrencyInstance(mLocale);

            nf.setCurrency(mCurrency);
            nf.setMaximumFractionDigits(mCurrency.getDefaultFractionDigits());

            String display9 = nf.format(d);

            //	Log.d(mLocaleCountryString, mLocaleCountryString + "= " + display9);

            return display9;

        }
    }

    public String dateForDb(String unformatted) {

        String formatted = null;

        Date unformattedNew;

        try {
            unformattedNew = sdf4Display.parse(unformatted);

            formatted = sdf4Db.format(unformattedNew);

            return formatted;

        } catch (Exception e) {

            e.printStackTrace();
            //	Log.d("datePicker", e.toString());
        }

        return formatted;

    }

    public String dateForDisplay(String unformatted) {

        String formatted = null;

        Date unformattedNew;
        try {
            unformattedNew = sdf4Db.parse(unformatted);

            formatted = sdf4Display.format(unformattedNew);

            return formatted;

        } catch (Exception e) {

            e.printStackTrace();
            //Log.d("datePicker", e.toString());
        }

        return formatted;
    }

    public String dateForDisplayNew(String unformatted) {

        String formatted = null;

        Date unformattedNew;
        try {
            unformattedNew = sdf4Db.parse(unformatted);

            formatted = sdf4DisplayNew.format(unformattedNew);

            return formatted;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Log.d("datePicker", e.toString());
        }

        return formatted;
    }

    public String dateForDisplayFromCalendarInstance(Date unformatted) {

        String formatted = null;

        try {

            formatted = sdf4Display.format(unformatted);

            return formatted;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //	Log.d("datePicker", e.toString());
        }

        return formatted;
    }

    public Long dateForAlarmManager(String date) {

        Long dateInMillis = null;
        try {
            dateInMillis = sdf4Db.parse(date).getTime();
            return dateInMillis;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dateInMillis;

    }

    public Date returnDateObjectFromDbDateString(String endDate) {
        // TODO Auto-generated method stub

        Date neededDate;
        try {
            neededDate = sdf4Db.parse(endDate);
            return neededDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Date returnDateObjectFromDisplayDateString(String displayDate) {
        // TODO Auto-generated method stub
        Date neededDate;
        try {
            neededDate = sdf4Display.parse(displayDate);
            return neededDate;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public String addTheseDaysToDateAndReturnDbDate(int numberOfDays,
                                                    String displayDate) {
        // TODO Auto-generated method stub
        // display date is current date

        Calendar c = Calendar.getInstance();

        try {
            //Log.d("displayDate", displayDate);
            Date dt = sdf4Display.parse(displayDate);
            //Log.d("dt", "" + dt);

            c.setTime(dt);
            c.add(Calendar.DAY_OF_MONTH, numberOfDays);

            String dateNew = dateForDisplayFromCalendarInstance(c.getTime());
            // Log.d("dateNew", dateNew);
            String nextDate = dateForDb(dateNew);
            //Log.d("nextDate", nextDate);

            return nextDate;
        } catch (ParseException e) {

            e.printStackTrace();
        }

        return null;
    }

    public String returnAmountEditTextString(Long dbAmount) {

        if (dbAmount < 0) {

            dbAmount = dbAmount * (-1);
        }

        BigDecimal bd = new BigDecimal(dbAmount);

        //Log.d("bdLong", "" + bd);
        BigDecimal divisor = new BigDecimal(100);

        bd = bd.divide(divisor, 2, RoundingMode.HALF_EVEN);

        //Log.d("bdAfterDivide", "" + bd);
        String r = bd.toString();

        return r;

    }

    public Double valueConverterReturnDouble(Long dbValue) {

        BigDecimal bd = new BigDecimal(dbValue);

        //Log.d("bdLong", "" + bd);
        BigDecimal divisor = new BigDecimal(100);

        bd = bd.divide(divisor, 2, RoundingMode.HALF_EVEN);

        //Log.d("bdAfterDivide", "" + bd);
        String r = bd.toString();
        //Log.d("r", "" + r);

        final Double d = bd.doubleValue();
        //Log.d("d", "" + d);

        return d;


    }

    public NumberFormat getNumberFormat() {


        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        mLocaleCountryString = mSharedPrefs.getString("pref_default_country",
                "US");

        mCurrencyString = mSharedPrefs
                .getString("pref_default_currency", "USD");

        mLocale = new Locale("", mLocaleCountryString);

        mCurrency = Currency.getInstance(mCurrencyString);

        NumberFormat nf = NumberFormat.getCurrencyInstance(mLocale);

        nf.setCurrency(mCurrency);
        nf.setMaximumFractionDigits(mCurrency.getDefaultFractionDigits());

        return nf;

    }

    public String returnAmountForCSVString(Long dbAmount) {


        BigDecimal bd = new BigDecimal(dbAmount);

        //Log.d("bdLong", "" + bd);
        BigDecimal divisor = new BigDecimal(100);

        bd = bd.divide(divisor, 2, RoundingMode.HALF_EVEN);

        //Log.d("bdAfterDivide", "" + bd);
        String r = bd.toString();

        return r;

    }

}
