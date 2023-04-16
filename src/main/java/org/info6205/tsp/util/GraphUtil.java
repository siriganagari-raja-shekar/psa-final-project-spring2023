package org.info6205.tsp.util;

import org.apache.lucene.util.SloppyMath;
import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;

import java.util.*;

public class GraphUtil {

    public static double getTotalCostOfTour(List<Vertex> tour){
        double totalCost = 0;
        for (int i = 0; i < tour.size()-1; i++) {
            totalCost += getDistanceBetweenVertices(tour.get(i), tour.get(i+1));
        }
        return totalCost;
    }

    public static double getDistanceBetweenVertices(Vertex v1, Vertex v2){
        return SloppyMath.haversinMeters(v1.getXPos(), v1.getYPos(), v2.getXPos(), v2.getYPos());
    }

    public static List<Vertex> getTSPTour(List<Vertex> tour) {
        Map<Vertex, Integer> visited = new HashMap<>();
        List<Vertex> result = new ArrayList<>();
        for (Vertex vertex: tour) {
            if (visited.putIfAbsent(vertex, 1) == null) result.add(vertex);
        }

        result.add(result.get(0));
        return result;
    }

    public static List<Edge> removeDuplicateUndirectedEdgesFromMultigraph(Graph graph){

        List<Edge> edgeList = new ArrayList<>(graph.getAllEdges());
        edgeList.sort((a,b)-> {
            if(a.getSource() == b.getSource()){
                return Long.compare(a.getDestination().getId(), b.getDestination().getId());
            }
            else
                return Long.compare(a.getSource().getId(), b.getSource().getId());
        });
        Set<String> keysOfEdgesToRemove = new HashSet<>();
        Set<Edge> edgesToRemove = new HashSet<>();
        for(Edge edge: edgeList){
            if(keysOfEdgesToRemove.contains(edge.getSource().getId()+"s"+edge.getDestination().getId()+"d")){
                edgesToRemove.add(edge);
            }
            else{
                keysOfEdgesToRemove.add(edge.getDestination().getId()+"s"+edge.getSource().getId()+"d");
            }
        }
        edgeList.removeAll(edgesToRemove);
        return edgeList;
    }

    public static Graph generateGraphFromEulerianCircuit(List<Vertex> vertices) throws Exception {
        Graph graph= new UndirectedGraph();
        for(Vertex v: vertices){
            boolean isAlreadAdded= false;
            for(Vertex gv: graph.getAllVertices())
                if(v.getId() == gv.getId())
                    isAlreadAdded = true;

            if(!isAlreadAdded)
                graph.addVertex(v);
        }
        for(int i=0; i< vertices.size(); i++){
            if(i+1 <= vertices.size()-1){
                graph.addEdge(vertices.get(i), vertices.get(i+1));
            }
        }
        return graph;
    }
}
