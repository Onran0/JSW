package com.github.onran0.jsw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public final class StringUtils {
    public static final String ASCII = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    private static final CharsetEncoder ENCODER = StandardCharsets.US_ASCII.newEncoder();
    private static final CharsetDecoder DECODER = StandardCharsets.US_ASCII.newDecoder();

    public static boolean isASCIIString(String str) {
        if(str.isEmpty()) return false;

        for(char c : str.toCharArray()) {
            if(ASCII.indexOf(c) == -1) return false;
        }

        return true;
    }

    public static void writeASCII(OutputStream out, String ascii) throws IOException {
        out.write(ENCODER.encode(CharBuffer.wrap(ascii)).array());
        ENCODER.reset();
    }

    public static String readASCII(InputStream in, int len) throws IOException {
        String str = new String(DECODER.decode(ByteBuffer.wrap(in.readNBytes(len))).array());
        DECODER.reset();
        return str;
    }
}