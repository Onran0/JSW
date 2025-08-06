package com.github.onran0.jsw.block;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Structure;
import com.github.onran0.jsw.io.NETCompatibleIO;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.BinaryUtil;
import com.github.onran0.jsw.util.StringUtils;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Block {
    public static final float ROTATION_MUL = ( (Short.MAX_VALUE & 0xFFFF) / 360f);

    private final Vector3 position = Vector3.zero();
    private final Vector3 rotation = Vector3.zero();
    private int id;
    private String name;
    private float speed;
    private float value;
    private int bearingBlockId = -1;
    private int[] connectedOutputsIds;
    private int[] additionalInts;
    private BlockMetadata metadata;
    private Color color;
    private Block bearingBlock;
    private List<Block> connectedOutputs = new ArrayList<>();

    public Block() { }

    public Block(int id) {
        this.id = id;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getRotation() {
        return rotation;
    }

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

    public Block getBearingBlock() {
        return bearingBlock;
    }

    public void setBearingBlock(Block bearingBlock) {
        this.bearingBlock = bearingBlock;
    }

    public List<Block> getConnectedOutputs() {
        return connectedOutputs;
    }

    public void connectOutput(Block output) {
        connectedOutputs.add(output);
    }

    public void connectInput(Block input) {
        input.connectOutput(this);
    }

    private void throwBlockNotFound(int id) throws IOException {
        throw new IOException("Block with id " + id + " not found");
    }

    public void postBlocksRead(Structure struct) throws IOException {
        connectedOutputs.clear();

        if(connectedOutputsIds != null) {
            for (int id : connectedOutputsIds) {
                Block block = struct.getBlockById(id);

                if (block == null)
                    throwBlockNotFound(id);

                connectedOutputs.add(block);
            }
        }

        if(bearingBlockId != -1) {
            Block block = struct.getBlockById(bearingBlockId);

            if (block == null)
                throwBlockNotFound(bearingBlockId);

            bearingBlock = block;
        } else bearingBlock = null;
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

        getPosition().setX(decodePositionComponent(in.readShort(), offset.getX(), size.getX()));
        getPosition().setY(decodePositionComponent(in.readShort(), offset.getY(), size.getY()));
        getPosition().setZ(decodePositionComponent(in.readShort(), offset.getZ(), size.getZ()));

        if(!struct.getRotationsTable().isEmpty()) {
            if(struct.getRotationsTable().size() <= 256)
                rotationId = in.read();
            else
                rotationId = in.readUnsignedShort();

            getRotation().set(struct.getRotationsTable().get(rotationId));
        } else {
            getRotation().setX((in.readShort() & 0xFFFF) / ROTATION_MUL);
            getRotation().setY((in.readShort() & 0xFFFF) / ROTATION_MUL);
            getRotation().setZ((in.readShort() & 0xFFFF) / ROTATION_MUL);
        }

        id = in.read();

        boolean[] bools = BinaryUtil.toBools(in.read());

        boolean interactable = Blocks.isInteractableBlock(id, version);

        if (interactable || bools[7])
            speed = in.read() / (bools[6] ? 1f : 255f);

        if(interactable) {
            if (bools[0])
                name = NETCompatibleIO.readString(in);

            value = in.read() / 255f;

            if(!bools[4])
                bearingBlockId = in.readUnsignedShort();
            else
                bearingBlockId = -1;

            if(bools[1]) {
                connectedOutputsIds = new int[in.read()];

                for (int i = 0; i < connectedOutputsIds.length; i++)
                    connectedOutputsIds[i] = in.readUnsignedShort();
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

        out.writeShort(encodePositionComponent(getPosition().getX(), offset.getX(), size.getX()));
        out.writeShort(encodePositionComponent(getPosition().getY(), offset.getY(), size.getY()));
        out.writeShort(encodePositionComponent(getPosition().getZ(), offset.getZ(), size.getZ()));

        if(!struct.getRotationsTable().isEmpty()) {
            if(struct.getRotationsTable().size() <= 256)
                out.write(rotationId);
            else
                out.writeShort((short) rotationId);
        } else {
            out.writeShort((short) (getRotation().getX() * ROTATION_MUL));
            out.writeShort((short) (getRotation().getY() * ROTATION_MUL));
            out.writeShort((short) (getRotation().getZ() * ROTATION_MUL));
        }

        out.write(id);

        boolean[] bools = {
                name != null && !name.isEmpty(),
                connectedOutputs != null && !connectedOutputs.isEmpty(),
                metadata == null,
                color == null,
                bearingBlock == null,
                additionalInts == null || additionalInts.length == 0,
                speed > 1,
                speed != 0
        };

        out.write(BinaryUtil.toByte(bools));

        boolean interactable = Blocks.isInteractableBlock(id, version);

        if (interactable || bools[7])
            out.write((int) (speed * (bools[6] ? 1 : 255)));

        if(interactable) {
            if (bools[0])
                NETCompatibleIO.writeString(name, out);

            out.write((int) (value * 255));

            if(!bools[4]) {
                int id = struct.getBlockId(bearingBlock);

                if(id == -1)
                    throw new IOException("Unlisted bearing block");

                out.writeShort((short) id);
            }

            if(bools[1]) {
                if (connectedOutputs.size() > 255)
                    throw new IOException("The number of block connections exceeds 255");

                out.write(connectedOutputs.size());

                for(Block block : connectedOutputs) {
                    int id = struct.getBlockId(block);

                    if(id == -1)
                        throwBlockNotFound(id);

                    out.writeShort((short) id);
                }
            }

            if (!bools[5])
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