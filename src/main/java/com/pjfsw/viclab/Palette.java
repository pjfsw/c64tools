package com.pjfsw.viclab;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.ImmutableList;

public class Palette {
    public static int OPAQUE = 0xFF000000;
    private static final int rhBias = 34;
    private static final int gsBias = 33;
    private static final int bvBias = 33;

    private Palette() {
    }

    private static List<Color> colors = ImmutableList.of(
        new Color(0x00, 0x00, 0x00),
        new Color(0xff, 0xff, 0xff),
        new Color(0x68, 0x37, 0x2b),
        new Color(0x70, 0xA4, 0xB2),
        new Color(0x6F, 0x3D, 0x86),
        new Color(0x58, 0x8D, 0x43),
        new Color(0x35, 0x28, 0x79),
        new Color(0xB8, 0xC7, 0x6F),
        new Color(0x6F, 0x4F, 0x25),
        new Color(0x43, 0x39, 0x00),
        new Color(0x9A, 0x67, 0x59),
        new Color(0x44, 0x44, 0x44),
        new Color(0x6C, 0x6C, 0x6C),
        new Color(0x9A, 0xD2, 0x84),
        new Color(0x6C, 0x5E, 0xB5),
        new Color(0x95, 0x95, 0x95)
    );

    private static long getSquaredDistance(int v1, int v2) {
        int r = rhBias * (((v2 >> 16)&255)-((v1 >> 16)&255));
        int g = gsBias * (((v2 >> 8)&255)-((v1 >> 8)&255));
        int b = bvBias * ((v2&255)-(v1&255));
        return r*r + g*g + b*b;
    }

    private static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    private static int createHsv(double h, double s, double v) {
        return ((int)(h * 255.0) << 16) | ((int)(s * 255.0) << 16) | ((int)(v * 255.0));
    }

    /**
     * Convert rgb to hsv
     * @param rgb rgb value
     * @return
     */
    private static int rgbToHsv(int rgb)
    {
        double r = getRed(rgb)/255.0;
        double g = getGreen(rgb)/255.0;
        double b = getBlue(rgb)/255.0;

        double min = Math.min(r, Math.min(g,b));
        double max = Math.max(r, Math.max(g,b));

        double v = max;
        double s;
        double h;

        double delta = max - min;
        if (delta < 0.00001)
        {
            return 0;
        }
        if( max > 0.0 ) { // NOTE: if Max is == 0, this divide would cause a crash
            s = (delta / max);                  // s
        } else {
            // if max is 0, then r = g = b = 0
            // s = 0, h is undefined
            s = 0;
            h = 0;
            return createHsv(h,s,v);
        }
        if( r >= max ) {                          // > is bogus, just keeps compilor happy
            h = ( g - b ) / delta;        // between yellow & magenta
        } else if( g >= max ) {
            h = 2.0 + ( b - r ) / delta;  // between cyan & yellow
        } else {
            h = 4.0 + ( r - g ) / delta;  // between magenta & cyan
        }

        h *= 60.0;                              // degrees

        if( h < 0.0 )
            h += 360.0;

        return createHsv(h,s,v);
    }

    private static int getComparableValue(int rgb) {
        return rgb;
    }
    public static Color getNearestColor(int rgb) {
        int hsv = getComparableValue(rgb);
        long shortestDistance = Long.MAX_VALUE;
        Color colorToUse = colors.get(0);
        for (Color color : colors) {
            long distance = getSquaredDistance(hsv, getComparableValue(color.getRGB()));
            if (distance < shortestDistance) {
                colorToUse = color;
                shortestDistance = distance;
            }
        }
        return colorToUse;
    }

}
