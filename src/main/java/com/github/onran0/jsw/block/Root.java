package com.github.onran0.jsw.block;

import com.github.onran0.jsw.math.Bounds;
import com.github.onran0.jsw.math.Vector3;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Root {
    private Vector3 position = new Vector3(), rotation = new Vector3();
    private Bounds bounds;
    private List<Block> blocks = new ArrayList<>();
    private int blocksCount;

    private Vector3 blockPositionMultiplier;

    public Bounds getBounds() {
        return bounds;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public int getReadedBlocksCount() {
        return blocksCount;
    }

    public Vector3 getBlockPositionMultiplier() {
        return blockPositionMultiplier;
    }

    public int getBlockId(Block block) {
        return blocks.indexOf(block);
    }

    public Block getBlockByName(final String name) {
        return findBlock(x -> name.equals(x.getName()));
    }

    public Block findBlock(Predicate<Block> predicate) {
        for (Block block : blocks) {
            if (predicate.test(block))
                return block;
        }

        return null;
    }

    public void read(LittleEndianDataInputStream in, int blocksCount, int version) throws IOException {
        blocks.clear();

        position.read(in, version);
        rotation.read(in, version);

        Vector3 offset = new Vector3();
        Vector3 size = new Vector3();

        offset.read(in, version);
        size.read(in, version);

        bounds = new Bounds(offset, size);

        blockPositionMultiplier = Vector3.mul(bounds.getSize().inverse(), Short.MAX_VALUE);

        this.blocksCount = in.readUnsignedShort() - blocksCount;
    }

    public void write(LittleEndianDataOutputStream out, int blocksCount, int version) throws IOException {
        position.write(out, version);
        rotation.write(out, version);

        bounds = new Bounds(position, Vector3.one());

        for (Block block : blocks)
            bounds.encapsulate(block.position);

        bounds.getCenter().write(out, version);
        bounds.getSize().write(out, version);

        blockPositionMultiplier = Vector3.mul(bounds.getSize().inverse(), Short.MAX_VALUE);

        out.writeShort((short) (blocksCount + blocks.size() - 1));
    }
}