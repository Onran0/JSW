package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Structure;
import com.github.onran0.jsw.block.Block;
import com.github.onran0.jsw.block.Blocks;
import com.github.onran0.jsw.block.Root;
import org.junit.Test;

public class WriteTest {

    @Test
    public void main() throws Exception {
        Structure structure = new Structure();

        Root root = new Root();

        Block b = new Block(Blocks.FILTER);

        b.setName("мяу");

        root.getBlocks().add(b);

        structure.getRoots().add(root);

        structure.write("a.structure");
    }
}