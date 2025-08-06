package com.github.onran0.jsw.io;

import com.github.onran0.jsw.util.StringUtils;

import java.io.*;

public final class NETCompatibleIO {

    public static void writeString(String str, OutputStream out) throws IOException {
        byte[] utf8Bytes = StringUtils.toBytes(StringUtils.UTF8_ENCODER, str);
        write7BitEncodedInt(out, utf8Bytes.length);
        out.write(utf8Bytes);
    }

    public static String readString(InputStream in) throws IOException {
        int length = read7BitEncodedInt(in);
        byte[] utf8Bytes = in.readNBytes(length);

        if (utf8Bytes.length != length)
            throw new IOException("Unexpected end of stream.");

        return StringUtils.fromBytes(StringUtils.UTF8_DECODER, utf8Bytes);
    }

    public static int read7BitEncodedInt(InputStream in) throws IOException {
        int result = 0;
        int bitsRead = 0;
        int byteRead;

        do {
            byteRead = in.read();
            if (byteRead == -1)
                throw new IOException("Unexpected end of stream while decoding 7-bit int.");

            result |= (byteRead & 0x7F) << bitsRead;
            bitsRead += 7;

            if (bitsRead > 35)
                throw new IOException("Too many bytes when decoding 7-bit int.");
        } while ((byteRead & 0x80) != 0);

        return result;
    }

    public static void write7BitEncodedInt(OutputStream out, int value) throws IOException {
        while (value >= 0x80) {
            out.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }

        out.write(value);
    }
}