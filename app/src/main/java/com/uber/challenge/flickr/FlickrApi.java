package com.uber.challenge.flickr;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface FlickrApi {

    @GET("https://api.flickr.com/services/rest/" +
            "?method=flickr.photos.search" +
            "&api_key=3e7cc266ae2b0e0d78e279ce8e361736" +
            "&format=json" +
            "&nojsoncallback=1")
    Observable<Response> list(@Query("text") String searchText, @Query("page") int page);
}
