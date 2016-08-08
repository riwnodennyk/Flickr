package com.uber.challenge.flickr.util;

import android.view.ViewGroup;

public class ViewUtils {
    public static void makeSquare(android.view.View view, int size) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = size;
        layoutParams.width = size;
        view.setLayoutParams(layoutParams);
    }
}
