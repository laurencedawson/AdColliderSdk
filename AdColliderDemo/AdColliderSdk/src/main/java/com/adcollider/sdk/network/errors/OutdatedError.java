package com.adcollider.sdk.network.errors;

import com.adcollider.sdk.volley.VolleyError;

public class OutdatedError extends VolleyError {

    public OutdatedError(String exceptionMessage) {
        super(exceptionMessage);
    }

}
