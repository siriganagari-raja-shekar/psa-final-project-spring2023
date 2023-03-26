package org.info6205.tsp.core;

import java.util.*;
import java.util.stream.Collectors;

public class UndirectedGraph<T extends Comparable<T>> implements Graph<T>{

    Set<T> vertices;
    Set<Edge> edges;

    public UndirectedGraph(){
        vertices = new HashSet<>();
        edges = new HashSet<>();
    }

    @Override
    public boolean addVertex(T t) throws Exception{
        if(isVertexAlreadyPresent(t))
            throw new Exception("Vertex already present in graph");
        return vertices.add(t);
    }

    @Override
    public boolean removeVertex(T t) throws Exception{
        if(!isVertexAlreadyPresent(t))
            throw new Exception("Vertex not present in graph");

        edges = edges.stream().filter(e -> ((T)e.getSource()).compareTo(t) != 0 && ((T)e.getDestination()).compareTo(t) != 0).collect(Collectors.toSet());

        return vertices.remove(t);
    }

    @Override
    public Set<T> getAllVertices() {
        return vertices;
    }

    @Override
    public boolean addEdge(T sourceVertex, T destinationVertex, double cost) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        return edges.add(new Edge(sourceVertex, destinationVertex, cost));
    }

    @Override
    public boolean removeAllEdgesBetweenVertices(T sourceVertex, T destinationVertex) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        return edges.removeAll(getEdgesBetweenVertices(sourceVertex, destinationVertex));
    }

    @Override
    public Set<Edge> getAllAdjacentEdgesOfVertex(T t) throws Exception{
        if(!isVertexAlreadyPresent(t))
            throw new Exception("Vertex not present in graph");

        return edges.stream().filter(e -> ((T)e.getSource()).equals(t) || ((T)e.getDestination()).equals(t)).collect(Collectors.toSet());
    }

    @Override
    public Set<Edge> getEdgesBetweenVertices(T sourceVertex, T destinationVertex) throws Exception{

        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");

        return edges.stream()
                .filter(
                        e -> (((T)e.getSource()).equals(sourceVertex) && ((T)e.getDestination()).equals(destinationVertex))
                                || (((T)e.getDestination()).equals(sourceVertex) && ((T)e.getSource()).equals(destinationVertex))
                ).collect(Collectors.toSet());
    }

    @Override
    public Set<Edge> getAllEdges() {
        return edges;
    }

    private boolean isVertexAlreadyPresent(T vertex) {
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
