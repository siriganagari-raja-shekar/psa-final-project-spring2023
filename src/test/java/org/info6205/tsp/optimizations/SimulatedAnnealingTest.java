package org.info6205.tsp.optimizations;

import org.info6205.tsp.algorithm.*;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;

public class SimulatedAnnealingTest {

    @Test
    public void test3AOptimization() {
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();

        try {
            Graph graph = preprocess.start("crimeSample.csv");

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);

            List<Vertex> bestTourYet = null;
            double bestCostYet = Double.MAX_VALUE;
            for (int i = 0; i < 100; i++) {
                List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();
                ThreeOptSwapOptimization threeOptSwapOptimization = new ThreeOptSwapOptimization(tspTour);
                List<Vertex> optimizedThreeOptTour = threeOptSwapOptimization.getOptimumTour();
                double threeOptTourCost = GraphUtil.getTotalCostOfTour(optimizedThreeOptTour);
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
            Graph graph = preprocess.start("crimeSample.csv");
            double best= Double.MAX_VALUE;
            double avg= 0;
            final double RUNS=100;
            for(int s=0; s<RUNS; s++){
                ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);
                List<Vertex> tspTour= christofidesAlgorithm.generateTSPTour();
                System.out.println("TSP Tour s="+s);
                SimulatedAnnealing saOptimization = new SimulatedAnnealing(tspTour, 1000000, 650, 0.99);
                List<Vertex> saOptimizationTour= saOptimization.optimize();
                double saCost= GraphUtil.getTotalCostOfTour(saOptimizationTour);
                System.out.println("\tCost of SA Opt "+ saCost);
                avg+=GraphUtil.getTotalCostOfTour(saOptimizationTour);
                if( saCost < best){
                    best = GraphUtil.getTotalCostOfTour(saOptimizationTour);
                }
            }
            System.out.println("Best cost:"+ best);
            System.out.println("Avg cost:"+ avg/RUNS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
        System.out.println("Running time: " + (endTime-startTime)/Math.pow(10,9));
    }

    @Test
    public void competitionSAvs3A(){
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        long startTime = System.nanoTime();
        Preprocess preprocess = new Preprocess();

        try {
            int saWinCount= 0;
            int taWinCount= 0;
            double maxCostDiff= 0;
            Graph graph = preprocess.start("crimeSample.csv");
            final double RUNS=100;
            for(int s=0; s<RUNS; s++){
                ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(graph);
                List<Vertex> tspTour= christofidesAlgorithm.generateTSPTour();
                System.out.println("TSP Tour s="+s);
                SimulatedAnnealing saOptimization = new SimulatedAnnealing(tspTour, 1000000, 650, 0.99);
                List<Vertex> saOptimizationTour= saOptimization.optimize();
                double saCost= GraphUtil.getTotalCostOfTour(saOptimizationTour);

                ThreeOptSwapOptimization threeOptSwapOptimization= new ThreeOptSwapOptimization(tspTour);
                List<Vertex> taTour= threeOptSwapOptimization.getOptimumTour();
                double taCost= GraphUtil.getTotalCostOfTour(taTour);

                if(saCost < taCost){
                    System.out.println("SA Wins:"+ saCost+","+taCost);
                    saWinCount++;
                }
                else{
                    System.out.println("TO Wins:"+ saCost+","+taCost);
                    if((taCost-saCost) > maxCostDiff)
                        maxCostDiff = taCost-saCost;
                    taWinCount++;
                }
            }
            System.out.println("SA won: "+ saWinCount);
            System.out.println("ta won: "+ taWinCount);
            System.out.println("Max cost diff: "+maxCostDiff);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
        System.out.println("Running time: " + (endTime-startTime)/Math.pow(10,9));
    }
}
