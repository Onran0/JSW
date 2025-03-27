package com.github.onran0.jsw;

import com.github.onran0.jsw.block.Block;
import com.github.onran0.jsw.block.Root;
import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.HashList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

public final class Structure implements ISerializable {
    public static final int MAX_SUPPORTED_VERSION = 6;
    public static final int MIN_SUPPORTED_VERSION = 6;

    private int version = -1;
    private List<Root> roots = new ArrayList<>();

    private final HashList<Color> colorsTable = new HashList<>();
    private final HashList<Vector3> rotationsTable = new HashList<>();

    public Structure() { }

    public Structure(InputStream in) throws IOException {
        this(new LittleEndianDataInputStream(in));
    }

    public Structure(LittleEndianDataInputStream in) throws IOException {
        read(in);

        in.close();
    }

    public List<Root> getRoots() {
        return roots;
    }

    public void setRoots(List<Root> roots) {
        this.roots = roots;
    }

    public HashList<Color> getColorsTable() {
        return colorsTable;
    }

    public HashList<Vector3> getRotationsTable() {
        return rotationsTable;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void read(String filename) throws IOException {
        read(new File(filename));
    }

    public void write(String filename) throws IOException {
        write(new File(filename));
    }

    public void read(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        read(fis);

        fis.close();
    }

    public void write(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);

        write(out);

        out.close();
    }

    public void read(InputStream in) throws IOException {
        read(new LittleEndianDataInputStream(in));
    }

    public void write(OutputStream out) throws IOException {
        write(new LittleEndianDataOutputStream(out));
    }

    public void read(final LittleEndianDataInputStream in) throws IOException {
        read(in, 0);
    }

    public void write(final LittleEndianDataOutputStream out) throws IOException {
        write(out, version);
    }

    private void checkVersion(int version) throws IOException {
        if(MAX_SUPPORTED_VERSION < version || version < MIN_SUPPORTED_VERSION)
            throw new IOException("Unsupported structure version: " + version);
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        version = in.read();

        checkVersion(version);

        this.version = version;

        colorsTable.clear();
        rotationsTable.clear();

        int colorCount = in.read();

        int rotationCount = in.readUnsignedShort();

        if (colorCount != 0xFF) {
            for (int i = 0; i < colorCount; i++) {
                Color color = new Color();

                color.readAsBGR565(in);

                colorsTable.add(color);
            }
        }

        if (rotationCount != 0xFFFF) {
            for (int i = 0; i < rotationCount; i++) {
                Vector3 rotation = new Vector3();

                rotation.x = in.readUnsignedShort() / Block.ROTATION_MUL;
                rotation.y = in.readUnsignedShort() / Block.ROTATION_MUL;
                rotation.z = in.readUnsignedShort() / Block.ROTATION_MUL;

                rotationsTable.add(rotation);
            }
        }

        roots.clear();

        int rootsCount = in.readUnsignedShort();

        int temp = 0;

        Map<Integer, Root> rootsMap = new HashMap<>();

        for (int i = 0; i < rootsCount; i++) {
            Root root = new Root();

            rootsMap.put(temp, root);

            root.read(in, temp, version);

            temp += root.getBlocksCount();

            roots.add(root);
        }

        int blocksCount = in.readUnsignedShort();

        Root root = null;

        for (int i = 0; i < blocksCount; i++) {
            Block block = new Block();

            if(rootsMap.containsKey(i))
                root = rootsMap.get(i);

            assert root != null;

            block.read(in, root, this, version);

            root.getBlocks().add(block);
        }
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        if(version == -1)
            version = MAX_SUPPORTED_VERSION;
        else
            checkVersion(version);

        out.write(version);

        int blocksCount = 0;
        int coloredBlocksCount = 0;

        for(Root root : roots) {
            for(Block block : root.getBlocks()) {
                blocksCount++;

                block.rotationId = rotationsTable.addOrGetIndex(block.rotation);

                if(block.getColor() == null)
                    continue;

                coloredBlocksCount++;

                block.colorId = colorsTable.addOrGetIndex(block.getColor());
            }
        }

        float avgColors = coloredBlocksCount / (float) colorsTable.size();
        float avgRots = blocksCount / (float) rotationsTable.size();

        boolean colorsTableDefined = avgColors > 2f && colorsTable.size() < 0xFF;
        boolean rotationsTableDefined = avgRots > (rotationsTable.size() <= 256 ? 1.2f : 1.5f) && rotationsTable.size() < 0xFFFF;

        out.write(colorsTableDefined ? colorsTable.size() : 0xFF);
        out.write(rotationsTableDefined ? rotationsTable.size() : 0xFFFF);

        if(colorsTableDefined) {
            for (Color color : colorsTable)
                color.writeAsBGR565(out);
        }

        if(rotationsTableDefined) {
            for (Vector3 rotation : rotationsTable) {
                out.writeShort((short) (rotation.x * Block.ROTATION_MUL));
                out.writeShort((short) (rotation.y * Block.ROTATION_MUL));
                out.writeShort((short) (rotation.z * Block.ROTATION_MUL));
            }
        }

        int temp = 0;

        out.writeShort((short) (roots.size()));

        for (Root root : roots) {
            root.write(out, temp, version);

            temp += root.getBlocksCount();
        }

        out.writeShort((short) (blocksCount));

        for (Root root : roots) {
            for (Block block : root.getBlocks()) {
                block.write(out, root, this, version);
            }
        }
    }
}