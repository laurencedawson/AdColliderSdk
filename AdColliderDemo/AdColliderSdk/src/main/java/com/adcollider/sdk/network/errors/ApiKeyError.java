package com.adcollider.sdk.network.errors;

import com.adcollider.sdk.volley.VolleyError;

public class ApiKeyError extends VolleyError {

    public ApiKeyError(String exceptionMessage) {
        super(exceptionMessage);
    }

}
