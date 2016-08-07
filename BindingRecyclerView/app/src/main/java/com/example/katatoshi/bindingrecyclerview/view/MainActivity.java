package com.example.katatoshi.bindingrecyclerview.view;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.katatoshi.bindingrecyclerview.BR;
import com.example.katatoshi.bindingrecyclerview.R;
import com.example.katatoshi.bindingrecyclerview.common.ItemBinder;
import com.example.katatoshi.bindingrecyclerview.common.ItemGroup;
import com.example.katatoshi.bindingrecyclerview.common.ItemGroupBinder;
import com.example.katatoshi.bindingrecyclerview.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> list1 = new ArrayList<Integer>() {
        {
            add(2);
            add(3);
            add(5);
            add(7);
            add(11);
        }
    };

    private List<String> list2 = new ArrayList<String>() {
        {
            add("apple");
            add("banana");
            add("cherry");
            add("melon");
            add("kiwi");
            add("grape");
        }
    };

    private ObservableList<Integer> observableList1 = new ObservableArrayList<>();

    private ObservableList<String> observableList2 = new ObservableArrayList<>();

    public List<ItemGroupBinder<String, ?>> itemGroupBinders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observableList1.addAll(list1);
        observableList2.addAll(list2);

        itemGroupBinders.add(new ItemGroupBinder<String, Integer>(new ItemGroup<String, Integer>("item1", observableList1), new ItemBinder(BR.title, R.layout.header), new ItemBinder(BR.item1, R.layout.item1)));
        itemGroupBinders.add(new ItemGroupBinder<String, String>(new ItemGroup<String, String>("item2", observableList2), new ItemBinder(BR.title, R.layout.header), new ItemBinder(BR.item2, R.layout.item2)));

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setView(this);
        setSupportActionBar(binding.toolbar);
    }
}
