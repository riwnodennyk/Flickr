package com.uber.challenge.flickr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.onCreate();
    }

    @NonNull
    abstract Presenter createPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onShown();
    }

    @Override
    protected void onPause() {
        presenter.onHidden();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
