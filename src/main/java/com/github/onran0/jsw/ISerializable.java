package com.github.onran0.jsw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ISerializable {

    void read(DataInputStream in, int version) throws IOException;

    void write(DataOutputStream out, int version) throws IOException;

    void read(DataInputStream in) throws IOException;

    void write(DataOutputStream out) throws IOException;
}