package org.info6205.tsp.core;

public class Vertex{

    private long id;

    private double xPos;

    private double yPos;

    public Vertex(long id, double xPos, double yPos) {
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public long getId() {
        return id;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    @Override
    public boolean equals(Object o){
        Vertex v = (Vertex) o;
        return this.getId() == v.getId();
    }

    @Override
    public String toString() {
        return id+"";
    }
}
