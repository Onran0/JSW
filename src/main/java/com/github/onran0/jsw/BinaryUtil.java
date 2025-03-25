package com.github.onran0.jsw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BinaryUtil {

    public static <T extends ISerializable> void writeArray(DataOutputStream out, T[] array) throws IOException {
        out.writeShort(array.length);

        for (T value : array)
            value.write(out);
    }

    public static <T extends ISerializable> void writeArray255(DataOutputStream out, T[] array) throws IOException {
        out.write(array.length);

        for (T value : array)
            value.write(out);
    }

    public static <T extends ISerializable> T[] readArray(
            DataInputStream in,
            Function<Integer, T[]> arrCreator,
            Supplier<T> objCreator
    ) throws IOException {
        T[] array = arrCreator.apply(in.readUnsignedShort());

        for (int i = 0; i < array.length; i++)
            (array[i] = objCreator.get()).read(in);

        return array;
    }

    public static <T extends ISerializable> T[] readArray255(
            DataInputStream in,
            Function<Integer, T[]> arrCreator,
            Supplier<T> objCreator
    ) throws IOException {
        T[] array = arrCreator.apply(in.readUnsignedByte());

        for (int i = 0; i < array.length; i++)
            (array[i] = objCreator.get()).read(in);

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
}