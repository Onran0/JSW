package com.github.onran0.jsw.block.meta;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Gradient;
import com.github.onran0.jsw.block.Blocks;
import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.BinaryUtil;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class BlockMetadata implements ISerializable {
    private boolean[] ticks = new boolean[0];
    private float[] sliders = new float[0];
    private int[][] groups = new int[0][];
    private int[] dropFields = new int[0];
    private List<Color> colors = new ArrayList<>();
    private List<Gradient> gradients = new ArrayList<>();
    private List<Vector3> vectors = new ArrayList<>();
    private ISerializable customMetadata;

    public int[][] getGroups() {
        return groups;
    }

    public void setGroups(int[][] groups) {
        this.groups = groups;
    }

    public int[] getDropFields() {
        return dropFields;
    }

    public void setDropFields(int[] dropFields) {
        this.dropFields = dropFields;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public List<Gradient> getGradients() {
        return gradients;
    }

    public List<Vector3> getVectors() {
        return vectors;
    }

    public boolean[] getTicks() {
        return ticks;
    }

    public void setTicks(boolean[] ticks) {
        this.ticks = ticks;
    }

    public float[] getSliders() {
        return sliders;
    }

    public void setSliders(float[] sliders) {
        this.sliders = sliders;
    }

    public void setGradients(List<Gradient> gradients) {
        this.gradients = gradients;
    }

    public void setVectors(List<Vector3> vectors) {
        this.vectors = vectors;
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        read(in, 0, version);
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        write(out, 0, version);
    }

    private int encodeComponent(float comp, float minComp, float maxComp) {
        return (short) ( (comp - minComp) / (maxComp - minComp) * (Short.MAX_VALUE & 0xFFFF) ) & 0xFFFF;
    }

    private float decodeComponent(int comp, float minComp, float maxComp) {
        return (float) comp / (Short.MAX_VALUE & 0xFFFF) * (maxComp - minComp) + minComp;
    }

    public void read(LittleEndianDataInputStream in, int id, int version) throws IOException {

        ticks = BinaryUtil.readPackedBools255(in);
        sliders = BinaryUtil.readFloatArray255(in);

        boolean custom = Blocks.isCustomBlock(id, version);

        int length = in.read();

        boolean hasVectors = length >= 127;

        vectors.clear();

        if (hasVectors || custom) {
            if(!custom) {
                length -= 127;
                Collections.addAll(vectors, BinaryUtil.readArray255(in, Vector3[]::new, Vector3::new, version));
            } else {
                byte minComp = in.readByte();
                byte maxComp = in.readByte();

                for (int i = 0; i < length; i++) {
                    vectors.add(new Vector3(
                        decodeComponent(in.readUnsignedShort(), minComp, maxComp),
                        decodeComponent(in.readUnsignedShort(), minComp, maxComp),
                        decodeComponent(in.readUnsignedShort(), minComp, maxComp)
                    ));
                }

                length = 0;
            }
        }

        groups = new int[length][];

        for (int i = 0; i < length; i++) {
            int subLength = in.read();

            groups[i] = new int[subLength];

            for (int j = 0; j < subLength; j++) {
                int value = in.readUnsignedShort();

                groups[i][j] = value == 0xFFFF ? -1 : value;
            }
        }

        int mask = in.read();

        dropFields = new int[mask & 0x3F];

        for (int i = 0; i < dropFields.length; i++) {
            dropFields[i] = in.read();
        }

        if((mask & 0x40) != 0)
            Collections.addAll(colors, BinaryUtil.readArray255(in, Color[]::new, Color::new, version));

        if((mask & 0x80) != 0)
            Collections.addAll(gradients, BinaryUtil.readArray255(in, Gradient[]::new, Gradient::new, version));

        Supplier<ISerializable> customMetadataProvider = Blocks.getBlockCustomMetadata(id, version);

        if(customMetadataProvider != null) {
            customMetadata = customMetadataProvider.get();

            customMetadata.read(in, version);
        }
    }

    public void write(LittleEndianDataOutputStream out, int id, int version) throws IOException {
        BinaryUtil.writePackedBools255(out, ticks);
        BinaryUtil.writeArray255(out, sliders);

        boolean custom = Blocks.isCustomBlock(id, version);

        if(custom) {
            out.write(vectors.size());
        } else {
            int length = groups.length;

            if(vectors != null && !vectors.isEmpty())
                length += 127;

            out.write(length);
        }

        if (vectors != null && !vectors.isEmpty()) {
            if(!custom) {
                BinaryUtil.writeArray255(out, vectors.toArray(new Vector3[0]), version);
            } else {
                Vector3 minVec = Vector3.mul(Vector3.one(), Float.MAX_VALUE);
                Vector3 maxVec = Vector3.mul(Vector3.one(), Float.MIN_VALUE);

                for (Vector3 vec : vectors) {
                    minVec = Vector3.min(minVec, vec);
                    maxVec = Vector3.max(maxVec, vec);
                }

                byte min = (byte) Math.floor(Math.min(Math.min(minVec.getX(), minVec.getY()), minVec.getZ()));
                byte max = (byte) Math.ceil(Math.max(Math.max(maxVec.getX(), maxVec.getY()), maxVec.getZ()));

                out.writeByte(min);
                out.writeByte(max);

                for (Vector3 vec : vectors) {
                    out.writeFloat(encodeComponent(vec.getX(), min, max));
                    out.writeFloat(encodeComponent(vec.getY(), min, max));
                    out.writeFloat(encodeComponent(vec.getZ(), min, max));
                }
            }
        }

        for(int[] group : groups) {
            out.write(group.length);

            for(int value : group) {
                out.writeShort((short) ( value == -1 ? 0xFFFF : value ));
            }
        }

        int mask = dropFields.length;

        boolean hasColors = colors != null && !colors.isEmpty();
        boolean hasGradients = gradients != null && !gradients.isEmpty();

        if(hasColors)
            mask |= 0x40;

        if(hasGradients)
            mask |= 0x80;

        out.write(mask);

        for (int field : dropFields) {
            out.write(field);
        }

        if (hasColors)
            BinaryUtil.writeArray255(out, colors.toArray(new Color[0]), version);

        if (hasGradients)
            BinaryUtil.writeArray255(out, gradients.toArray(new Gradient[0]), version);

        if(customMetadata != null)
            customMetadata.write(out, version);
    }

    public ISerializable getCustomMetadata() {
        return customMetadata;
    }

    public void setCustomMetadata(ISerializable customMetadata) {
        this.customMetadata = customMetadata;
    }
}