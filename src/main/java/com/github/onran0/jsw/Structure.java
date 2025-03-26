package com.github.onran0.jsw;

import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.math.Vector3;
import com.github.onran0.jsw.util.HashList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Structure implements ISerializable {
    private static final float ROTATION_MUL = ( (Short.MAX_VALUE & 0xFFFF) / 360f);
    private static final float ROTATION_MUL_2 = 2047 / 360f;

    public static final int MAX_SUPPORTED_VERSION = 6;
    public static final int MIN_SUPPORTED_VERSION = 6;
    public static final int LATEST_VERSION = 6;

    private int version = -1;

    private final HashList<Color> colorsTable = new HashList<>();
    private final HashList<Vector3> rotationsTable = new HashList<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void read(final DataInputStream in) throws IOException {
        read(in, 0);
    }

    public void write(final DataOutputStream out) throws IOException {
        write(out, version);
    }

    private void checkVersion(int version) throws IOException {
        if(MAX_SUPPORTED_VERSION < version || version < MIN_SUPPORTED_VERSION)
            throw new IOException("Unsupported structure version: " + version);
    }

    @Override
    public void read(DataInputStream in, int version) throws IOException {
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

                rotation.x = in.readUnsignedShort() / ROTATION_MUL;
                rotation.y = in.readUnsignedShort() / ROTATION_MUL;
                rotation.z = in.readUnsignedShort() / ROTATION_MUL;

                rotationsTable.add(rotation);
            }
        }
    }

    @Override
    public void write(DataOutputStream out, int version) throws IOException {
        if(version == -1)
            version = LATEST_VERSION;
        else
            checkVersion(version);

        out.write(version);

        this.version = version;

        boolean colorsTableDefined = true;
        boolean rotationsTableDefined = true;

        out.write(colorsTableDefined ? colorsTable.size() : 0xFF);
        out.write(rotationsTableDefined ? rotationsTable.size() : 0xFFFF);

        if(colorsTableDefined) {
            for (Color color : colorsTable)
                color.writeAsBGR565(out);
        }

        if(rotationsTableDefined) {
            for (Vector3 rotation : rotationsTable) {
                out.writeShort((short) (rotation.x * ROTATION_MUL));
                out.writeShort((short) (rotation.y * ROTATION_MUL));
                out.writeShort((short) (rotation.z * ROTATION_MUL));
            }
        }
    }
}