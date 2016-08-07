package com.example.katatoshi.bindingrecyclerview.common;

import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class ItemGroupBinder<T, U> {
    private ItemGroup<T, U> itemGroup;

    public ItemGroup<T, U> getItemGroup() {
        return itemGroup;
    }

    private ItemBinder headerBinder;

    public ItemBinder getHeaderBinder() {
        return headerBinder;
    }

    private ItemBinder itemBinder;

    public ItemBinder getItemBinder() {
        return itemBinder;
    }

    private WeakReference<RecyclerView.Adapter> adapterWeakReference;

    private OnListChangedCallback onListChangedCallback;

    public ItemGroupBinder(ItemGroup<T, U> itemGroup, ItemBinder headerBinder, ItemBinder itemBinder) {
        this.itemGroup = itemGroup;
        this.headerBinder = headerBinder;
        this.itemBinder = itemBinder;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        adapterWeakReference = new WeakReference<RecyclerView.Adapter>(adapter);
    }

    public void addOnListChangedCallback() {
        if (onListChangedCallback != null) {
            return;
        }

        onListChangedCallback = new OnListChangedCallback(adapterWeakReference.get());
        itemGroup.getItems().addOnListChangedCallback(onListChangedCallback);
    }

    public void removeOnListChangedCallback() {
        if (onListChangedCallback == null) {
            return;
        }

        itemGroup.getItems().removeOnListChangedCallback(onListChangedCallback);
        onListChangedCallback = null;
    }

    private static class OnListChangedCallback extends ObservableList.OnListChangedCallback {
        private WeakReference<RecyclerView.Adapter> adapterWeakReference;

        public OnListChangedCallback(RecyclerView.Adapter adapter) {
            adapterWeakReference = new WeakReference<RecyclerView.Adapter>(adapter);
        }

        @Override
        public void onChanged(ObservableList sender) {
            RecyclerView.Adapter adapter = adapterWeakReference.get();
            if (adapter == null) {
                return;
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterWeakReference.get();
            if (adapter == null) {
                return;
            }

            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterWeakReference.get();
            if (adapter == null) {
                return;
            }

            adapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            RecyclerView.Adapter adapter = adapterWeakReference.get();
            if (adapter == null) {
                return;
            }

            // TODO itemCount != 1 のときもこれでいいのか？
            adapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            RecyclerView.Adapter adapter = adapterWeakReference.get();
            if (adapter == null) {
                return;
            }

            adapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    }
}
