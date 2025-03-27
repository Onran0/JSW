package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Structure;

import java.io.DataInputStream;
import java.io.FileInputStream;

public class ReadTest {

    public static void main(String[] args) throws Exception {
        Structure structure = new Structure();

        DataInputStream in = new DataInputStream(new FileInputStream("TEST.structure"));

        structure.read(in);

        in.close();

        System.out.println("Readed successfully!");
    }
}