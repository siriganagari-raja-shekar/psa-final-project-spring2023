package org.info6205.tsp.optimizations;

import org.info6205.tsp.algorithm.GreedyPerfectMatching;
import org.info6205.tsp.algorithm.HierholzerEulerianCircuit;
import org.info6205.tsp.algorithm.MinimumSpanningTree;
import org.info6205.tsp.algorithm.TwoOptSwapOptimization;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.io.Preprocess;
import org.info6205.tsp.util.GraphUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulatedAnnealingTest {

    @Test
    public void testSAOptimization() {
        Preprocess preprocess = new Preprocess();
        List<String> lines = preprocess.start("crimeSample.csv");

        Graph graph = new UndirectedGraph();

        try {
            for (String line : lines) {
                String[] lineSplit = line.split(",");
                graph.addVertex(new Vertex(Long.parseLong(lineSplit[0]), Double.parseDouble(lineSplit[1]), Double.parseDouble(lineSplit[2])));
            }

            List<Vertex> vertexList = new ArrayList<>(graph.getAllVertices());

            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = i + 1; j < vertexList.size(); j++) {
                    Vertex a = vertexList.get(i);
                    Vertex b = vertexList.get(j);
                    graph.addEdge(a, b);
                }
            }

            Graph minimumSpanningTree = new MinimumSpanningTree(graph).getMinimumSpanningTree();
            Graph subGraph = new UndirectedSubGraph(minimumSpanningTree.getOddDegreeVertices(), graph);
            Graph minimumCostPerfectMatching = new GreedyPerfectMatching(subGraph).getPerfectMatching();
            minimumSpanningTree.addExistingEdgesToGraph(new ArrayList<>(minimumCostPerfectMatching.getAllEdges()));
            HierholzerEulerianCircuit hierholzerEulerianCircuit = new HierholzerEulerianCircuit(minimumSpanningTree);
            List<Vertex> circuit = hierholzerEulerianCircuit.getEulerianCircuit();
            for (Vertex v : circuit) System.out.print(v + " -- >");
            System.out.println("\n" + GraphUtil.getTotalCostOfTour(circuit));
            List<Vertex> TSPTour = GraphUtil.getTSPTour(circuit);
            System.out.println("Cost without any optimization:\n"+GraphUtil.getTotalCostOfTour(TSPTour));
            List<Vertex> simulatedTour = new SimulatedAnnealing(TSPTour, 100000, 100000, 0.85).optimize();
            for (Vertex v : simulatedTour) System.out.print(v + " -- >");
            System.out.println("\nCost after SA:\n"+GraphUtil.getTotalCostOfTour(simulatedTour));

            List<Vertex> twoOptTour = new TwoOptSwapOptimization(TSPTour).getOptimumTour(1000);
            for (Vertex v : simulatedTour) System.out.print(v + " -- >");
            System.out.println("\nCost after Two Opt:\n"+GraphUtil.getTotalCostOfTour(twoOptTour));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
