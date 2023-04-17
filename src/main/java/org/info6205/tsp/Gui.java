package org.info6205.tsp;

import org.info6205.tsp.core.Vertex;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class Gui extends Canvas {
    Overlay overlay;
    public Gui() {
        JFrame jFrame = new JFrame("TSP Plot:");
        overlay = new Overlay();
        overlay.getSize();
        jFrame.add(overlay);
        setJFrameSettings(jFrame);
    }

    public void drawGraph(List<Vertex> tour) {
        // x-h)2 + y-k)2 = r2
//        int offset = 100;
//        for (int i = 0; i < tour.size(); i++) {
//            Vertex vertex = tour.get(i);
//            addNode(vertex.getId(), i + offset, getYValue(i), 10, 10);
//            offset += 5;
//        }
//        for (int i = 0; i < 10; i++) {
//            Vertex vertex = tour.get(i);
//            addNode(vertex.getId(), i, i, 10, 10);
//        }
//        for (int i = -200; i < 201; i++) {
//            addNode(1, Math.abs(400 + i), 400 - (int) Math.sqrt(400*400 - (i-400)*(i-400)), 10, 10);
//        }

        int r = 100;
        int xOffset = 300;
        int yOffset = 300;
        for (int i = 0; i < 584; i += 1) {
//            for (int j = 0; j < 10; j += 1) {
                addNode(1, 1, 1, 10, 10);
//                int y = (int) Math.sqrt(r*r - i*i);
//                addNode(1, r + i, y, 10, 10);
//                y = (int) Math.sqrt(4*r*r - i*i);
//                addNode(1, 2*r + i, y, 10, 10);
//            }
//            int x = i;
//            int y = (int) Math.sqrt(r*r - x*x);
//            addNode(1, xOffset + x, y + yOffset, 10, 10);
        }

//        for (int i = 100; i >= 0; i-=20) {
//            int x = i;
//            int y = (int) Math.sqrt(r*r - x*x);
//            addNode(1, xOffset - x, y + yOffset, 10, 10);
//        }
//
//        for (int i = 0; i < 100; i+=20) {
//            int x = i;
//            int y = (int) Math.sqrt(r*r - x*x);
//            addNode(1, xOffset + x, yOffset - y, 10, 10);
//        }
//
//        for (int i = 100; i >= 0; i-=20) {
//            int x = i;
//            int y = (int) Math.sqrt(r*r - x*x);
//            addNode(1, xOffset - x, yOffset - y, 10, 10);
//        }
    }

    private int getYValue(int x) {
        int h = 50;
        int k = 50;
        int r = 100;
//        return (int) Math.sqrt(Math.pow(r, 2) - Math.pow((x - h), 2)) + k;
        return (int) Math.sqrt(r*r - x*x);
    }

    private void addNode(long id, int x, int y, int width, int height) {
        Dimension dimension = overlay.getSize();
        System.out.println(dimension);
        Random randomX = new Random();
        Random randomY = new Random();
        x = randomX.nextInt((int) dimension.getWidth());
        y = randomY.nextInt((int) dimension.getHeight());
        System.out.println("X, Y: " + x + " " + y);
        overlay.addNode(id, x, y, width, height);
    }

    private void setJFrameSettings(JFrame jFrame) {
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setVisible(true);
    }
}
