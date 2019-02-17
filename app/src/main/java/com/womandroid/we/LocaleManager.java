package com.womandroid.we;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {

    private static final String LANGUAGE_ENGLISH = "en";
    private static final String LANGUAGE_KEY = "language_key";

    public static Context

    setLocale(Context c) {
        return wrap(c, getLanguage(c));
    }

    public static void setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        wrap(c, language);
    }

    private static String getLanguage(Context c) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(Context c, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    public static Context wrap(Context context, String language) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        Locale newLocale = new Locale(language);
        Locale.setDefault(newLocale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            configuration.setLayoutDirection(newLocale);
            context = context.createConfigurationContext(configuration);
        }
        else /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)*/ {
            configuration.locale = newLocale;
            configuration.setLocale(newLocale);
            configuration.setLayoutDirection(newLocale);
            context = context.createConfigurationContext(configuration);
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return context;
    }
}
