package org.info6205.tsp.core;


import java.util.Set;

public interface Graph {


    public boolean addVertex(Vertex vertex) throws Exception;

    public boolean removeVertex(Vertex vertex) throws Exception;

    public Set<Vertex> getAllVertices();

    public boolean addEdge(Vertex sourceVertex, Vertex destinationVertex, double cost) throws Exception;

    public boolean addEdge(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public boolean removeAllEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public Set<Edge> getAllAdjacentEdgesOfVertex(Vertex vertex) throws Exception;

    public Set<Edge> getEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public Set<Edge> getAllEdges();
}
