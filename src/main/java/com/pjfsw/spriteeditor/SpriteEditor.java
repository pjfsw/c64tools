package com.pjfsw.spriteeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.pjfsw.spriteeditor.palette.Palette;

public class SpriteEditor {
    private static final int WAIT_PERIOD = 1_000_000_000 / 60;
    private static final int W = 1024;
    private static final int H = 720;
    private static final int MINIMUM_ZOOM = 2;
    private static final int MAXIMUM_ZOOM = 32;

    private final JFrame frame;
    private int zoom = 8;
    private boolean grid = true;

    private SpriteEditor() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        GraphicsConfiguration gc = gs[0].getConfigurations()[0];

        frame = new JFrame(gc);
        frame.setPreferredSize(new Dimension(W,H));
        frame.setTitle("Sprite Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
    }

    private static void waitNs(long ns) {
        if (ns > 0) {
            try {
                Thread.sleep(ns / 1000000, (int)(ns % 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean handleKeyEvent(KeyEvent event) {
        if (event.getID() != KeyEvent.KEY_PRESSED) {
            return false;
        }
        switch (event.getKeyChar()) {
            case '+':
                if (zoom < MAXIMUM_ZOOM) {
                    zoom++;
                }
                return true;
            case '-':
                if (zoom > MINIMUM_ZOOM) {
                    zoom--;
                }
                return true;
            case 'g':
                grid = !grid;
                return true;
            default:
                return false;
        }
    }


    public void run() {
        frame.setVisible(true);
        frame.createBufferStrategy(2);
        BufferStrategy strategy = frame.getBufferStrategy();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::handleKeyEvent);

        long ticks = System.nanoTime();
        while (true) {
            do {
                // preparation for rendering ?
                do {
                    Graphics graphics = strategy.getDrawGraphics();
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0,0,W,H);
                    graphics.translate(0, frame.getInsets().top);
                    draw(graphics);
                    graphics.dispose();
                } while (strategy.contentsRestored());
                strategy.show();
                long wait = WAIT_PERIOD - (int)(System.nanoTime() - ticks);
                ticks = System.nanoTime();
                waitNs(wait);
            } while (strategy.contentsLost());
        }
//        frame.setVisible(false);
//        frame.dispose();
    }

    private int getHeight() {
        return H-frame.getInsets().top-frame.getInsets().bottom;
    }

    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        int h = getHeight();
        for (int x = 0; x < 24; x++) {
            for (int y = 0; y < 21; y++) {
                g2.setColor(Palette.getColor(x+y*24));
                int pad = (grid && zoom > 4) ? 1 : 0;
                g2.fillRect(x*zoom,y*zoom, zoom-pad,zoom-pad);
            }
        }
    }

    public static void main(String[] args) {
        new SpriteEditor().run();
    }
}
