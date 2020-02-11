package com.adcollider.sdk.network;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.model.Ad;
import com.adcollider.sdk.util.Logger;
import com.adcollider.sdk.util.notifications.NotificationHelper;
import com.adcollider.sdk.volley.DefaultRetryPolicy;
import com.adcollider.sdk.volley.NetworkResponse;
import com.adcollider.sdk.volley.Response;
import com.adcollider.sdk.volley.VolleyError;
import com.adcollider.sdk.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class ClickRequest extends AbstractRequest<Void> {

    static String constructUrl(){
        StringBuilder builder = new StringBuilder();
        builder.append(getBaseUrl());
        builder.append("/click");
        return builder.toString();
    }

    private Ad mAd;

    public ClickRequest(Ad ad) {
        super(Method.GET, constructUrl(), null);
        this.mAd = ad;

        // Disable caching and retrying
        setShouldCache(false);
        setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = super.getHeaders();
        headers.put("package-clicked", mAd._package);
        return headers;
    }

    @Override
    protected Response<Void> parseNetworkResponse(NetworkResponse response) {
        Logger.log("Success");
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError!=null && volleyError.networkResponse!=null && volleyError.networkResponse.statusCode==503) {
            NotificationHelper.showNotification("Error: ClickRequest", "Rate limiting detected");
        }

        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(Void response) {
        // Do nothing
    }

}
