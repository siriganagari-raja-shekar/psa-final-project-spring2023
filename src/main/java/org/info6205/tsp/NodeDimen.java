package org.info6205.tsp;

import org.info6205.tsp.core.Vertex;

public class NodeDimen extends Vertex {
    int x, y, width, height;
    NodeDimen(long id, int x, int y, int width, int height) {
        super(id, x, y);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}