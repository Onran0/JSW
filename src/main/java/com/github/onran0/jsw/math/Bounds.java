package com.github.onran0.jsw.math;

public final class Bounds {
    private Vector3 center, size;

    public Bounds(Vector3 center, Vector3 size) {
        this.center = center;
        this.size = Vector3.mul(size, 0.5f);
    }

    public Vector3 getCenter() {
        return center;
    }

    public Vector3 getSize() {
        return size;
    }

    public void setCenter(Vector3 center) {
        this.center = center;
    }

    public void setSize(Vector3 size) {
        this.size = size;
    }

    public Vector3 getMaximum() {
        return Vector3.add(center, size);
    }

    public Vector3 getMinimum() {
        return Vector3.sub(center, size);
    }

    public void setMinimum(Vector3 minimum) {
        this.setMinMax(minimum, getMaximum());
    }

    public void setMaximum(Vector3 maximum) {
        this.setMinMax(getMinimum(), maximum);
    }

    public void encapsulate(Vector3 point) {
        this.setMinMax(Vector3.min(getMinimum(), point), Vector3.max(getMaximum(), point));
    }

    public void setMinMax(Vector3 min, Vector3 max) {
        this.size = Vector3.mul(Vector3.sub(max, min), 0.5f);
        this.center = Vector3.add(min, this.size);
    }
}