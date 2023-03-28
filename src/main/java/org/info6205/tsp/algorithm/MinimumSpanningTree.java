package org.info6205.tsp.algorithm;

import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;

import java.util.*;

public class MinimumSpanningTree {
    /**
     * Boolean Map to keep track of visited nodes
     */
    private Map<Vertex, Boolean> visited;

    /**
     * Priority queue to get the edge with the minimum weight
     */
    private PriorityQueue<Edge> pq;

    /**
     * Undirected graph which is used to store the Minimum Spanning Tree
     */
    private Graph mst;

    /**
     * Method creates a minimum spanning tree from the provided undirected graph
     * @param graph Undirected graph of TSP
     * @param source Starting vertex in the MST
     * @return Returns the MST graph
     * @throws Exception
     */
    public Graph getMinimumSpanningTree(Graph graph, Vertex source) throws Exception {
        //Initialize the pq, mst and visited boolean map
        mst = new UndirectedGraph();
        pq= new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        visited = new HashMap<>();

        //Iterate over all the vertices in the graph and store them in visited hashmap as unvisited
        for(Vertex v: graph.getAllVertices()){
            visited.put(v, false);
            //Add all vertices in the MST
            mst.addVertex(v);
        }
        //Visit the initial vertex and add all adjacent vertices in the pq
        visit(graph, source);

        //Iterate over all the edges in the min heap and process all adjacent vertices in sequence
        while(!pq.isEmpty()){
            Edge e= pq.poll();
            Vertex v1= e.getSource();
            Vertex v2= e.getDestination();
            //continue if both vertices in the edge are already visited
            if(visited.get(v1) && visited.get(v2))
                continue;

            //Add the edge in the MST if vertices are not visited
            mst.addEdge(v1, v2, e.getWeight());

            //Visit the remaining vertex if not visited yet
            if(!visited.get(v1))
                visit(graph, v1);
            if(!visited.get(v2))
                visit(graph, v2);
        }
        return mst;
    }

    /**
     * Method to visit the vertex v and add it's adjacent vertices in the minimum priority queue
     * @param graph Graph of TSP
     * @param v vertex which should be visited
     * @throws Exception
     */
    private void visit(Graph graph, Vertex v) throws Exception {
        //Mark the vertex as visited
        visited.put(v, true);

        //Get and iterate over all adjacent edges connected to v
        for(Edge e: graph.getAllAdjacentEdgesOfVertex(v)){
            //Check if the v is source or destination of the edge, and
            //add unvisited vertex accordingly to the pq
            if(e.getSource() == v){
                if(!visited.get(e.getDestination()))
                    pq.add(e);
            }else{
                if(!visited.get(e.getSource()))
                    pq.add(e);
            }
        }
    }
}
