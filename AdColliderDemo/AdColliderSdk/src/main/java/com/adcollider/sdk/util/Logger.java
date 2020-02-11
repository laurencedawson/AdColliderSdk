package com.adcollider.sdk.util;

import android.text.TextUtils;
import android.util.Log;

import com.adcollider.sdk.BuildConfig;

public final class Logger {

  public static final String TAG = "AdCollider";

  private Logger(){
    // N/A
  }

  public static void log(String text){
    log(TAG, text);
  }

  public static void log(String tag, String text){
    if(!BuildConfig.DEBUG){
      return;
    }

    if(TextUtils.isEmpty(text)){
      return;
    }

    Log.d(tag, text);
  }

  public static void logw(String text){
    logw(TAG, text);
  }

  public static void logw(String tag, String text){
    if(!BuildConfig.DEBUG){
      return;
    }

    if(TextUtils.isEmpty(text)){
      return;
    }

    Log.w(tag, text);
  }

  public static void error(String text){
    error(TAG, text);
  }
  public static void error(String tag, String text){
    if(!BuildConfig.DEBUG){
      return;
    }

    if(TextUtils.isEmpty(text)){
      return;
    }

    Log.e(tag, text);
  }

  public static void exception(Exception e){
    if(!BuildConfig.DEBUG){
      return;
    }

    if(e==null){
      return;
    }

    e.printStackTrace();
  }

}