package com.pjfsw.viclab;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class VicLabFrame extends JFrame {
    private BufferedImage image;
    private double aspectRatio;

    VicLabFrame()  {
        super("Vic LAB");
        image = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        setPreferredSize(new Dimension(640,400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    void setImage(BufferedImage image, double aspectRatio) {
        this.image = image;
        this.aspectRatio = aspectRatio;
        image.setRGB(159,199, 0xFFFF00FF);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        int w = g.getClipBounds().width;
        int h = g.getClipBounds().height;

        g.setColor(Color.BLACK);
        g.fillRect(0,0,w,h);
        if ((double)w/h > aspectRatio) {
            w = (int)((double)h * aspectRatio);
        } else {
            h = (int)((double)w / aspectRatio);
        }
        g.drawImage(image, 0, 0, w, h, null);
    }
}
