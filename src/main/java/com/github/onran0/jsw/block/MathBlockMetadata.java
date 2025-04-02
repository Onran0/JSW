package com.github.onran0.jsw.block;

import com.github.onran0.jsw.io.ISerializable;
import com.github.onran0.jsw.util.StringUtils;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class MathBlockMetadata implements ISerializable {
    private StringBuilder formula = new StringBuilder();
    private final Map<Integer, Integer> connectionsSlots = new HashMap<>();

    public Map<Integer, Integer> getConnectionsSlots() {
        return connectionsSlots;
    }

    public StringBuilder getFormula() {
        return formula;
    }

    public void setFormula(StringBuilder formula) {
        if (formula == null)
            throw new NullPointerException("Argument formula is null");

        this.formula = formula;
    }

    public void setFormula(CharSequence formula) {
        this.formula = new StringBuilder(formula);
    }

    public void appendFormula(Object x) {
        this.formula.append(x);
    }

    @Override
    public void read(LittleEndianDataInputStream in, int version) throws IOException {
        getConnectionsSlots().clear();

        setFormula(new StringBuilder(StringUtils.readASCII(in, in.readUnsignedShort())));

        int size = in.read();

        byte[] key = new byte[size];

        byte[] value = new byte[size];

        in.read(key);

        in.read();

        in.read(value);

        for (int i = 0; i < size; i++)
            getConnectionsSlots().put(key[i] & 0xFF, value[i] & 0xFF);

        for(int i = 0;i < 256;i++) {
            if(!getConnectionsSlots().containsKey(i))
                getConnectionsSlots().put(i, i);
        }
    }

    @Override
    public void write(LittleEndianDataOutputStream out, int version) throws IOException {
        if(!StringUtils.isASCIIString(getFormula().toString()))
            throw new IOException("Invalid math block formula charset");

        out.writeShort((short) getFormula().length());

        StringUtils.writeASCII(out, getFormula().toString());

        out.write(getConnectionsSlots().size());

        for (Integer connectionId : getConnectionsSlots().keySet())
            out.write(connectionId);

        out.write(getConnectionsSlots().size());

        for (Integer slot : getConnectionsSlots().values())
            out.write(slot);
    }
}