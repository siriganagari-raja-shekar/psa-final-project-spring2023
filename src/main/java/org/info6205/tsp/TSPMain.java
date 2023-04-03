package org.info6205.tsp;

import org.info6205.tsp.algorithm.*;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;

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
        List<String> lines = preprocess.start("crimeSample.csv");

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

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);

            List<Vertex> bestTourYet = null;
            double bestCostYet = Double.MAX_VALUE;
            for (int i = 0; i < 1000; i++) {

                List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();

                TwoOptSwapOptimization twoOptSwapOptimization = new TwoOptSwapOptimization(tspTour);
                List<Vertex> optimizedTour = twoOptSwapOptimization.getOptimumTour(1);

                double currentCost = GraphUtil.getTotalCostOfTour(optimizedTour);
                if(currentCost < bestCostYet){
                    bestTourYet = tspTour;
                    bestCostYet = currentCost;
                }
            }

            for(Vertex v: bestTourYet) System.out.print(v+"-->");
            System.out.println();
            System.out.println("Total cost of tour: " + bestCostYet);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
        System.out.println("Running time: " + (endTime-startTime)/Math.pow(10,9));
    }

    public static double calculateEuclidianDistance(Vertex a, Vertex b){
        return Math.sqrt(Math.pow(a.getXPos()-b.getXPos(),2) + Math.pow(a.getYPos()-b.getYPos(),2));
    }
}
