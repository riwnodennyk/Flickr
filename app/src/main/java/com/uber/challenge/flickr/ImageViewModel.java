package com.uber.challenge.flickr;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ImageViewModel {
    public abstract String text();

    @NonNull
    public static ImageViewModel create(String id) {
        return new AutoValue_ImageViewModel(id);
    }
}
