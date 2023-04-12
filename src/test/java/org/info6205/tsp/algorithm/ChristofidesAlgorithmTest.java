package org.info6205.tsp.algorithm;

import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class ChristofidesAlgorithmTest {

    static Graph testGraph;

    @BeforeAll
    public static void loadGraph(){

        try{
            Preprocess preprocess = new Preprocess();
            testGraph = preprocess.start("crimeSample.csv");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void checkTSPTourHasExactNumberOfVerticesAsOriginalGraphPlusOne(){

        try {
            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(testGraph);

            List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();

            Assertions.assertEquals(tspTour.size(), testGraph.getAllVertices().size()+1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void verifyTSPTourCostIsWithinRangeOfWorstCaseForChristofidesAlgorithm(){

        try {

            MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(testGraph);

            Graph mst = minimumSpanningTree.getMinimumSpanningTree();

            double mstCost = mst.getAllEdges().stream().mapToDouble(Edge::getWeight).reduce(0, (a,b)-> a+b)/2;

            ChristofidesAlgorithm christofidesAlgorithm = new ChristofidesAlgorithm(testGraph);

            List<Vertex> tspTour = christofidesAlgorithm.generateTSPTour();

            System.out.println((GraphUtil.getTotalCostOfTour(tspTour)-mstCost)/mstCost);

            Assertions.assertEquals( GraphUtil.getTotalCostOfTour(tspTour), mstCost * 1.5, Math.pow(10,6));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
