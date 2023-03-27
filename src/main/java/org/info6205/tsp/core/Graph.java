package org.info6205.tsp.core;


import java.util.List;
import java.util.Set;

public interface Graph {


    public boolean addVertex(Vertex vertex) throws Exception;

    public List<Edge> removeVertex(Vertex vertex) throws Exception;

    public Set<Vertex> getAllVertices();

    public void addEdge(Vertex sourceVertex, Vertex destinationVertex, double cost) throws Exception;

    public void addEdge(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public void removeAllEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public Set<Edge> getAllAdjacentEdgesOfVertex(Vertex vertex) throws Exception;

    public Set<Edge> getEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception;

    public Set<Edge> getAllEdges();
}
