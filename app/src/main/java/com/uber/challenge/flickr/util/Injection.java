package com.uber.challenge.flickr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.squareup.moshi.Moshi;
import com.uber.challenge.flickr.domain.MyAdapterFactory;
import com.uber.challenge.flickr.domain.FlickrApi;

import java.util.Set;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class Injection {

    @NonNull
    public static FlickrApi getFlickrApi() {

        Moshi moshi = new Moshi.Builder()
                .add(MyAdapterFactory.create())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl("https://api.flickr.com")
                .build();

        return retrofit.create(FlickrApi.class);
    }

    @NonNull
    public static Preference<Set<String>> searchHistory(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        return rxPreferences.getStringSet("searchHistory");
    }
}
