package com.adcollider.sdk;

import android.content.Context;
import android.text.TextUtils;

import com.adcollider.sdk.util.CountryHelper;
import com.adcollider.sdk.util.Logger;

public class AdCollider {

    private static boolean mInit;
    private static Context mApplicationContext;
    private static String mAdvertisingId;
    private static String mApiKey;
    private static String mCountryCode;
    private static String mPackage;

    public static boolean getInit(){
        return mInit;
    }

    public static Context getApplicationContext(){
        return mApplicationContext;
    }

    public static boolean getDebug(){
        return BuildConfig.DEBUG;
    }

    public static String getVersion(){
        return "V2";
    }

    public static String getPackage(){
        return mPackage;
    }

    public static String getApiKey(){
        return mApiKey;
    }

    public static String getCountryCode(){
        return mCountryCode;
    }

    public static void init(Context context, String apiKey){
        if(context==null){
            throw new RuntimeException("No context provided!");
        } else {
            mApplicationContext = context.getApplicationContext();
        }

        mPackage = mApplicationContext.getPackageName();
        mApiKey = apiKey;

        if(TextUtils.isEmpty(apiKey)){
            throw new RuntimeException("No API provided!");
        }

        mCountryCode = CountryHelper.getCountry(context);
        Logger.log("Country: " + mCountryCode);

        mInit = true;
    }

    public static void init(Context context, String packageName, String apiKey){
        if(context==null){
            throw new RuntimeException("No context provided!");
        } else {
            mApplicationContext = context.getApplicationContext();
        }

        mPackage = packageName;
        mApiKey = apiKey;

        if(TextUtils.isEmpty(apiKey)){
            throw new RuntimeException("No API provided!");
        }

        mCountryCode = CountryHelper.getCountry(context);
        Logger.log("Country: " + mCountryCode);

        mInit = true;
    }

    public static boolean isAdvertisingIdSet(){
        return !TextUtils.isEmpty(mAdvertisingId);
    }

    public static void setAdvertisingId(String id){
        mAdvertisingId = id;
    }

    public static String getAdvertisingId(){
        return mAdvertisingId;
    }

}
