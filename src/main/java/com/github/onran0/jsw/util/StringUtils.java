package com.github.onran0.jsw.util;

public final class StringUtils {
    public static final String ASCII = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    public static boolean isASCIIString(String str) {
        if(str.isEmpty()) return false;

        for(char c : str.toCharArray()) {
            if(ASCII.indexOf(c) == -1) return false;
        }

        return true;
    }
}