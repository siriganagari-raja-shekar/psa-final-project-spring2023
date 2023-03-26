package org.info6205.tsp.core;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UndirectedSubGraph<T extends Comparable<T>> extends UndirectedGraph{

    public UndirectedSubGraph(Set<T> vertices, Graph<T> graph){
        this.edges = graph
                .getAllEdges()
                .stream()
                .filter(e -> vertices.contains((T)e.getSource()) && vertices.contains((T)e.getDestination())).collect(Collectors.toSet());
        this.vertices = new HashSet(vertices);
    }

    private UndirectedSubGraph() {}

}
