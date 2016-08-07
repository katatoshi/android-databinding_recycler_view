package com.example.katatoshi.bindingrecyclerview.common;

import android.databinding.ObservableList;

public class ItemGroup<T, U> {
    private T header;

    public T getHeader() {
        return header;
    }

    private ObservableList<U> items;

    public ObservableList<U> getItems() {
        return items;
    }

    public ItemGroup(T header, ObservableList<U> items) {
        this.header = header;
        this.items = items;
    }
}
