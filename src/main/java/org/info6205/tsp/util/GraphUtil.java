package org.info6205.tsp.util;

import org.apache.lucene.util.SloppyMath;
import org.info6205.tsp.core.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for graphs
 */
public class GraphUtil {

    /**
     * Get total cost of tour from a list of vertices
     * @param tour Tour for which cost has to be found
     * @return total cost of the tour
     */
    public static double getTotalCostOfTour(List<Vertex> tour){
        double totalCost = 0;
        for (int i = 0; i < tour.size()-1; i++) {
            totalCost += getDistanceBetweenVertices(tour.get(i), tour.get(i+1));
        }
        return totalCost;
    }

    /**
     * Distance between vertices calculated using Haversine formula
     * @param v1 First vertex
     * @param v2 Second vertex
     * @return distance between vertices
     */
    public static double getDistanceBetweenVertices(Vertex v1, Vertex v2){
        return SloppyMath.haversinMeters(v1.getXPos(), v1.getYPos(), v2.getXPos(), v2.getYPos());
    }

    /**
     * Getting a TSP tour from an Euler tour
     * @param tour Euler tour
     * @return Final TSP tour after removing duplicates from original tour
     */
    public static List<Vertex> getTSPTour(List<Vertex> tour) {
        Map<Vertex, Integer> visited = new HashMap<>();
        List<Vertex> result = new ArrayList<>();
        for (Vertex vertex: tour) {
            if (visited.putIfAbsent(vertex, 1) == null) result.add(vertex);
        }

        result.add(result.get(0));
        return result;
    }

}
