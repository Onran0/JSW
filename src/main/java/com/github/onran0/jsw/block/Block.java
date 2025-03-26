package com.github.onran0.jsw.block;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.math.Vector3;

public final class Block {
    public final Vector3 position = Vector3.zero();
    public final Vector3 rotation = Vector3.zero();
    private int id;
    private String name;
    private float speed;
    private float value;
    private int bearingBlockId;
    private int[] connectedOutputs;
    private int[] additionalInts;
    private Color color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getBearingBlockId() {
        return bearingBlockId;
    }

    public void setBearingBlockId(int bearingBlockId) {
        this.bearingBlockId = bearingBlockId;
    }

    public int[] getConnectedOutputs() {
        return connectedOutputs;
    }

    public void setConnectedOutputs(int[] connectedOutputs) {
        this.connectedOutputs = connectedOutputs;
    }

    public int[] getAdditionalInts() {
        return additionalInts;
    }

    public void setAdditionalInts(int[] additionalInts) {
        this.additionalInts = additionalInts;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}