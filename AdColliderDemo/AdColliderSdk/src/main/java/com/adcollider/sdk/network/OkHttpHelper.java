package com.adcollider.sdk.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public final class OkHttpHelper {

  static OkHttpClient sVolleyClient;

  public synchronized static OkHttpClient getVolleyClient(){
    if(sVolleyClient==null){
      OkHttpClient.Builder builder = new OkHttpClient.Builder()
              .cache(null)
              .readTimeout(10, TimeUnit.SECONDS)
              .connectTimeout(10, TimeUnit.SECONDS)
              .writeTimeout(10, TimeUnit.SECONDS)
              .retryOnConnectionFailure(false)
              .followSslRedirects(true)
              .followRedirects(true);

      sVolleyClient = builder.build();
    }

    return sVolleyClient;
  }

}