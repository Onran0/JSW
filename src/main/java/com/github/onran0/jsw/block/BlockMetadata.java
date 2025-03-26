package com.github.onran0.jsw.block;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Gradient;
import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.BinaryUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BlockMetadata implements ISerializable {
    private boolean[] booleans;
    private float[] floats;
    private int[][] integers2DArray;
    private Color[] colors;
    private Gradient[] gradients;
    private Vector3[] vectors;

    @Override
    public void read(DataInputStream in, int version) throws IOException {
        booleans = BinaryUtil.readPackedBools255(in);
        floats = BinaryUtil.readFloatArray(in);
    }

    @Override
    public void write(DataOutputStream out, int version) throws IOException {

    }
}