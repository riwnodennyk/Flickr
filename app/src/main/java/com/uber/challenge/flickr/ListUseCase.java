package com.uber.challenge.flickr;

import java.util.Set;

import rx.Observable;

public interface ListUseCase {
    Observable<Response> photos(String searchText, int page);

    Observable<Set<String>> suggestions();

    void saveToHistory(String searchText);
}
