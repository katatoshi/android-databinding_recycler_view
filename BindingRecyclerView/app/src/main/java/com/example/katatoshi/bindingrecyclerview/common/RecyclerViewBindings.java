package com.example.katatoshi.bindingrecyclerview.common;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class RecyclerViewBindings {
    @BindingAdapter("itemGroupBinders")
    public static <T> void setItemGroupBinders(RecyclerView recyclerView, List<ItemGroupBinder<T, ?>> itemGroupBinders) {
        GroupedRecyclerViewAdapter<T> adapter = new GroupedRecyclerViewAdapter<>(itemGroupBinders);
        recyclerView.setAdapter(adapter);
    }
}
