package se.vhaga.catsfiltering.utility;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vhaga on 2017-09-14.
 */

public class PreferencesHelper {

    private static SharedPreferences getPreferencesInstance(Context context) {
        return context.getSharedPreferences(context.getApplicationContext().getPackageName(), MODE_PRIVATE);
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = getPreferencesInstance(context);
        return preferences.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = getPreferencesInstance(context);
        preferences.edit().putBoolean(key, value).apply();
    }
}
