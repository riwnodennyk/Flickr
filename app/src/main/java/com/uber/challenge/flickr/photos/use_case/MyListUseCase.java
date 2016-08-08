package com.uber.challenge.flickr.photos.use_case;

import com.f2prateek.rx.preferences.Preference;
import com.uber.challenge.flickr.domain.FlickrApi;
import com.uber.challenge.flickr.photos.model.Response;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;

public class MyListUseCase implements ListUseCase {
    private final FlickrApi flickrApi;
    private final Preference<Set<String>> searchHistory;

    public MyListUseCase(FlickrApi flickrApi, Preference<Set<String>> searchHistory) {
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
