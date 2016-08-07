package com.uber.challenge.flickr;

import com.f2prateek.rx.preferences.Preference;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;

class MyListUseCase implements ListUseCase {
    private final FlickrApi flickrApi;
    private final Preference<Set<String>> searchHistory;

    MyListUseCase(FlickrApi flickrApi, Preference<Set<String>> searchHistory) {
        this.flickrApi = flickrApi;
        this.searchHistory = searchHistory;
    }

    public Observable<Response> photos(String searchText, int page) {
        return flickrApi.list(searchText, page);
    }

    @Override
    public void saveToHistory(String searchText) {
        Set<String> strings = searchHistory.get();
        if (strings == null) {
            strings = new HashSet<>();
        } else {
            strings = new HashSet<>(strings);
        }
        strings.add(searchText);
        searchHistory.set(strings);
    }

    @Override
    public Observable<Set<String>> suggestions() {
        return searchHistory.asObservable();
    }
}
