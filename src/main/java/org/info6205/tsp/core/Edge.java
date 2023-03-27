package org.info6205.tsp.core;

import org.apache.lucene.util.SloppyMath;

public class Edge implements Comparable<Edge>{

    private Vertex source;

    private Vertex destination;

    private double weight;

    public Edge(Vertex source, Vertex destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Edge(Vertex source, Vertex destination) {
        this.source = source;
        this.destination = destination;
        this.weight = calculateDistanceInMeters();
    }

    public Vertex getSource() {
        return source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Method to calculate Euclidean distance without overflow or underflow
     *
     * @return Euclidean distance between source and destination
     */
    private double calculateEuclideanDistance(){
        return Math.hypot(destination.getXPos()-source.getXPos(),destination.getYPos()-source.getYPos());
    }

    private double calculateDistanceInMeters(){
        return SloppyMath.haversinMeters(source.getXPos(), source.getYPos(), destination.getXPos(), destination.getYPos());
    }
    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.getWeight(), o.getWeight());
    }
}
