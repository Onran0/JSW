package com.github.onran0.jsw.math;

import com.github.onran0.jsw.io.ISerializable;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public final class Vector3 implements ISerializable {
    public float x, y, z;

    public Vector3() {}

    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(final Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vector3 other)
            return x == other.x && y == other.y && z == other.z;
        else return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    public static Vector3 one() {
        return new Vector3(1, 1, 1);
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

    public static Vector3 mul(final Vector3 a, final float b) {
        return new Vector3(a.x * b, a.y * b, a.z * b);
    }

    public static Vector3 div(final Vector3 a, final Vector3 b) {
        return new Vector3(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public static Vector3 div(final Vector3 a, final float b) {
        return new Vector3(a.x / b, a.y / b, a.z / b);
    }

    public Vector3 inverse() {
        return new Vector3(1 / x, 1 / y, 1 / z);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.z = in.readFloat();
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }
}