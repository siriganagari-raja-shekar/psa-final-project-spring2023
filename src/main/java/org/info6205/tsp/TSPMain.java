package org.info6205.tsp;

import org.info6205.tsp.algorithm.GreedyPerfectMatching;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class TSPMain
{
    public static void main( String[] args )
    {

        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));

        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();
        List<String> lines = preprocess.start("sample.csv");

        Graph graph = new UndirectedGraph();

        try {
            for(String line: lines){
                String[] lineSplit = line.split(",");
                graph.addVertex(new Vertex(Long.parseLong(lineSplit[0]), Double.parseDouble(lineSplit[1]), Double.parseDouble(lineSplit[2])));
            }

            List<Vertex> vertexList = new ArrayList<>(graph.getAllVertices());

            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = i+1; j < vertexList.size(); j++) {
                    Vertex a = vertexList.get(i);
                    Vertex b = vertexList.get(j);
                    graph.addEdge(a, b);
                }
            }
//
            Graph minimumCostPerfectMatching = GreedyPerfectMatching.getPerfectMatching(graph);
            System.out.println(minimumCostPerfectMatching);
            System.out.println("No. of vertices: " + minimumCostPerfectMatching.getAllVertices().size());
            System.out.println("No. of edges: " + minimumCostPerfectMatching.getAllEdges().size());
            System.out.println("Cost: "+minimumCostPerfectMatching.getAllEdges().stream().mapToDouble(e -> e.getWeight()).reduce(0.0, (a,b)->a+b)/2);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println((endTime-startTime)/Math.pow(10,9));
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));

    }

    public static double calculateEuclidianDistance(Vertex a, Vertex b){
        return Math.sqrt(Math.pow(a.getXPos()-b.getXPos(),2) + Math.pow(a.getYPos()-b.getYPos(),2));
    }
}
