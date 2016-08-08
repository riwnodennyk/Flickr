package com.uber.challenge.flickr.photos;

import java.util.Set;

interface FlickrListContract {

    interface View<Data, ActionListener> extends com.uber.challenge.flickr.architecture.View<ActionListener> {
        void add(Data data);

        void error(Throwable throwable);

        void clear();

        void setSuggestions(Set<String> strings);
    }

    interface ActionListener {
        void onSearchTextChanged(String text);

        void onUserScrolledTillBottom();
    }
}
