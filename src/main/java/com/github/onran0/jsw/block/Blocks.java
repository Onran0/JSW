package com.github.onran0.jsw.block;

import com.github.onran0.jsw.io.ISerializable;

import java.util.function.Supplier;

public final class Blocks {

    public static boolean isCustomBlock(int id) {
        return false;
    }

    public static boolean isInteractable(int id) {
        return false;
    }

    public static Supplier<ISerializable> getBlockCustomMetadata(int id) {
        return null;
    }
}