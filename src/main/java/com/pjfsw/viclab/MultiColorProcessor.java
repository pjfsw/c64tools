package com.pjfsw.viclab;

import static java.util.stream.Collectors.toList;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

public class MultiColorProcessor {

    private static int contrast(int c) {
        int contrast = 130;
        int adjustment = 50;
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
        int w = image.getWidth();
        int h = image.getHeight();
        int ox = 0;//image.getWidth()/1;
        int oy = 50;//image.getHeight()/6;

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

        CharacterColors[][] mostUsed = new CharacterColors[25][40];
        for (int y = 0; y < 25; y++) {
            for (int x = 0; x < 40; x++) {
                mostUsed[y][x] = new CharacterColors();
            }
        }

        List<Integer> initialPalette = ImmutableList.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        for (int y = 0; y < 200; y++) {
            for (int x = 0; x < 160; x++) {
                int rgb = applyContrast(target.getRGB(x,y));
                int colorIndex = Palette.getNearestColor(
                    rgb,initialPalette);
                mostUsed[y>>3][x>>2].put(colorIndex);
            }
        }
        Map<Integer,Integer> mostUsedOnScreen = new HashMap<>();

        for (int y = 0; y < 25; y++) {
            for (int x = 0; x < 40; x++) {
                List<Integer> colors = mostUsed[y][x].getSortedList();
                Integer colorIndex = colors.get(colors.size() - 1);
                mostUsedOnScreen.put(colorIndex, mostUsedOnScreen.getOrDefault(colorIndex, 0)+1);
            }
        }

        Integer background =
            mostUsedOnScreen.entrySet().stream()
                .max(Entry.comparingByValue())
                .map(Entry::getKey)
                .orElse(0);

        System.out.printf("Background index %d\n", background);

        for (int cy = 0; cy < 25; cy++) {
            for (int cx = 0; cx < 40; cx++) {
                List<Integer> allColors = mostUsed[cy][cx].getSortedList();
                List<Integer> colorsToUse = new ArrayList<>();

                colorsToUse.add(background);
                int idx = allColors.size()-1;
                while (colorsToUse.size() < 4 && idx >= 0) {
                    if (!allColors.get(idx).equals(background)) {
                        colorsToUse.add(allColors.get(idx));
                    }
                    idx--;
                }

                for (int suby = 0; suby < 8; suby++) {
                    for (int subx = 0; subx < 4; subx++) {
                        int x = cx * 4 + subx;
                        int y = cy * 8 + suby;
                        int rgb = applyContrast(target.getRGB(x,y));
                        /*target.setRGB(x,y,
                            Palette.getColor((cy+cx)%16).getRGB());*/

                        target.setRGB(x,y,
                            Palette.getColor(Palette.getNearestColor(
                                rgb,
                                colorsToUse)).getRGB());
                    }
                }
            }
        }
        return target;
    }

    private static class CharacterColors {
        private final Map<Integer,Integer> mostUsed = new HashMap<>();

        private void put(int rgb) {
            mostUsed.putIfAbsent(rgb, 0);
            mostUsed.put(rgb, mostUsed.get(rgb)+1);
        }

        private List<Integer> getSortedList() {
            return mostUsed.entrySet().stream().sorted((Entry.comparingByValue()))
                .map(Entry::getKey)
                .collect(toList());
        }
    }
}
