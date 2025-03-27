package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Structure;

import java.io.FileInputStream;

import com.github.onran0.jsw.block.Block;
import com.github.onran0.jsw.block.Blocks;
import com.github.onran0.jsw.block.MathBlockMetadata;
import com.github.onran0.jsw.block.Root;
import org.junit.Test;

public class ReadTest {

    @Test
    public void main() throws Exception {
        Structure structure = new Structure(new FileInputStream("TEST.structure"));

        for (Root root : structure.getRoots()) {
            for (Block block : root.getBlocks()) {
                if(block.getId() == Blocks.MATH_BLOCK) {
                    System.out.println( ((MathBlockMetadata)block.getMetadata().getCustomMetadata()).formula );
                }
            }
        }

        System.out.println("Readed successfully!");
    }
}