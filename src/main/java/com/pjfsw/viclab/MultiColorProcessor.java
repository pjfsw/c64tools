package com.pjfsw.viclab;

import java.awt.image.BufferedImage;

public class MultiColorProcessor {

    public static BufferedImage process(BufferedImage image) {
        BufferedImage target = new BufferedImage(160,200,BufferedImage.TYPE_INT_ARGB);
        int w = image.getWidth()/2;
        int h = image.getHeight()/2;
        int ox = image.getWidth()/3;
        int oy = image.getHeight()/6;

        if (w * 200 / 320 <= image.getHeight()) {
            h = w * 200 / 320;
        } else if (h * 320 / 200 <= image.getWidth()) {
            w = h * 320 / 200;
        } else {
            w = 2;
        }
        target.getGraphics().drawImage(image,
            0, 0, 160,200,
            0+ox,0+oy, w+ox,h+oy,
            null);
        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                target.setRGB(x,y, Palette.getNearestColor(target.getRGB(x,y)).getRGB());
            }
        }
        return target;
    }
}
