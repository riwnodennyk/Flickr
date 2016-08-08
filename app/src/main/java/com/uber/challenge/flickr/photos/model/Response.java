package com.uber.challenge.flickr.photos.model;

import com.android.annotations.NonNull;
import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class Response {

    public abstract Photos photos();

    abstract String stat();

    @NonNull
    public static JsonAdapter<Response> jsonAdapter(Moshi moshi) {
        return new AutoValue_Response.MoshiJsonAdapter(moshi);
    }

}
