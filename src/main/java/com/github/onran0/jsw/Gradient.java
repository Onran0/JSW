package com.github.onran0.jsw;

import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.util.BinaryUtil;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public class Gradient implements ISerializable {

    public Color[] colorKeys;
    public float[] colorTimeKeys;

    public float[] alphaKeys;
    public float[] alphaTimeKeys;

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        this.colorKeys = BinaryUtil.readArray(in, Color[]::new, Color::new, version);
        this.colorTimeKeys = BinaryUtil.readFloatArray(in);
        this.alphaKeys = BinaryUtil.readFloatArray(in);
        this.alphaTimeKeys = BinaryUtil.readFloatArray(in);
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        BinaryUtil.writeArray(out, colorKeys, version);
        BinaryUtil.writeArray(out, colorTimeKeys);
        BinaryUtil.writeArray(out, alphaKeys);
        BinaryUtil.writeArray(out, alphaTimeKeys);
    }
}