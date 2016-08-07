package com.example.katatoshi.bindingrecyclerview.common;

public class ItemBinder {
    private int variableId;

    public int getVariableId() {
        return variableId;
    }

    private int layoutId;

    public int getLayoutId() {
        return layoutId;
    }

    public ItemBinder(int variableId, int layoutId) {
        this.variableId = variableId;
        this.layoutId = layoutId;
    }
}
