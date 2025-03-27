package com.github.onran0.jsw.test;

import com.github.onran0.jsw.Color;
import com.github.onran0.jsw.Structure;
import com.github.onran0.jsw.block.Block;
import com.github.onran0.jsw.block.Blocks;
import com.github.onran0.jsw.block.MathBlockMetadata;
import com.github.onran0.jsw.block.Root;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

public class PictureGenerator {

    @Test
    public void main() throws Exception {
        BufferedImage image = ImageIO.read(new File("picture.png"));

        Structure structure = new Structure();

        Root root = new Root();

        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                Block block = new Block();

                block.setId(Blocks.METAL_BLOCK);

                block.position.x = x;
                block.position.y = y;

                Color color = new Color();

                color.setRgba(image.getRGB(x, y));

                block.setColor(color);

                root.getBlocks().add(block);
            }
        }

        structure.getRoots().add(root);

        structure.write("picture.structure");
    }
}