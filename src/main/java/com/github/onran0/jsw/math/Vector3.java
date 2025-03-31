package com.github.onran0.jsw.math;

import com.github.onran0.jsw.io.ISerializable;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.IOException;

public final class Vector3 implements ISerializable {
    private float x;
    private float y;
    private float z;

    public Vector3() {}

    public Vector3(final float x, final float y, final float z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(final float x, final float y, final float z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public void set(final Vector3 v) {
        this.setX(v.getX());
        this.setY(v.getY());
        this.setZ(v.getZ());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vector3 other)
            return getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ();
        else return false;
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getZ() {
            return z;
        }

        public void setZ(float z) {
            this.z = z;
        }

    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    public static Vector3 one() {
        return new Vector3(1, 1, 1);
    }

    public static Vector3 min(Vector3 a, Vector3 b) {
        return new Vector3(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
    }

    public static Vector3 max(Vector3 a, Vector3 b) {
        return new Vector3(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
    }

    public static Vector3 add(final Vector3 a, final Vector3 b) {
        return new Vector3(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }

    public static Vector3 sub(final Vector3 a, final Vector3 b) {
        return new Vector3(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static Vector3 mul(final Vector3 a, final Vector3 b) {
        return new Vector3(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ());
    }

    public static Vector3 mul(final Vector3 a, final float b) {
        return new Vector3(a.getX() * b, a.getY() * b, a.getZ() * b);
    }

    public static Vector3 div(final Vector3 a, final Vector3 b) {
        return new Vector3(a.getX() / b.getX(), a.getY() / b.getY(), a.getZ() / b.getZ());
    }

    public static Vector3 div(final Vector3 a, final float b) {
        return new Vector3(a.getX() / b, a.getY() / b, a.getZ() / b);
    }

    public Vector3 inverse() {
        return new Vector3(1 / getX(), 1 / getY(), 1 / getZ());
    }

    public float length() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        this.setX(in.readFloat());
        this.setY(in.readFloat());
        this.setZ(in.readFloat());
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        out.writeFloat(getX());
        out.writeFloat(getY());
        out.writeFloat(getZ());
    }
}