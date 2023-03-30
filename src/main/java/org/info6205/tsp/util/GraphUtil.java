package org.info6205.tsp.util;

import org.apache.lucene.util.SloppyMath;
import org.info6205.tsp.core.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphUtil {

    public static double getTotalCostOfTour(List<Vertex> tour){
        double totalCost = 0;
        for (int i = 0; i < tour.size()-1; i++) {
            totalCost += getDistanceBetweenVertices(tour.get(i), tour.get(i+1));
        }
        return totalCost;
    }

    public static double getDistanceBetweenVertices(Vertex v1, Vertex v2){
        return SloppyMath.haversinMeters(v1.getXPos(), v1.getYPos(), v2.getXPos(), v2.getYPos());
    }

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
