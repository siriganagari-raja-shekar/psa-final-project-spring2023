package org.info6205.tsp.core;


import java.util.Set;

public interface Graph<T extends Comparable<T>> {


    public boolean addVertex(T t) throws Exception;

    public boolean removeVertex(T t) throws Exception;

    public Set<T> getAllVertices();

    public boolean addEdge(T sourceVertex, T destinationVertex, double cost) throws Exception;

    public boolean removeAllEdgesBetweenVertices(T sourceVertex, T destinationVertex) throws Exception;

    public Set<Edge> getAllAdjacentEdgesOfVertex(T t) throws Exception;

    public Set<Edge> getEdgesBetweenVertices(T sourceVertex, T destinationVertex) throws Exception;

    public Set<Edge> getAllEdges();
}
