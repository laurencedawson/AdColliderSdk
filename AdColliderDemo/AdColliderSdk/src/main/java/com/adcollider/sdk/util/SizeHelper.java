package com.adcollider.sdk.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public final class SizeHelper {

  private SizeHelper(){
    // N/A
  }

  public static float pixelToDip(Context context, int size){
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size,
        context.getResources().getDisplayMetrics());
  }

  public static float pixelToDip(int size){
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, size,
      Resources.getSystem().getDisplayMetrics()
    );
  }

  public static int pixelToIntDip(int size){
    return (int) TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, size,
      Resources.getSystem().getDisplayMetrics()
    );
  }

}
