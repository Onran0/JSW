package com.github.onran0.jsw.util;

import com.github.onran0.jsw.io.ISerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BinaryUtil {

    public static void writePackedBools255(DataOutputStream out, boolean[] bools) throws IOException {
        out.write(bools.length);

        int index = 0;

        while (index < bools.length)
        {
            int packedByte = 0;

            for (int bit = 7; bit >= 0 && index < bools.length; bit--, index++)
            {
                if (bools[index])
                    packedByte |= 1 << bit;
            }

            out.write(packedByte);
        }
    }

    public static boolean[] readPackedBools255(DataInputStream in) throws IOException {
        boolean[] bools = new boolean[in.read()];

        int bytes = (int) Math.ceil(bools.length / 8.0);

        int index = 0;

        for (int bi = 0;bi < bytes;bi++)
        {
            int b = in.read();

            for (int bit = 7; bit >= 0 && index < bools.length; bit--)
            {
                bools[index++] = (b & (1 << bit)) != 0;
            }
        }

        return bools;
    }

    public static <T extends ISerializable> void writeArray(DataOutputStream out, T[] array, int version) throws IOException {
        out.writeShort(array.length);

        for (T value : array)
            value.write(out, version);
    }

    public static <T extends ISerializable> void writeArray255(DataOutputStream out, T[] array, int version) throws IOException {
        out.write(array.length);

        for (T value : array)
            value.write(out, version);
    }

    public static <T extends ISerializable> T[] readArray(
            DataInputStream in,
            Function<Integer, T[]> arrCreator,
            Supplier<T> objCreator,
            int version
    ) throws IOException {
        T[] array = arrCreator.apply(in.readUnsignedShort());

        for (int i = 0; i < array.length; i++)
            (array[i] = objCreator.get()).read(in, version);

        return array;
    }

    public static <T extends ISerializable> T[] readArray255(
            DataInputStream in,
            Function<Integer, T[]> arrCreator,
            Supplier<T> objCreator,
            int version
    ) throws IOException {
        T[] array = arrCreator.apply(in.readUnsignedByte());

        for (int i = 0; i < array.length; i++)
            (array[i] = objCreator.get()).read(in, version);

        return array;
    }

    public static void writeArray(DataOutputStream out, float[] array) throws IOException {
        out.writeShort(array.length);

        for (float value : array)
            out.writeFloat(value);
    }

    public static float[] readFloatArray(DataInputStream in) throws IOException {
        float[] array = new float[in.readUnsignedShort()];

        for (int i = 0; i < array.length; i++)
            array[i] = in.readFloat();

        return array;
    }

    public static void writeArray255(DataOutputStream out, float[] array) throws IOException {
        out.write(array.length);

        for (float value : array)
            out.writeFloat(value);
    }

    public static float[] readArray255(DataInputStream in) throws IOException {
        float[] array = new float[in.read()];

        for (int i = 0; i < array.length; i++)
            array[i] = in.readFloat();

        return array;
    }
}