package com.adcollider.sdk.util;

import com.adcollider.sdk.volley.Cache;
import com.adcollider.sdk.volley.NetworkResponse;
import com.adcollider.sdk.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Overrride the default volley responses cache config
 * http://stackoverflow.com/questions/16781244/android-volley-jsonobjectrequest-caching/16852314#16852314
 */
public final class CustomHttpHeaderParser {

  private CustomHttpHeaderParser(){
    // N/A
  }

  public static Cache.Entry parseIgnoreCacheHeadersRelativeDuration(NetworkResponse response, long cacheDuration) {
    long now = System.currentTimeMillis();

    Map<String, String> headers = response.headers;
    long serverDate = 0;
    String serverEtag = null;
    String headerValue;

    headerValue = headers.get("Date"); // NON-NLS
    if (headerValue != null) {
      serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
    }

    serverEtag = headers.get("ETag"); // NON-NLS
    final long softExpire = now + cacheDuration;
    final long ttl = now + cacheDuration;

    Cache.Entry entry = new Cache.Entry();
    entry.data = response.data;
    entry.etag = serverEtag;
    entry.softTtl = softExpire;
    entry.ttl = ttl;
    entry.serverDate = serverDate;
    entry.responseHeaders = headers;

    return entry;
  }

}