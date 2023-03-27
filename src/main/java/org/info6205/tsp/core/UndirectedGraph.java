package org.info6205.tsp.core;

import java.util.*;
import java.util.stream.Collectors;

public class UndirectedGraph implements Graph{

    Set<Vertex> vertices;
    Set<Edge> edges;

    public UndirectedGraph(){
        vertices = new HashSet<>();
        edges = new HashSet<>();
    }

    @Override
    public boolean addVertex(Vertex vertex) throws Exception{
        if(isVertexAlreadyPresent(vertex))
            throw new Exception("Vertex already present in graph");
        return vertices.add(vertex);
    }

    @Override
    public boolean removeVertex(Vertex vertex) throws Exception{
        if(!isVertexAlreadyPresent(vertex))
            throw new Exception("Vertex not present in graph");

        edges = edges.stream().filter(e -> !e.getSource().equals(vertex) && !e.getDestination().equals(vertex)).collect(Collectors.toSet());

        return vertices.remove(vertex);
    }

    @Override
    public Set<Vertex> getAllVertices() {
        return vertices;
    }

    /**
     * Adds weighted edges to the graph. Weight is passed as a parameter
     *
     * @param sourceVertex Source vertex for the edge
     * @param destinationVertex Destination vertex for the edge
     * @param cost  Weight of the edge
     * @return Returns true when edges gets added successfully
     * @throws Exception Throws exception if source or destination vertices are not present
     */
    @Override
    public boolean addEdge(Vertex sourceVertex, Vertex destinationVertex, double cost) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        return edges.add(new Edge(sourceVertex, destinationVertex, cost));
    }

    /**
     * Adds weighted edges to the graph, weight is Euclidean distance between the source
     * and destination vertices which is calculated in Edge class
     *
     * @param sourceVertex Source vertex for the edge
     * @param destinationVertex Destination vertex for the edge
     * @return Returns true when edges gets added successfully
     * @throws Exception Throws exception if source or destination vertices are not present
     */
    @Override
    public boolean addEdge(Vertex sourceVertex, Vertex destinationVertex) throws Exception {
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        return edges.add(new Edge(sourceVertex, destinationVertex));
    }

    @Override
    public boolean removeAllEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        return edges.removeAll(getEdgesBetweenVertices(sourceVertex, destinationVertex));
    }

    @Override
    public Set<Edge> getAllAdjacentEdgesOfVertex(Vertex vertex) throws Exception{
        if(!isVertexAlreadyPresent(vertex))
            throw new Exception("Vertex not present in graph");

        return edges.stream().filter(e -> ((Vertex)e.getSource()).equals(vertex) || ((Vertex)e.getDestination()).equals(vertex)).collect(Collectors.toSet());
    }


    @Override
    public Set<Edge> getEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception{

        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");

        return edges.stream()
                .filter(
                        e -> ((e.getSource()).equals(sourceVertex) && (e.getDestination()).equals(destinationVertex))
                                || ((e.getDestination()).equals(sourceVertex) && (e.getSource()).equals(destinationVertex))
                ).collect(Collectors.toSet());
    }

    @Override
    public Set<Edge> getAllEdges() {
        return edges;
    }

    private boolean isVertexAlreadyPresent(Vertex vertex) {
        return vertices.contains(vertex);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Graph:\n");
        for(Edge edge: edges){
            sb.append(edge.getSource().toString()+"---"+ edge.getWeight()+"--->"+edge.getDestination().toString()+"\n");
        }

        return sb.toString();
    }
}
