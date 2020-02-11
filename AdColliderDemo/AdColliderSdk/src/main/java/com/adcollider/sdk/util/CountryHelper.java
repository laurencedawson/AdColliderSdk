package com.adcollider.sdk.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

public class CountryHelper {

    public static String getCountry(Context context) {
        String country = getCountryBasedOnSimCardOrNetwork(context);
        if(TextUtils.isEmpty(country)){
            country = "US";
        }

        return country.toUpperCase(Locale.US);
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     *
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     *
     * https://stackoverflow.com/questions/3659809/where-am-i-get-country/37373737#37373737
     */
    private static String getCountryBasedOnSimCardOrNetwork(Context context) {
        if(context==null){
            return null;
        }

        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
