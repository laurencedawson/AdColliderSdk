package com.adcollider.sdk.network;

import androidx.annotation.Nullable;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.volley.AuthFailureError;
import com.adcollider.sdk.volley.Request;
import com.adcollider.sdk.volley.Response;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest<T> extends Request<T> {

    protected static String getBaseUrl(){
        return "https://ads.adcollider.com/";
    }

    public AbstractRequest(int method, String url, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);
    }

    @Override
    public Map<String, String> getHeaders() {
        // Return the identification header
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "AdCollider " + AdCollider.getVersion());
        headers.put("package-requesting", AdCollider.getPackage());
        headers.put("api-key", AdCollider.getApiKey());
        headers.put("advertising-id",AdCollider.getAdvertisingId() );
        headers.put("country-code", AdCollider.getCountryCode());
        return headers;
    }

}