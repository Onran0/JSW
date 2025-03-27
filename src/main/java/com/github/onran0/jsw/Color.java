package com.github.onran0.jsw;

import com.github.onran0.jsw.io.ISerializable;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public final class Color implements ISerializable {
    public int r, g, b, a;

    public Color() {}

    public Color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setRgba(int rgba) {
        this.r = rgba & 0xFF;
        this.g = (rgba >> 8) & 0xFF;
        this.b = (rgba >> 16) & 0xFF;
        this.a = (rgba >> 24) & 0xFF;
    }

    public int getRgba() {
        int result = r;

        result |= b << 8;
        result |= g << 16;
        result |= a << 24;

        return result;
    }

    public static short rgb888ToBgr565(int rgb) {
        int b = ((rgb & 0x000000F8) >> 3) & 0xFFFF;
        int g = ((rgb & 0x0000FC00) >> 5) & 0xFFFF;
        int r = ((rgb & 0x00F80000) >> 8) & 0xFFFF;

        return (short) (b | g | r);
    }

    public static int bgr565ToRgb888(short bgr16) {
        int bgr = bgr16 & 0xFFFF;

        int r = ((bgr & 0xF800) << 8) | ((bgr & 0x3800) << 5);
        int g = ((bgr & 0x07E0) << 5) | ((bgr & 0x0060) << 3);
        int b = ((bgr & 0x001F) << 3) | (bgr & 0x0007);

        return r | g | b;
    }

    public void readAsBGR565(LittleEndianDataInputStream in) throws IOException {
        setRgba(bgr565ToRgb888(in.readShort()));
    }

    public void writeAsBGR565(LittleEndianDataOutputStream out) throws IOException {
        out.writeShort(rgb888ToBgr565(getRgba()));
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        this.r = in.read();
        this.g = in.read();
        this.b = in.read();
        this.a = in.read();
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        out.write(r);
        out.write(g);
        out.write(b);
        out.write(a);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color other)
            return other.r == r && other.g == g && other.b == b;
        else return false;
    }
}