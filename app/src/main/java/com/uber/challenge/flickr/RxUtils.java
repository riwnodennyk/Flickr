package com.uber.challenge.flickr;

import android.support.annotation.NonNull;

import rx.Observable;

import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

class RxUtils {

    @NonNull
    static <T> Observable.Transformer<T, T> onBackground() {
        return responseObservable -> responseObservable
                .subscribeOn(io())
                .observeOn(mainThread());
    }
}
