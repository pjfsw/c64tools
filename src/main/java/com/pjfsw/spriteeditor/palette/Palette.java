package com.pjfsw.spriteeditor.palette;

import java.awt.Color;

public class Palette {
    private static final int[] palette = {
        0x000000,
        0xFFFFFF,
        0x68372B,
        0x70A4B2,
        0x6F3D86,
        0x588D43,
        0x352879,
        0xB8C76F,

        0x6F4F25,
        0x433900,
        0x9A6759,
        0x444444,
        0x6C6C6C,
        0x9AD2B4,
        0x6C5EB5,
        0x959595
    };

    private static final Color[] colors = {
        new Color(palette[0]),
        new Color(palette[1]),
        new Color(palette[2]),
        new Color(palette[3]),
        new Color(palette[4]),
        new Color(palette[5]),
        new Color(palette[6]),
        new Color(palette[7]),
        new Color(palette[8]),
        new Color(palette[9]),
        new Color(palette[10]),
        new Color(palette[11]),
        new Color(palette[12]),
        new Color(palette[13]),
        new Color(palette[14]),
        new Color(palette[15])
    };

    public static Color getColor(int index) {
        return colors[index & 15];
    }

    public static int getRgb(int index) {
        return palette[index & 15];
    }
}
