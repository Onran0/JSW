package com.github.onran0.jsw;

import com.github.onran0.jsw.io.ISerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Color implements ISerializable {
    public byte r, g, b, a;

    public Color() {}

    public Color(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setRgba(int rgba) {
        this.r = (byte) (rgba & 0xFF);
        this.g = (byte) ((rgba >> 8) & 0xFF);
        this.b = (byte) ((rgba >> 16) & 0xFF);
        this.a = (byte) ((rgba >> 24) & 0xFF);
    }

    public int getRgba() {
        int result = r & 0xFF;

        result |= (b & 0xFF) << 8;
        result |= (g & 0xFF) << 16;
        result |= (a & 0xFF) << 24;

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

    public void readAsBGR565(DataInputStream in) throws IOException {
        setRgba(bgr565ToRgb888(in.readShort()));
    }

    public void writeAsBGR565(DataOutputStream out) throws IOException {
        out.writeShort(rgb888ToBgr565(getRgba()));
    }

    @Override
    public void read(DataInputStream in, int version) throws IOException {
        read(in);
    }

    @Override
    public void write(DataOutputStream out, int version) throws IOException {
        write(out);
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        this.r = in.readByte();
        this.g = in.readByte();
        this.b = in.readByte();
        this.a = in.readByte();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(r);
        out.writeByte(g);
        out.writeByte(b);
        out.writeByte(a);
    }
}