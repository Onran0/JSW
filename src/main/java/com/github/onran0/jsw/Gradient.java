package com.github.onran0.jsw;

import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.util.BinaryUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Gradient implements ISerializable {

    public Color[] colorKeys;
    public float[] colorTimeKeys;

    public float[] alphaKeys;
    public float[] alphaTimeKeys;

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
        this.colorKeys = BinaryUtil.readArray(in, Color[]::new, Color::new);
        this.colorTimeKeys = BinaryUtil.readFloatArray(in);
        this.alphaKeys = BinaryUtil.readFloatArray(in);
        this.alphaTimeKeys = BinaryUtil.readFloatArray(in);
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
         BinaryUtil.writeArray(out, colorKeys);
         BinaryUtil.writeArray(out, colorTimeKeys);
         BinaryUtil.writeArray(out, alphaKeys);
         BinaryUtil.writeArray(out, alphaTimeKeys);
    }
}