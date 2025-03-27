package com.github.onran0.jsw.util;

import com.github.onran0.jsw.io.ISerializable;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BinaryUtil {

    public static int toByte(boolean[] bools) {
        int b = 0;

        for (int i = 0; i < bools.length; i++)
            b |= (bools[i] ? 1 : 0) << i;

        return b;
    }

    public static boolean[] toBools(int b) {
        boolean[] bools = new boolean[8];

        for (int i = 0; i < bools.length; i++)
            bools[i] = (b & (1 << i)) != 0;

        return bools;
    }

    public static void writePackedBools255(LittleEndianDataOutputStream out, boolean[] bools) throws IOException {
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

    public static boolean[] readPackedBools255(LittleEndianDataInputStream in) throws IOException {
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

    public static <T extends ISerializable> void writeArray(LittleEndianDataOutputStream out, T[] array, int version) throws IOException {
        out.writeShort(array.length);

        for (T value : array)
            value.write(out, version);
    }

    public static <T extends ISerializable> void writeArray255(LittleEndianDataOutputStream out, T[] array, int version) throws IOException {
        out.write(array.length);

        for (T value : array)
            value.write(out, version);
    }

    public static <T extends ISerializable> T[] readArray(
            LittleEndianDataInputStream in,
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
            LittleEndianDataInputStream in,
            Function<Integer, T[]> arrCreator,
            Supplier<T> objCreator,
            int version
    ) throws IOException {
        T[] array = arrCreator.apply(in.readUnsignedByte());

        for (int i = 0; i < array.length; i++)
            (array[i] = objCreator.get()).read(in, version);

        return array;
    }

    public static void writeArray(LittleEndianDataOutputStream out, float[] array) throws IOException {
        out.writeShort(array.length);

        for (float value : array)
            out.writeFloat(value);
    }

    public static float[] readFloatArray(LittleEndianDataInputStream in) throws IOException {
        float[] array = new float[in.readUnsignedShort()];

        for (int i = 0; i < array.length; i++)
            array[i] = in.readFloat();

        return array;
    }

    public static void writeArray255(LittleEndianDataOutputStream out, float[] array) throws IOException {
        out.write(array.length);

        for (float value : array)
            out.writeFloat(value);
    }

    public static float[] readFloatArray255(LittleEndianDataInputStream in) throws IOException {
        float[] array = new float[in.read()];

        for (int i = 0; i < array.length; i++)
            array[i] = in.readFloat();

        return array;
    }

    public static void writeArray255(LittleEndianDataOutputStream out, int[] array) throws IOException {
        out.write(array.length);

        for (int value : array)
            out.writeInt(value);
    }

    public static int[] readIntArray255(LittleEndianDataInputStream in) throws IOException {
        int[] array = new int[in.read()];

        for (int i = 0; i < array.length; i++)
            array[i] = in.readInt();

        return array;
    }
}