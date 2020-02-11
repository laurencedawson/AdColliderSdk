package com.adcollider.sdk.network;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.util.Logger;
import com.adcollider.sdk.volley.Request;
import com.adcollider.sdk.volley.RequestQueue;
import com.adcollider.sdk.volley.toolbox.Volley;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdRequestQueue {

    static final String TAG = AdRequestQueue.class.getSimpleName();

    private static AdRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private ExecutorService executor;

    private AdRequestQueue(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
        executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized AdRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new AdRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void add(final Request<T> req) {
        if(req instanceof ImpressionRequest){
            // If we can sent the request, make a note of the time
            if(canSend(IMPRESSION)){
                Logger.log("Recording impression last sent");
                setLastSent(IMPRESSION);
            }

            // Otherwise bounce the request
            else {
                if(AdCollider.getDebug()){
                    Toast.makeText(AdCollider.getApplicationContext(), "Bouncing impression request", Toast.LENGTH_SHORT).show();
                }

                Logger.log("Bouncing impression request");
                req.cancel();
                return;
            }
        }

        else if(req instanceof ClickRequest){
            // If we can sent the request, make a note of the time
            if(canSend(CLICK)){
                Logger.log("Recording click last sent");
                setLastSent(CLICK);
            }

            // Otherwise bounce the request
            else {
                if(AdCollider.getDebug()){
                    Toast.makeText(AdCollider.getApplicationContext(), "Bouncing click request", Toast.LENGTH_SHORT).show();
                }

                Logger.log("Bouncing click request");
                req.cancel();
                return;
            }
        }

        if(AdCollider.isAdvertisingIdSet()) {
            Log.i(TAG, "Advertising ID was already set: " + AdCollider.getAdvertisingId());
            getRequestQueue().add(req);
        }

        else {
            Log.i(TAG, "Advertising ID was not set");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if(ctx==null){
                        return;
                    }

                    // Check if we've set our advertising ID by now
                    if(AdCollider.isAdvertisingIdSet()) {
                        Log.i(TAG, "Advertising ID was set while we were waiting: " + AdCollider.getAdvertisingId());
                        getRequestQueue().add(req);
                    }

                    else {
                        Log.i(TAG, "No advertising ID found");

                        try {
                            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(ctx);
                            if (null != info) {
                                AdCollider.setAdvertisingId(info.getId());

                                Log.i(TAG, "Advertising ID set and request added to queue: " + AdCollider.getAdvertisingId());
                                getRequestQueue().add(req);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private static final String IMPRESSION = "impression";
    private static final String CLICK = "click";

    private static void setLastSent(String key){
        PreferenceManager
                .getDefaultSharedPreferences(AdCollider.getApplicationContext())
                .edit()
                .putLong(key, System.currentTimeMillis())
                .apply();
    }

    private static boolean canSend(String key){

        long lastSent = PreferenceManager.getDefaultSharedPreferences(AdCollider.getApplicationContext()).getLong(key, 0);

        Logger.log("Last sent: " + lastSent);
        Logger.log("Current: " + System.currentTimeMillis());
        Logger.log("Valid: " + ((System.currentTimeMillis() - lastSent) > (60 * 1000)));
        return lastSent==0 || (System.currentTimeMillis() - lastSent) > (60 * 1000);
    }

}