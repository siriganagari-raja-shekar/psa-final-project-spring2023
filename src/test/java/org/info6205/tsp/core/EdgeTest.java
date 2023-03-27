package org.info6205.tsp.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdgeTest {

    /**
     * Method to test the implementation of Euclidean distance between vertices
     */
    @Test
    public void testEuclideanDistance(){
        Vertex v1= new Vertex(1, 10.0, 20.0);
        Vertex v2= new Vertex(2, 11.0, 21.0);
        Edge edge= new Edge(v1, v2);
        assertEquals(edge.getWeight(), 1.4142135623730951, 0.1);
    }
}
