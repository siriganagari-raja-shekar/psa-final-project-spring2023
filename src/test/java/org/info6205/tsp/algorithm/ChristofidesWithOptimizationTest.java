package org.info6205.tsp.algorithm;


import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class ChristofidesWithOptimizationTest {

    static Graph testGraph;

    @BeforeAll
    public static void loadGraph(){

        try{
            Preprocess preprocess = new Preprocess();
            List<String> lines = preprocess.start("updatedCrimeSample.csv");

            testGraph = new UndirectedGraph();

            for (String line : lines) {
                String[] lineSplit = line.split(",");
                testGraph.addVertex(new Vertex(Long.parseLong(lineSplit[0]), Double.parseDouble(lineSplit[2]), Double.parseDouble(lineSplit[1])));
            }

            List<Vertex> vertexList = new ArrayList<>(testGraph.getAllVertices());

            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = i + 1; j < vertexList.size(); j++) {
                    Vertex a = vertexList.get(i);
                    Vertex b = vertexList.get(j);
                    testGraph.addEdge(a, b);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void CostAfterThreeOptSwapOptimizationWithinRangeOfMSTTest(){

        try {

            MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(testGraph);

            Graph mst = minimumSpanningTree.getMinimumSpanningTree();

            double mstCost = mst.getAllEdges().stream().mapToDouble(e -> e.getWeight()).reduce(0, (a,b)-> a+b)/2;

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(testGraph);

            List<Vertex> tour = christofidesAlgorithm.generateTSPTour();

            ThreeOptSwapOptimization threeOptSwapOptimization = new ThreeOptSwapOptimization(tour);

            List<Vertex> optimizedTour = threeOptSwapOptimization.getOptimumTour();

            //Checking if cost after optimization is not more than 25% of mst cost
            Assertions.assertEquals(mstCost, GraphUtil.getTotalCostOfTour(optimizedTour), mstCost/4);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void CostAfterTwoOptSwapOptimizationWithinRangeOfMSTTest(){

        try {

            MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(testGraph);

            Graph mst = minimumSpanningTree.getMinimumSpanningTree();

            double mstCost = mst.getAllEdges().stream().mapToDouble(e -> e.getWeight()).reduce(0, (a,b)-> a+b)/2;

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(testGraph);

            List<Vertex> tour = christofidesAlgorithm.generateTSPTour();

            TwoOptSwapOptimization threeOptSwapOptimization = new TwoOptSwapOptimization(tour);

            List<Vertex> optimizedTour = threeOptSwapOptimization.getOptimumTour();

            //Checking if cost after optimization is not more than 25% of mst cost
            Assertions.assertEquals(mstCost, GraphUtil.getTotalCostOfTour(optimizedTour), mstCost/4);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
