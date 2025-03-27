package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Structure;
import com.github.onran0.jsw.block.Block;
import com.github.onran0.jsw.block.Blocks;
import com.github.onran0.jsw.block.Root;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class WriteTest {

    @Test
    public void main() throws Exception {
        Structure structure = new Structure();

        Root root = new Root();

        Block a = new Block();
        Block b = new Block();

        b.position.y++;

        root.getBlocks().add(a);
        root.getBlocks().add(b);

        structure.getRoots().add(root);

        structure.write("wt.structure");
    }
}