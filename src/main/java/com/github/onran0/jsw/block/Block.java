package com.github.onran0.jsw.block;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Structure;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.BinaryUtil;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Block {
    public static final float ROTATION_MUL = ( (Short.MAX_VALUE & 0xFFFF) / 360f);

    public final Vector3 position = Vector3.zero();
    public final Vector3 rotation = Vector3.zero();
    private int id;
    private String name;
    private float speed;
    private float value;
    private int bearingBlockId = -1;
    private int[] connectedOutputsIds;
    private int[] additionalInts;
    private BlockMetadata metadata;
    private Color color;
    private List<Block> connectedOutputs = new ArrayList<>();

    public int rotationId, colorId;

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

    public List<Block> getConnectedOutputs() {
        return connectedOutputs;
    }

    public void connectOutput(Block output) {
        connectedOutputs.add(output);
    }

    private void throwBlockNotFound(int id) throws IOException {
        throw new IOException("Block with id " + id + " not found");
    }

    public void setupConnectedOutputs(Structure struct) throws IOException {
        connectedOutputs.clear();

        for (int id : connectedOutputsIds) {
            Block block = struct.getBlock(id);

            if (block == null)
                throwBlockNotFound(id);

            connectedOutputs.add(block);
        }
    }

    public void setConnectedOutputs(List<Block> connectedOutputs) {
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

    public void setColor(int rgba) {
        this.color = new Color();

        color.setRgba(rgba);
    }

    public BlockMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BlockMetadata metadata) {
        this.metadata = metadata;
    }

    private short encodePositionComponent(float f, float offset, float size)
    {
        return (short) ((f - offset) * size);
    }

    private float decodePositionComponent(short t, float offset, float size)
    {
        return (t / size) + offset;
    }

    public void read(LittleEndianDataInputStream in, Root root, Structure struct, int version) throws IOException {
        Vector3 offset = root.getBounds().getCenter();
        Vector3 size = root.getBlockPositionMultiplier();

        position.x = decodePositionComponent(in.readShort(), offset.x, size.x);
        position.y = decodePositionComponent(in.readShort(), offset.y, size.y);
        position.z = decodePositionComponent(in.readShort(), offset.z, size.z);

        if(!struct.getRotationsTable().isEmpty()) {
            if(struct.getRotationsTable().size() <= 255)
                rotationId = in.read();
            else
                rotationId = in.readShort() & 0xFFFF;

            rotation.set(struct.getRotationsTable().get(rotationId));
        } else {
            rotation.x = (in.readShort() & 0xFFFF) / ROTATION_MUL;
            rotation.y = (in.readShort() & 0xFFFF) / ROTATION_MUL;
            rotation.z = (in.readShort() & 0xFFFF) / ROTATION_MUL;
        }

        id = in.read();

        boolean[] bools = BinaryUtil.toBools(in.read());

        boolean interactable = Blocks.isInteractableBlock(id, version);

        if (interactable || bools[7])
            speed = in.read() / (bools[6] ? 1f : 255f); // 0

        if(interactable) {
            if (bools[0])
                name = in.readUTF();

            value = in.read() / 255f; // 0

            if(!bools[4])
                bearingBlockId = in.readShort() & 0xFFFF;

            if(bools[1]) {
                connectedOutputsIds = new int[in.read()];

                for (int i = 0; i < connectedOutputsIds.length; i++)
                    connectedOutputsIds[i] = in.readShort() & 0xFFFF;
            }

            if (!bools[5])
                additionalInts = BinaryUtil.readIntArray255(in);

            if (!bools[2])
                (metadata = new BlockMetadata()).read(in, id, version);
        }

        if (!bools[3]) {
            if (!struct.getColorsTable().isEmpty()) {
                colorId = in.read();
                color = struct.getColorsTable().get(colorId);
            } else
                (color = new Color()).readAsBGR565(in);
        }
    }

    public void write(LittleEndianDataOutputStream out, Root root, Structure struct, int version) throws IOException {
        Vector3 offset = root.getBounds().getCenter();
        Vector3 size = root.getBlockPositionMultiplier();

        out.writeShort(encodePositionComponent(position.x, offset.x, size.x));
        out.writeShort(encodePositionComponent(position.y, offset.y, size.y));
        out.writeShort(encodePositionComponent(position.z, offset.z, size.z));

        if(!struct.getRotationsTable().isEmpty()) {
            if(struct.getRotationsTable().size() <= 255)
                out.write(rotationId);
            else
                out.writeShort((short) rotationId);
        } else {
            out.writeShort((short) (rotation.x * ROTATION_MUL));
            out.writeShort((short) (rotation.y * ROTATION_MUL));
            out.writeShort((short) (rotation.z * ROTATION_MUL));
        }

        out.write(id);

        boolean[] bools = {
                name != null && !name.isEmpty(),
                connectedOutputs != null && connectedOutputs.size() > 0,
                metadata == null,
                color == null,
                bearingBlockId == -1,
                additionalInts == null || additionalInts.length == 0,
                speed > 1,
                speed != 0
        };

        // 00101100

        out.write(BinaryUtil.toByte(bools));

        boolean interactable = Blocks.isInteractableBlock(id, version);

        if (interactable || bools[7])
            out.write((int) (speed * (bools[6] ? 1 : 255)));

        if(interactable) {
            if (bools[0])
                out.writeUTF(name);

            out.write((int) (value * 255));

            if(!bools[4])
                out.writeShort((short) bearingBlockId);

            if(bools[1]) {
                out.write(connectedOutputs.size());

                for(Block block : connectedOutputs) {
                    int id = struct.getBlockId(block);

                    if(id == -1)
                        throwBlockNotFound(id);

                    out.writeShort((short) id);
                }
            }

            if (bools[5])
                BinaryUtil.writeArray255(out, additionalInts);

            if(!bools[2])
                metadata.write(out, id, version);
        }

        if(!bools[3]) {
            if(!struct.getColorsTable().isEmpty())
                out.write(colorId);
            else
                color.writeAsBGR565(out);
        }
    }
}