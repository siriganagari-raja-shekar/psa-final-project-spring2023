package org.info6205.tsp.core;

public class Edge<T extends Comparable> implements Comparable<Edge>{

    private T source;

    private T destination;

    private double weight;

    public Edge(T source, T destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public T getSource() {
        return source;
    }

    public T getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
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
