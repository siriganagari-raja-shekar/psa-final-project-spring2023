package org.info6205.tsp.core;

import java.util.*;
import java.util.stream.Collectors;

public class UndirectedGraph implements Graph{

    HashMap<Vertex, List<Edge>> graph;
    public UndirectedGraph(){
        graph = new HashMap<>();
    }

    @Override
    public boolean addVertex(Vertex vertex) throws Exception{
        if(isVertexAlreadyPresent(vertex))
            throw new Exception(vertex + " already present in graph");
        return graph.put(vertex, new ArrayList<Edge>()) == null;
    }

    @Override
    public List<Edge> removeVertex(Vertex vertex) throws Exception{
        if(!isVertexAlreadyPresent(vertex))
            throw new Exception(vertex + " not present in graph");

        return graph.remove(vertex);
    }

    @Override
    public Set<Vertex> getAllVertices() {
        return graph.keySet();
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
    public void addEdge(Vertex sourceVertex, Vertex destinationVertex, double cost) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception(sourceVertex + " not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception(destinationVertex + " not present in graph");
        graph.get(sourceVertex).add(new Edge(sourceVertex, destinationVertex, cost));
        graph.get(destinationVertex).add(new Edge(destinationVertex, sourceVertex, cost));
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
    public void addEdge(Vertex sourceVertex, Vertex destinationVertex) throws Exception {
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception("sourceVertex not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception("destinationVertex not present in graph");
        graph.get(sourceVertex).add(new Edge(sourceVertex, destinationVertex));
        graph.get(destinationVertex).add(new Edge(destinationVertex, sourceVertex));
    }

    @Override
    public void removeAllEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception(sourceVertex + " not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception(destinationVertex + " not present in graph");

        graph.get(sourceVertex).removeAll(getEdgesBetweenVertices(sourceVertex, destinationVertex));
        graph.get(destinationVertex).removeAll(getEdgesBetweenVertices(destinationVertex, sourceVertex));
    }

    @Override
    public Set<Edge> getAllAdjacentEdgesOfVertex(Vertex vertex) throws Exception{
        if(!isVertexAlreadyPresent(vertex))
            throw new Exception(vertex + " not present in graph");

        return new HashSet<>(graph.get(vertex));
    }


    @Override
    public Set<Edge> getEdgesBetweenVertices(Vertex sourceVertex, Vertex destinationVertex) throws Exception{
        if(!isVertexAlreadyPresent(sourceVertex))
            throw new Exception(sourceVertex + " not present in graph");
        if(!isVertexAlreadyPresent(destinationVertex))
            throw new Exception(destinationVertex + " not present in graph");
        Set<Edge> edges = new HashSet<>();
        edges.addAll(graph.get(sourceVertex).stream().filter(e -> e.getDestination().equals(destinationVertex)).collect(Collectors.toSet()));
        edges.addAll(graph.get(destinationVertex).stream().filter(e -> e.getDestination().equals(sourceVertex)).collect(Collectors.toSet()));
        return edges;
    }

    @Override
    public Set<Edge> getAllEdges() {
        return graph.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private boolean isVertexAlreadyPresent(Vertex vertex) {
        return graph.containsKey(vertex) || graph.keySet().stream().anyMatch(v -> v.equals(vertex));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Graph:\n");
        for(Edge edge: getAllEdges()){
            sb.append(edge.getSource().toString()+"---"+ edge.getWeight()+"--->"+edge.getDestination().toString()+"\n");
        }

        return sb.toString();
    }
}
