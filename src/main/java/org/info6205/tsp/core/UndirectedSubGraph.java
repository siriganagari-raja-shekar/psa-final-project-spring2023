package org.info6205.tsp.core;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UndirectedSubGraph extends UndirectedGraph{

    public UndirectedSubGraph(Set vertices, Graph graph){
        this.edges = graph
                .getAllEdges()
                .stream()
                .filter(e -> vertices.contains(e.getSource()) && vertices.contains(e.getDestination())).collect(Collectors.toSet());
        this.vertices = new HashSet(vertices);
    }

    private UndirectedSubGraph() {}

}
