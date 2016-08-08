package com.uber.challenge.flickr.photos;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uber.challenge.flickr.R;
import com.uber.challenge.flickr.architecture.BaseActivity;
import com.uber.challenge.flickr.architecture.BindingHolder;
import com.uber.challenge.flickr.architecture.Presenter;
import com.uber.challenge.flickr.databinding.ActivityFlickrBinding;
import com.uber.challenge.flickr.databinding.ImageBinding;
import com.uber.challenge.flickr.photos.model.Photo;
import com.uber.challenge.flickr.photos.model.Response;
import com.uber.challenge.flickr.photos.use_case.MyListUseCase;
import com.uber.challenge.flickr.util.Injection;
import com.uber.challenge.flickr.util.KeyboardUtils;
import com.uber.challenge.flickr.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscription;

import static android.text.TextUtils.isEmpty;
import static android.view.LayoutInflater.from;
import static com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView.scrollEvents;
import static com.jakewharton.rxbinding.widget.RxTextView.textChanges;
import static java.text.MessageFormat.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.android.schedulers.AndroidSchedulers.mainThread;

public class FlickrActivity extends BaseActivity implements FlickrListContract.View<Response, FlickrListContract.ActionListener> {

    private static final String TAG = FlickrActivity.class.getSimpleName();

    private RecyclerView.Adapter<BindingHolder<ImageBinding>> photosAdapter;
    private GridLayoutManager layoutManager;
    private List<Photo> photos = new ArrayList<>();
    private ArrayAdapter<String> suggestionsAdapter;
    private FlickrListContract.ActionListener listener;
    private ActivityFlickrBinding binding;
    private Subscription textChangedSubscription;
    private Subscription showDropdownSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flickr);

        suggestionsAdapter = new ArrayAdapter<>(binding.getRoot().getContext(),
                android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        binding.search.setAdapter(suggestionsAdapter);

        layoutManager = new GridLayoutManager(binding.getRoot().getContext(), 3);
        binding.recycler.setLayoutManager(layoutManager);
        photosAdapter = new RecyclerView.Adapter<BindingHolder<ImageBinding>>() {

            @Override
            public BindingHolder<ImageBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
                ImageBinding binding = DataBindingUtil.inflate(from(parent.getContext()), R.layout.image, parent, false);
                ViewUtils.makeSquare(binding.getRoot(), parent.getMeasuredWidth() / 3);
                return new BindingHolder<>(binding);
            }

            @Override
            public void onBindViewHolder(BindingHolder<ImageBinding> holder, int position) {
                Photo image = photos.get(position);
                String imageUrl = format("http://farm{0}.static.flickr.com/{1}/{2}_{3}.jpg", image.farm(), image.server(), image.id(), image.secret());
                ImageView imageView = holder.getBinding().image;
                Picasso.with(imageView.getContext())
                        .load(imageUrl)
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }

            @Override
            public int getItemCount() {
                return photos.size();
            }
        };
        binding.recycler.setAdapter(photosAdapter);
        scrollEvents(binding.recycler)
                .filter(ev -> ev.dy() > 0) // scrolling down
                .filter(ev -> {
                    int screen = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition();
                    return layoutManager.getItemCount() < (layoutManager.findLastVisibleItemPosition() + 2 * screen);
                }) // less than 2 screens left till the end
                .map(ev -> photos.get(photos.size() - 1)) // check last item
                .distinctUntilChanged() // we want to trigger only once for it
                .subscribe(lastPhoto -> listener.onUserScrolledTillBottom());

        Observable<CharSequence> searchTextChanges = textChanges(binding.search);

        textChangedSubscription = searchTextChanges
                .debounce(1, SECONDS)
                .filter(e -> !isEmpty(e))
                .map(CharSequence::toString)
                .distinctUntilChanged()
                .subscribe(e -> listener.onSearchTextChanged(e));
        showDropdownSubscription = searchTextChanges
                .debounce(100, MILLISECONDS)
                .filter(TextUtils::isEmpty)
                .observeOn(mainThread())
                .subscribe(textViewAfterTextChangeEvent -> {
                    binding.search.showDropDown();
                });
    }

    @NonNull
    @Override
    protected Presenter createPresenter() {
        return new FlickrListPresenter(this, new MyListUseCase(Injection.getFlickrApi(), Injection.searchHistory(this)));
    }

    @Override
    protected void onDestroy() {
        textChangedSubscription.unsubscribe();
        showDropdownSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void add(final Response data) {
        List<Photo> newPhotos = data.photos().photo();
        photos.addAll(newPhotos);
        photosAdapter.notifyItemRangeInserted(photos.size() - newPhotos.size(), newPhotos.size());
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(binding.getRoot(), throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
        Log.e(TAG, "Rx exception", throwable);
    }

    @Override
    public void clear() {
        int wasSize = photos.size();
        photos.clear();
        photosAdapter.notifyItemRangeRemoved(0, wasSize);
        KeyboardUtils.hideSoft(binding.getRoot().findFocus());
    }

    @Override
    public void setSuggestions(Set<String> strings) {
        suggestionsAdapter.clear();
        suggestionsAdapter.addAll(strings);
        suggestionsAdapter.notifyDataSetChanged();
    }

    public void setActionListener(FlickrListContract.ActionListener actionListener) {
        listener = actionListener;
    }

}
