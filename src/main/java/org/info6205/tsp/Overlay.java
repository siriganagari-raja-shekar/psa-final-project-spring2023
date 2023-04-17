package org.info6205.tsp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Overlay extends JPanel {

    java.util.List<NodeDimen> nodes = new ArrayList<>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawEdges(g);
        drawNodes(g);
    }

    private void drawEdges(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < nodes.size() - 1; i++) {
            NodeDimen node1 = nodes.get(i);
            NodeDimen node2 = nodes.get(i+1);
            graphics.drawLine(node1.x + node1.width/2, node1.y + node1.height/2, node2.x + node2.width/2, node2.y + node2.height/2);
        }
    }

    private void drawNodes(Graphics graphics) {
        graphics.setColor(Color.RED);
        for (int i = 0; i < nodes.size(); i++) {
            graphics.fillOval(nodes.get(i).x, nodes.get(i).y, nodes.get(i).width, nodes.get(i).height);
        }
    }

    public void addNode(long id, int x, int y, int width, int height) {
        nodes.add(new NodeDimen(id, x, y, width, height));
        repaint();
    }
}
