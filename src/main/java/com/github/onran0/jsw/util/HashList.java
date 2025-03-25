package com.github.onran0.jsw.util;

import java.util.ArrayList;

public class HashList<T> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        return !contains(t) && super.add(t);
    }

    @Override
    public void add(int index, T element) {
        if (!contains(element))
            super.add(index, element);
    }

    public int addOrGetIndex(T element) {
        int index = super.indexOf(element);

        if (index == -1) {
            super.add(element);
            return size() - 1;
        } else return index;
    }
}