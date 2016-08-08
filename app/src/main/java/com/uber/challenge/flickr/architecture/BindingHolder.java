package com.uber.challenge.flickr.architecture;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final T binding;

    public BindingHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public T getBinding() {
        return binding;
    }
}
