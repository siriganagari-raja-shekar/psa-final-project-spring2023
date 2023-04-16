package org.info6205.tsp;

import org.info6205.tsp.algorithm.GreedyPerfectMatching;
import org.info6205.tsp.algorithm.HierholzerEulerianCircuit;
import org.info6205.tsp.algorithm.MinimumSpanningTree;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.UndirectedSubGraph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.util.GraphUtil;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class TSPTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testChristofides() throws Exception {
        Graph g= new UndirectedGraph();
        Vertex v1= new Vertex(1, 0, 0);
        Vertex v2= new Vertex(2,0, 0);
        Vertex v3= new Vertex(3, 0, 0);
        Vertex v4= new Vertex(4, 0, 0);
        Vertex v5= new Vertex(5, 0, 0);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);

        g.addEdge(v1, v2, 1);
        g.addEdge(v1, v3, 1);
        g.addEdge(v1, v4, 2);
        g.addEdge(v1, v5, 1);

        g.addEdge(v2, v3, 2);
        g.addEdge(v2, v4, 1);
        g.addEdge(v2, v5, 1);

        g.addEdge(v3, v4, 1);
        g.addEdge(v3, v5, 1);

        g.addEdge(v4, v5, 1);

        // Creating minimum spanning tree algorithm class instance
        MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(g);

        //Getting minimum spanning tree
        Graph mst = minimumSpanningTree.getMinimumSpanningTree();
        System.out.println("MST\n"+mst);

        //Creating subgraph for greedy perfect matching input
        Graph subGraph = new UndirectedSubGraph(mst.getOddDegreeVertices(), g);
        System.out.println("Subgraph with odd degree vertices\n"+subGraph);
        //Creating Greedy perfect matching class instance
        GreedyPerfectMatching greedyPerfectMatching = new GreedyPerfectMatching(subGraph);

        //Adding existing edges to minimum spanning tree to create multigraph
        mst.addExistingEdgesToGraph(greedyPerfectMatching.getPerfectMatching().getAllEdges().stream().collect(Collectors.toList()));
        System.out.println("MST after perfect matching\n"+mst);

        //Creating Hierholzer Eulerian Circuit class instance
        HierholzerEulerianCircuit hierholzerEulerianCircuit = new HierholzerEulerianCircuit(mst);

        //Getting a Eulerian Circuit
        List<Vertex> eulerianCircuit = hierholzerEulerianCircuit.getEulerianCircuit();
        System.out.println("Eulerian Circuit\n"+ GraphUtil.getTSPTour(eulerianCircuit));
    }
}
