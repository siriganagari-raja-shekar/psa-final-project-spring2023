package org.info6205.tsp.optimizations;

import org.info6205.tsp.algorithm.*;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SimulatedAnnealingTest {

    @Test
    public void testSAOptimization() {
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));

        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();

        try {
            Graph graph = preprocess.start("updatedCrimeSample750.csv");

            for(int s=0; s<1; s++){
                ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);
                double bestCost=Double.MAX_VALUE;
                List<Vertex> tspTour= christofidesAlgorithm.generateTSPTour();
                System.out.println("TSP Tour s="+s);
                ThreeOptSwapOptimization threeOptSwapOptimization= new ThreeOptSwapOptimization(tspTour);
                System.out.println("\tCost of 3 Opt "+ GraphUtil.getTotalCostOfTour(threeOptSwapOptimization.getOptimumTour()));
                System.out.println("\tMetrics for Simulated Annealing");
                for(int i=100; i<100000; i=i*2){
                    int bestTemp= Integer.MAX_VALUE;
                    for(int t=10; t< 10000; t= t*2){
                        for(double g=0.1; g<1; g+=0.1){
                            SimulatedAnnealing saOptimization = new SimulatedAnnealing(tspTour, i, t, g);
                            List<Vertex> saOptimizationTour= saOptimization.optimize();
                            if(bestCost > GraphUtil.getTotalCostOfTour(saOptimizationTour)){
                                bestCost = GraphUtil.getTotalCostOfTour(saOptimizationTour);
                                System.out.println("\t\tBest cost found for("+bestCost+"): "+i +", "+ t+", "+ g);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
        System.out.println("Running time: " + (endTime-startTime)/Math.pow(10,9));
    }

    @Test
    public void test3AOptimization() {
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));

        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();

        try {
            Graph graph = preprocess.start("updatedCrimeSample750.csv");

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);

            List<Vertex> bestTourYet = null;
            double bestCostYet = Double.MAX_VALUE;
            for (int i = 0; i < 100; i++) {
                List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();
                ThreeOptSwapOptimization threeOptSwapOptimization = new ThreeOptSwapOptimization(tspTour);
                List<Vertex> optimizedThreeOptTour = threeOptSwapOptimization.getOptimumTour();
                double threeOptTourCost = GraphUtil.getTotalCostOfTour(optimizedThreeOptTour);
                //System.out.println("Three opt tour cost: " + threeOptTourCost);
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

    @Test
    public void testWithBigDataSet(){
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();

        try {
            Graph graph = preprocess.start("updatedCrimeSample750.csv");
            double best= Double.MAX_VALUE;
            double avg= 0;
            for(int s=0; s<1; s++){
                ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);
                List<Vertex> tspTour= christofidesAlgorithm.generateTSPTour();
                System.out.println("TSP Tour s="+s);
//                ThreeOptSwapOptimization threeOptSwapOptimization= new ThreeOptSwapOptimization(tspTour);
//                System.out.println("\tCost of 3 Opt "+ GraphUtil.getTotalCostOfTour(threeOptSwapOptimization.getOptimumTour()));
                SimulatedAnnealing saOptimization = new SimulatedAnnealing(tspTour, 1000000, 1000, 0.99);
                List<Vertex> saOptimizationTour= saOptimization.optimize();
                double saCost= GraphUtil.getTotalCostOfTour(saOptimizationTour);
                System.out.println("\tCost of SA Opt "+ saCost);
                avg+=GraphUtil.getTotalCostOfTour(saOptimizationTour);
                if( saCost < best){
                    best = GraphUtil.getTotalCostOfTour(saOptimizationTour);
                }
            }
            System.out.println("Best cost:"+ best);
            System.out.println("Avg cost:"+ avg/20);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
        System.out.println("Running time: " + (endTime-startTime)/Math.pow(10,9));
    }

}
