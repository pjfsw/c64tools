package com.pjfsw.viclab;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.google.common.collect.ImmutableList;

public class MultiColorProcessor {

    private static int contrast(int c) {
        int contrast = 200;
        int adjustment = 200;
        c = c * contrast / 100 - adjustment;
        if (c < 0) {
            c = 0;
        }
        if (c > 255) {
            c = 255;
        }
        return c;
    }

    private static int applyContrast(int rgb) {
        int r = contrast(Palette.getRed(rgb));
        int g = contrast(Palette.getGreen(rgb));
        int b = contrast(Palette.getBlue(rgb));
        return new Color(r,g,b).getRGB();
    }

    public static BufferedImage process(BufferedImage image) {
        BufferedImage target = new BufferedImage(160,200,BufferedImage.TYPE_INT_ARGB);
        int w = image.getWidth()/1;
        int h = image.getHeight()/1;
        int ox = 0;//image.getWidth()/1;
        int oy = 130;//image.getHeight()/6;

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
                int rgb = applyContrast(target.getRGB(x,y));
                target.setRGB(x,y,
                    Palette.getNearestColor(
                        rgb,
                        ImmutableList.of(0,1,11,15)).getRGB());
            }
        }
        return target;
    }
}
