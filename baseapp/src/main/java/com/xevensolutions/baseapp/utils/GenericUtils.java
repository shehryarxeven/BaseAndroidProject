package com.xevensolutions.baseapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.util.List;

public class GenericUtils {

    public static int getAttributedColor(Context context, int color) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }

    public static boolean isArrayEmpty(List list) {
        return list == null || list.isEmpty();
    }
}
