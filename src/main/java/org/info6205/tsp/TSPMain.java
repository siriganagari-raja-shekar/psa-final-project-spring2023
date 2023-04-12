package org.info6205.tsp;

import org.info6205.tsp.algorithm.*;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;

import java.util.ArrayList;
import java.util.HashSet;
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
        Graph graph = null;
        try {
            graph = preprocess.start("crimeSample.csv");
            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);

            List<Vertex> bestTourYet = null;
            double bestCostYet = Double.MAX_VALUE;
            for (int i = 0; i < 1; i++) {

                List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();


                ThreeOptSwapOptimization threeOptSwapOptimization = new ThreeOptSwapOptimization(tspTour);
                List<Vertex> optimizedThreeOptTour = threeOptSwapOptimization.getOptimumTour();


                double threeOptTourCost = GraphUtil.getTotalCostOfTour(optimizedThreeOptTour);
                System.out.println("Three opt tour cost: " + threeOptTourCost);


                if(bestCostYet > GraphUtil.getTotalCostOfTour(optimizedThreeOptTour)){
                    bestTourYet = optimizedThreeOptTour;
                    bestCostYet = GraphUtil.getTotalCostOfTour(optimizedThreeOptTour);
                }
            }

            for(Vertex v: bestTourYet) System.out.print(v+"-->");
            System.out.println();
            System.out.println("Total cost of tour: " + bestCostYet);
            System.out.println(new HashSet<Vertex>(bestTourYet).size());

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
