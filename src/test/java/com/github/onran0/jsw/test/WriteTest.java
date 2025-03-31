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

        Block in = new Block();

        in.setId(Blocks.AND);
        in.getPosition().setY(in.getPosition().getY() + 1);

        for(int i = 0;i < 150;i++) {
            Block block = new Block();

            block.setId(Blocks.HEAVY_GUN);

            in.connectOutput(block);

            root.getBlocks().add(block);
        }

        root.getBlocks().add(in);

        Block t = new Block();

        t.setId(Blocks.MONITOR);

        t.getPosition().setY(t.getPosition().getY() + 2);

        root.getBlocks().add(t);

        structure.getRoots().add(root);

        structure.write("a.structure");
    }
}