package com.github.onran0.jsw.block;

import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.util.StringUtils;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class MathBlockMetadata implements ISerializable {
    public String formula;
    public final Map<Integer, Integer> connectionsSlots = new HashMap<>();

    private void checkFormula() throws IOException {
        if(!StringUtils.isASCIIString(formula))
            throw new IOException("Invalid math block formula charset");
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        connectionsSlots.clear();

        formula = in.readUTF();

        checkFormula();

        int size = in.read();

        byte[] key = new byte[size];

        byte[] value = new byte[size];

        in.read(key);

        in.read();

        in.read(value);

        for (int i = 0; i < size; i++)
            connectionsSlots.put(key[i] & 0xFF, value[i] & 0xFF);

        for(int i = 0;i < 256;i++) {
            if(!connectionsSlots.containsKey(i))
                connectionsSlots.put(i, i);
        }
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        checkFormula();

        out.writeUTF(formula == null ? "" : formula);

        out.write(connectionsSlots.size());

        for (Integer connectionId : connectionsSlots.keySet())
            out.write(connectionId);

        out.write(connectionsSlots.size());

        for (Integer slot : connectionsSlots.values())
            out.write(slot);
    }
}