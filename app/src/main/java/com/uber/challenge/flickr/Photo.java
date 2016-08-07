package com.uber.challenge.flickr;

import com.android.annotations.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Photo {

    @NonNull
    public static JsonAdapter<Photo> jsonAdapter(Moshi moshi) {
        return new AutoValue_Photo.MoshiJsonAdapter(moshi);
    }

    public abstract String id();

    public abstract String title();

    public abstract String server();

    public abstract String secret();

    public abstract String farm();
}
