package com.adcollider.sdk.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.adcollider.sdk.AdCollider;
import com.adcollider.sdk.R;
import com.adcollider.sdk.model.Ad;
import com.adcollider.sdk.network.AdRequest;
import com.adcollider.sdk.network.AdRequestQueue;
import com.adcollider.sdk.network.ClickRequest;
import com.adcollider.sdk.network.ImpressionRequest;
import com.adcollider.sdk.network.errors.ApiKeyError;
import com.adcollider.sdk.network.errors.OutdatedError;
import com.adcollider.sdk.network.errors.PackageError;
import com.adcollider.sdk.volley.Response;
import com.adcollider.sdk.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class AdView extends FrameLayout {

    private Ad mAd;
    private boolean mNightMode;

    public AdView(Context context) {
        super(context);

        if(getId()==View.NO_ID){
            setId(R.id.adColliderAdView);
        }
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(getId()==View.NO_ID){
            setId(R.id.adColliderAdView);
        }
    }

    public void setAd(Ad ad){
        this.mAd = ad;
        setupAd();
    }

    public Ad getAd(){
        return mAd;
    }

    public boolean hasAd(){
        return mAd != null;
    }

    public String getAdPackage(){
        if(hasAd()){
            return getAd()._package;
        }

        return null;
    }

    private boolean showError(VolleyError error){
        if(error instanceof PackageError){
            return true;
        } else if(error instanceof OutdatedError){
            return true;
        } else if(error instanceof ApiKeyError){
            return true;
        }

        return false;
    }

    public void setupError(VolleyError error){
        // Bail if we have no error
        if(error==null){
            return;
        }

        // Only allow certain error types
        if(!showError(error)){
            return;
        }

        // Bail if context is invalid
        if(!isContextValid()){
            return;
        }

        // Inflate inner
        inflate(getContext(), R.layout.view_errorlayout, this);

        // Style the view
        findViewById(R.id.errorLayout_root).setBackgroundColor(0xffB00020);

        TextView errorMessageView = findViewById(R.id.errorLayout_message);
        errorMessageView.setText(error.getMessage());
    }

    private boolean isContextValid(){
        // No context
        if(getContext() == null){
            return false;
        }

        // Bail if we've been destroyed
        if(getContext() instanceof Activity && ((Activity) getContext()).isDestroyed()){
            return false;
        }

        // Bail if we're finishing
        if(getContext() instanceof Activity && ((Activity) getContext()).isFinishing()){
            return false;
        }

        // We're no longer attached
        if(getParent() == null){
            return false;
        }

        return true;
    }

    private void setupAd(){
        // Only setupAd if we have a valid ad
        if(!hasAd()){
            return;
        }

        // Bail if context is invalid
        if(!isContextValid()) {
            return;
        }

        // Inflate inner
        inflate(getContext(), R.layout.view_adlayout, this);

        // Grab thew root
        final View rootView = findViewById(R.id.adLayout_root);

        // Trigger a layout pass and then send the impression request
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Make sure we're showing the root view
                if(rootView.getHeight() > 0
                        && rootView.getWidth()>0
                        && rootView.isAttachedToWindow()
                        && rootView.isShown()){

                    // Bail if we have no context
                    if(getContext() == null){
                        return;
                    }

                    // Bail if our context is not valid
                    if(!isContextValid()){
                        return;
                    }

                    AdRequestQueue.getInstance(getContext()).add(new ImpressionRequest(getAd()));
                }

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        rootView.requestLayout();

        // Grab all views
        ImageView imageView = findViewById(R.id.adLayout_image);
        TextView title = findViewById(R.id.adLayout_title);
        TextView tagline = findViewById(R.id.adLayout_tagline);

        // Setup the entire button as a clickable object
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AdRequestQueue.getInstance(getContext()).add(new ClickRequest(getAd()));

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + getAdPackage() +"&referrer=AdCollider"));
                getContext().startActivity(intent);
            }
        });

        // Grab the image
        {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));

            Glide
                    .with(getContext())
                    .load(getAd()._thumbnail)
                    .apply(requestOptions)
                    .into(imageView);
        }

        // Set all text elements
        title.setText(getAd()._name);
        tagline.setText(getAd()._category + " • " + getAd()._rating + " ★");

        // Style the view
        styleView();
    }

    public void requestAd(){
        if(!AdCollider.getInit()){
            throw new RuntimeException("AdCollider.init() has not been  called!");
        }

        if(!isContextValid()){
            return;
        }

        AdRequestQueue.getInstance(getContext()).add(new AdRequest(new Response.Listener<Ad>() {
            @Override
            public void onResponse(Ad ad) {
                setAd(ad);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setupError(error);
            }
        }));
    }

    public void setNightMode(){
        mNightMode = true;
        styleView();
    }

    private void styleView(){
        if(findViewById(R.id.adLayout_divider)!=null){
            findViewById(R.id.adLayout_divider).setBackgroundColor(mNightMode ? 0xff1c1e20  : 0xffdfdfdf);
        }

        if(findViewById(R.id.adLayout_root)!=null){
            findViewById(R.id.adLayout_root).setBackgroundColor(mNightMode ? 0xff292929 : 0xffffffff);
        }

        if(findViewById(R.id.adLayout_title)!=null){
            ((TextView) findViewById(R.id.adLayout_title)).setTextColor(mNightMode ? 0xffffffff : 0xff242424);
        }

        if(findViewById(R.id.adLayout_tagline)!=null){
            ((TextView) findViewById(R.id.adLayout_tagline)).setTextColor(mNightMode ? 0xff8d8d8d: 0xffa0a0a0);
        }
    }

}
