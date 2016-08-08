package com.uber.challenge.flickr.util;

import android.support.annotation.NonNull;

import rx.Observable;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

public class RxUtils {

    @NonNull
    public static <T> Observable.Transformer<T, T> onBackground() {
        return responseObservable -> responseObservable
                .subscribeOn(io())
                .observeOn(mainThread());
    }
}
