package com.github.onran0.jsw.io;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;

public interface ISerializable {

    void read(LittleEndianDataInputStream in, int version) throws IOException;

    void write(LittleEndianDataOutputStream out, int version) throws IOException;
}