package com.uber.challenge.flickr.photos;

import com.uber.challenge.flickr.architecture.Presenter;
import com.uber.challenge.flickr.photos.model.Response;
import com.uber.challenge.flickr.photos.use_case.ListUseCase;

import rx.Subscription;
import rx.functions.Action1;

import static com.uber.challenge.flickr.util.RxUtils.onBackground;


class FlickrListPresenter extends Presenter implements FlickrListContract.ActionListener {

    private final FlickrListContract.View<Response, FlickrListContract.ActionListener> view;
    private final ListUseCase useCase;

    private Subscription listInLoad;
    private Subscription suggestionsInLoad;
    private String text;
    private int currentPage;

    FlickrListPresenter(FlickrListContract.View<Response, FlickrListContract.ActionListener> view, ListUseCase useCase) {
        this.view = view;
        this.useCase = useCase;
        view.setActionListener(this);
    }

    @Override
    protected void onShown() {
        super.onShown();
        suggestionsInLoad = useCase.suggestions()
                .subscribe(view::setSuggestions);
    }

    @Override
    protected void onHidden() {
        if (suggestionsInLoad != null) {
            suggestionsInLoad.unsubscribe();
        }
        super.onHidden();
    }

    private void load(int page, Action1<Response> success) {
        this.listInLoad = useCase.photos(this.text, page)
                .compose(onBackground())
                .subscribe(success, view::error);
    }


    @Override
    protected void onDestroy() {
        if (listInLoad != null) {
            listInLoad.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onSearchTextChanged(String text) {
        this.currentPage = 0;
        this.text = text;
        this.useCase.saveToHistory(text);
        load(0, response -> {
            view.clear();
            view.add(response);
        });
    }

    @Override
    public void onUserScrolledTillBottom() {
        load(++currentPage, view::add);
    }

}
