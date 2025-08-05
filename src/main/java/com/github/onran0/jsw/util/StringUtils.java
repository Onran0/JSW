package com.github.onran0.jsw.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public final class StringUtils {
    public static final CharsetEncoder ASCII_ENCODER = StandardCharsets.US_ASCII.newEncoder();
    public static final CharsetDecoder ASCII_DECODER = StandardCharsets.US_ASCII.newDecoder();
    public static final CharsetEncoder UTF8_ENCODER = StandardCharsets.UTF_8.newEncoder();
    public static final CharsetDecoder UTF8_DECODER = StandardCharsets.UTF_8.newDecoder();

    public static String fromBytes(CharsetDecoder decoder, final byte[] bytes) throws IOException {
        CharBuffer charBuffer = decoder.decode(ByteBuffer.wrap(bytes));
        decoder.reset();
        return charBuffer.toString();
    }

    public static byte[] toBytes(CharsetEncoder encoder, final String str) throws IOException {
        ByteBuffer buffer = encoder.encode(CharBuffer.wrap(str));
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        encoder.reset();
        return bytes;
    }

    public static String readString(CharsetDecoder decoder, InputStream in, int len) throws IOException {
        String str = new String(decoder.decode(ByteBuffer.wrap(in.readNBytes(len))).array());
        decoder.reset();
        return str;
    }

    public static void writeString(CharsetEncoder encoder, OutputStream out, String str) throws IOException {
        out.write(encoder.encode(CharBuffer.wrap(str)).array());
        encoder.reset();
    }
}