package org.info6205.tsp.algorithm;

import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.util.GraphUtil;
import org.info6205.tsp.visualization.TSPVisualization;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChristofidesAlgorithmVisualization {
    /**
     * Initial input graph
     */
    Graph graph;

    /**
     * Parameterized constructor taking inital graph as input
     * @param graph
     */
    public ChristofidesAlgorithmVisualization(Graph graph) {
        this.graph = graph;
    }

    /**
     * Generates a new TSP tour (Not necessarily the same tour each time)
     * @return A TSP tour containing a list of vertices
     */
    public List<Vertex> generateTSPTour() throws Exception{

        // Creating minimum spanning tree algorithm class instance
        MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(graph);
        //Getting minimum spanning tree
        Graph mst = minimumSpanningTree.getMinimumSpanningTree();
        TSPVisualization viz= new TSPVisualization(mst, 1900, 1000);
        viz.visualizeMST();


        viz.hightlightOddDegreeVertices(mst.getOddDegreeVertices(), Color.CYAN, 10, 10);

        //Creating subgraph for greedy perfect matching input
        Graph subGraph = new UndirectedSubGraph(mst.getOddDegreeVertices(), graph);

        //Creating Greedy perfect matching class instance
        GreedyPerfectMatching greedyPerfectMatching = new GreedyPerfectMatching(subGraph);

        //Adding existing edges to minimum spanning tree to create multigraph
        Graph gpGraph= greedyPerfectMatching.getPerfectMatching();
        List<Edge> edgesFromPerfectMatching= GraphUtil.removeDuplicateUndirectedEdgesFromMultigraph(gpGraph);
        mst.addExistingEdgesToGraph(gpGraph.getAllEdges().stream().collect(Collectors.toList()));
        viz.convertGraphToMultiGraph();
        viz.generateMultiGraph();
        viz.addEdgesfromPerfectMatching(edgesFromPerfectMatching);
        viz.highlightEdges(edgesFromPerfectMatching, Color.CYAN, 2.0f);

        //Creating Hierholzer Eulerian Circuit class instance
        HierholzerEulerianCircuit hierholzerEulerianCircuit = new HierholzerEulerianCircuit(mst);

        //Getting a Eulerian Circuit
        List<Vertex> eulerianCircuit = hierholzerEulerianCircuit.getEulerianCircuit();
        Graph eulerianGraph= GraphUtil.generateGraphFromEulerianCircuit(hierholzerEulerianCircuit.getEulerianCircuit());
        List<Edge> eulerianEdges= GraphUtil.removeDuplicateUndirectedEdgesFromMultigraph(eulerianGraph);
        viz.highlightEdges(eulerianEdges, Color.MAGENTA, 2.0f);

        List<Vertex> tspTourVertices= GraphUtil.getTSPTour(eulerianCircuit);
        Graph tspGraph= GraphUtil.generateGraphFromEulerianCircuit(tspTourVertices);
        List<Edge> tspEdges= GraphUtil.removeDuplicateUndirectedEdgesFromMultigraph(tspGraph);
        viz.visualizeTSPTour(tspGraph);
        return tspTourVertices;
    }
}
