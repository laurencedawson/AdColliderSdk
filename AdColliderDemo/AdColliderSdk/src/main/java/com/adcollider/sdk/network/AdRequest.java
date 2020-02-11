package com.adcollider.sdk.network;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.model.Ad;
import com.adcollider.sdk.network.errors.ApiKeyError;
import com.adcollider.sdk.network.errors.ErrorCodes;
import com.adcollider.sdk.network.errors.OutdatedError;
import com.adcollider.sdk.network.errors.PackageError;
import com.adcollider.sdk.util.CustomHttpHeaderParser;
import com.adcollider.sdk.util.Logger;
import com.adcollider.sdk.volley.NetworkResponse;
import com.adcollider.sdk.volley.Response;
import com.adcollider.sdk.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

public class AdRequest extends AbstractRequest<Ad> {

    public static String constructUrl(){
        StringBuilder builder = new StringBuilder();
        builder.append(getBaseUrl());
        builder.append("/request");
        return builder.toString();
    }

    private Response.Listener<Ad> listener;

    public AdRequest(Response.Listener<Ad> listener, Response.ErrorListener errorListener) {
        super(Method.GET, constructUrl(), errorListener);
        this.listener = listener;

        // Make sure we cache this request
        setShouldCache(true);
    }

    @Override
    protected Response<Ad> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data);
            Logger.log("Data: " + data);
            JSONObject object = new JSONObject(data);

            Ad ad = new Ad();
            ad._package= object.optString("id").trim();
            ad._name = object.optString("name").trim();
            ad._category = object.optString("category").trim();
            ad._thumbnail = object.getString("thumbnail").trim();
            ad._rating = object.optDouble("rating");

            return Response.success(ad, CustomHttpHeaderParser.parseIgnoreCacheHeadersRelativeDuration(response, 30 * 1000));
        }

        catch (Exception e){
            e.printStackTrace();
            return Response.error(null);
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        try {
            Logger.log("Error: " + volleyError);
            Logger.log("Status: " + volleyError.networkResponse.statusCode);

            String data = new String(volleyError.networkResponse.data);
            JSONObject object = new JSONObject(data);

            String error = object.optString("error");
            String message = object.optString("message");

            if(ErrorCodes.INVALID_PACKAGE.equals(error)){
                Logger.log("Detected invalid package error");
                return new PackageError(message);
            }

            else if(ErrorCodes.INVALID_API_KEY.equals(error)){
                Logger.log("Detected API error");
                return new ApiKeyError(message);
            }

            else {
                return volleyError;
            }
        }

        catch (Exception e){
            e.printStackTrace();
            return volleyError;
        }
    }

    @Override
    protected void deliverResponse(Ad response) {
        if(listener!=null){
            listener.onResponse(response);
        }
    }

}
