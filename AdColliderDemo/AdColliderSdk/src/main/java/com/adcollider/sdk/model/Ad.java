package com.adcollider.sdk.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ad implements Serializable {

    public String _package;
    public String _name;
    public String _category;
    public String _thumbnail;
    public double _rating;

    @NonNull
    @Override
    public String toString() {
        return _name;
    }

}
