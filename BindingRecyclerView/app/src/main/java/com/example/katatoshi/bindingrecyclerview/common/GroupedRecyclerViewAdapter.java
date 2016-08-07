package com.example.katatoshi.bindingrecyclerview.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InvalidObjectException;
import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class GroupedRecyclerViewAdapter<T> extends RecyclerView.Adapter<GroupedRecyclerViewAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<ItemGroupBinder<T, ?>> itemGroupBinders;

    public List<ItemGroupBinder<T, ?>> getItemGroupBinders() {
        return itemGroupBinders;
    }

    private LayoutInflater inflater;

    public GroupedRecyclerViewAdapter(List<ItemGroupBinder<T, ?>> itemGroupBinders) {
        this.itemGroupBinders = itemGroupBinders;
        setItemGroups(itemGroupBinders);
    }

    public void setItemGroups(final List<ItemGroupBinder<T, ?>> itemGroupBinders) {
        if (this.itemGroupBinders == itemGroupBinders) {
            return;
        }

        if (this.itemGroupBinders != null) {
            rx.Observable.from(this.itemGroupBinders)
                    .forEach(new Action1<ItemGroupBinder<T, ?>>() {
                        @Override
                        public void call(ItemGroupBinder<T, ?> x) {
                            x.removeOnListChangedCallback();
                        }
                    });
            notifyItemRangeRemoved(0, rx.Observable.from(this.itemGroupBinders)
                    .map(new Func1<ItemGroupBinder<T, ?>, Integer>() {
                        @Override
                        public Integer call(ItemGroupBinder<T, ?> x) {
                            return 1 + x.getItemGroup().getItems().size();
                        }
                    })
                    .reduce(new Func2<Integer, Integer, Integer>() {
                        @Override
                        public Integer call(Integer acc, Integer x) {
                            return acc + x;
                        }
                    })
                    .toBlocking()
                    .single());
        }

        if (itemGroupBinders != null) {
            this.itemGroupBinders = itemGroupBinders;
            notifyItemRangeInserted(0, rx.Observable.from(this.itemGroupBinders)
                    .map(new Func1<ItemGroupBinder<T, ?>, Integer>() {
                        @Override
                        public Integer call(ItemGroupBinder<T, ?> x) {
                            return 1 + x.getItemGroup().getItems().size();
                        }
                    })
                    .reduce(new Func2<Integer, Integer, Integer>() {
                        @Override
                        public Integer call(Integer acc, Integer x) {
                            return acc + x;
                        }
                    })
                    .toBlocking()
                    .single());
            rx.Observable.from(this.itemGroupBinders)
                    .forEach(new Action1<ItemGroupBinder<T, ?>>() {
                        @Override
                        public void call(ItemGroupBinder<T, ?> x) {
                            x.setAdapter(GroupedRecyclerViewAdapter.this);
                            x.addOnListChangedCallback();
                        }
                    });
        } else {
            this.itemGroupBinders = null;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        if (itemGroupBinders == null) {
            return;
        }

        rx.Observable.from(itemGroupBinders)
                .forEach(new Action1<ItemGroupBinder<T, ?>>() {
                    @Override
                    public void call(ItemGroupBinder<T, ?> x) {
                        x.removeOnListChangedCallback();
                    }
                });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, viewType, parent, false);
        return new ViewHolder(binding);
    }

    private static final int TAG_ITEM = 0;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int headerPosition = 0;
        for (int i = 0; i < itemGroupBinders.size(); i++) {
            if (position == headerPosition) {
                holder.viewDataBinding.setVariable(itemGroupBinders.get(i).getHeaderBinder().getVariableId(), itemGroupBinders.get(i).getItemGroup().getHeader());
                holder.viewDataBinding.executePendingBindings();
                return;
            }

            if (headerPosition + 1 <= position && position < headerPosition + 1 + itemGroupBinders.get(i).getItemGroup().getItems().size()) {
                holder.viewDataBinding.setVariable(itemGroupBinders.get(i).getItemBinder().getVariableId(), itemGroupBinders.get(i).getItemGroup().getItems().get(position - (headerPosition + 1)));
//                holder.viewDataBinding.getRoot().setTag(TAG_ITEM, itemGroupBinders.get(i).getItemGroup().getItems().get(position - (headerPosition + 1)));
//                holder.viewDataBinding.getRoot().setOnClickListener(this);
//                holder.viewDataBinding.getRoot().setOnLongClickListener(this);
                holder.viewDataBinding.executePendingBindings();
                return;
            }

            headerPosition += 1 + itemGroupBinders.get(i).getItemGroup().getItems().size();
        }

        throw new IllegalStateException();
    }

    @Override
    public int getItemViewType(int position) {
        int headerPosition = 0;
        for (int i = 0; i < itemGroupBinders.size(); i++) {
            if (position == headerPosition) {
                return itemGroupBinders.get(i).getHeaderBinder().getLayoutId();
            }

            if (headerPosition + 1 <= position && position < headerPosition + 1 + itemGroupBinders.get(i).getItemGroup().getItems().size()) {
                return itemGroupBinders.get(i).getItemBinder().getLayoutId();
            }

            headerPosition += 1 + itemGroupBinders.get(i).getItemGroup().getItems().size();
        }

        throw new IllegalStateException();
    }

    @Override
    public int getItemCount() {
        return itemGroupBinders == null ? 0 : rx.Observable.from(this.itemGroupBinders)
                .map(new Func1<ItemGroupBinder<T, ?>, Integer>() {
                    @Override
                    public Integer call(ItemGroupBinder<T, ?> x) {
                        return 1 + x.getItemGroup().getItems().size();
                    }
                })
                .reduce(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer acc, Integer x) {
                        return acc + x;
                    }
                })
                .toBlocking()
                .single();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding viewDataBinding;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
        }
    }
}
