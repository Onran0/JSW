package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Structure;

import java.io.DataInputStream;
import java.io.FileInputStream;

import com.google.common.io.LittleEndianDataInputStream;
import org.junit.Test;

public class ReadTest {

    @Test
    public void main() throws Exception {
        Structure structure = new Structure();

        LittleEndianDataInputStream in = new LittleEndianDataInputStream(new FileInputStream("TEST.structure"));

        structure.read(in);

        in.close();



        System.out.println("Readed successfully!");
    }
}