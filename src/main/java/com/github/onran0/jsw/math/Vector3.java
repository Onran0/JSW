package com.github.onran0.jsw.math;

import com.github.onran0.jsw.io.ISerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Vector3 implements ISerializable {
    public float x, y, z;

    public Vector3() {}

    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vector3 min(Vector3 a, Vector3 b) {
        return new Vector3(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }

    public static Vector3 max(Vector3 a, Vector3 b) {
        return new Vector3(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }

    public static Vector3 add(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3 sub(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vector3 mul(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vector3 div(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
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
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }
}