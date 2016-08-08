package com.uber.challenge.flickr.photos.model;

import com.android.annotations.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

@AutoValue
abstract public class Photos {
    abstract public int page();

    abstract public int pages();

    abstract public int perpage();

    abstract public String total();

    public abstract List<Photo> photo();

    @NonNull
    public static JsonAdapter<Photos> jsonAdapter(Moshi moshi) {
        return new AutoValue_Photos.MoshiJsonAdapter(moshi);
    }
}
