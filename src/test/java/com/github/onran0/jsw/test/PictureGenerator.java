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

public class PictureGenerator {

    @Test
    public void main() throws Exception {
        BufferedImage image = ImageIO.read(new File("picture.png"));

        int w = image.getWidth();
        int h = image.getHeight();

        Structure structure = new Structure();

        Root root = new Root();

        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                Block block = new Block();

                block.setId(Blocks.METAL_BLOCK);

                block.getPosition().setX(x);
                block.getPosition().setY(y);

                Color color = new Color();

                color.setRgba(image.getRGB(x, h-y));

                block.setColor(color);

                root.getBlocks().add(block);
            }
        }

        structure.getRoots().add(root);

        structure.write("picture.structure");
    }
}