package org.info6205.tsp.core;

import java.util.*;
import java.util.stream.Collectors;

public class UndirectedSubGraph extends UndirectedGraph{

    public UndirectedSubGraph(Set<Vertex> vertices, Graph graph){
        this.graph = new HashMap<>();
        vertices.forEach(v -> this.graph.put(v,new ArrayList<>()));
        try{
            for (Vertex v : vertices) {
                for(Edge edge: graph.getAllAdjacentEdgesOfVertex(v)){
                    if(vertices.contains(edge.getDestination()))
                        this.graph.get(v).add(edge);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private UndirectedSubGraph() {}

}
