package org.info6205.tsp.algorithm;

import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MinimumSpanningTreeTest {

    @Test
    public void testSpanningTree(){
        try {
            Graph g= new UndirectedGraph();
            Vertex v1 = new Vertex(1, 1.0, 1.0);
            g.addVertex(v1);
            Vertex v2= new Vertex(2, 2.0, 2.0);
            g.addVertex(v2);
            Vertex v3= new Vertex(3, 3.0, 3.0);
            g.addVertex(v3);
            Vertex v4= new Vertex(4, 4.0, 4.0);
            g.addVertex(v4);
            Vertex v5= new Vertex(5, 5.0, 5.0);
            g.addVertex(v5);

            g.addEdge(v1, v2, 1);
            g.addEdge(v1, v3, 7);
            g.addEdge(v2, v3, 5);
            g.addEdge(v2, v5, 3);
            g.addEdge(v2, v4, 4);
            g.addEdge(v3, v5, 6);
            g.addEdge(v4, v5, 2);

            MinimumSpanningTree mstclass= new MinimumSpanningTree();
            Graph mst= mstclass.getMinimumSpanningTree(g, v1);
            int noOfVertices= mst.getAllVertices().size();
            int noOfEdges= mst.getAllEdges().size();
            assertEquals("Checking number of edges are 8",8, noOfEdges);
            assertEquals("Checking if number of edges are equal to (number of vertices - 1) * 2", noOfEdges, (noOfVertices-1)*2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
