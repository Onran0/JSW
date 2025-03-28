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

public class BadAppleVideoPlayer {

    @Test
    public void main() throws Exception {
        for (File f : new File("bad_apple").listFiles()) {
            System.out.println(f.getName());
        }

        Structure structure = new Structure();

        Root root = new Root();

        structure.getRoots().add(root);

        structure.write("Bad Apple Video Player.structure");
    }
}