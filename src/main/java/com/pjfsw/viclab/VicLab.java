package com.pjfsw.viclab;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class VicLab {
    public static void main(String[] args) {
        try {
            VicLabFrame viclabFrame = new VicLabFrame();
            viclabFrame.pack();
            viclabFrame.setVisible(true);
            BufferedImage image = ImageIO.read(new File(args[0]));
            BufferedImage c64Image = MultiColorProcessor.process(image);
            viclabFrame.setImage(c64Image, 1.6);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
