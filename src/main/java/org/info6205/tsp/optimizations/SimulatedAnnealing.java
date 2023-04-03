package org.info6205.tsp.optimizations;

import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.util.GraphUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulatedAnnealing {
    private int iterations;
    private double temp;
    private List<Vertex> tour;
    private double gamma;

    public SimulatedAnnealing(List<Vertex> tour, int iterations, double temp, double gamma) {
        this.tour = tour;
        this.iterations = iterations;
        this.temp = temp;
        this.gamma = gamma;
    }

    public List<Vertex> optimize() {
        double currentCost = GraphUtil.getTotalCostOfTour(tour);
        double newCost = currentCost;
        for (int it = 0; it < iterations; it++) {
            int vertexCount = tour.size();
            Random rand = new Random();
            int i = rand.nextInt(vertexCount - 1);
            int j = rand.nextInt(vertexCount - 1);
            while (i == j) {
                j = rand.nextInt(vertexCount - 1);
            }
            Vertex v1 = tour.get(i);
            Vertex v2 = tour.get(i + 1);
            Vertex v3 = tour.get(j);
            Vertex v4 = tour.get(j + 1);
            double costDelta = -GraphUtil.getDistanceBetweenVertices(v1, v2) - GraphUtil.getDistanceBetweenVertices(v3, v4)
                    + GraphUtil.getDistanceBetweenVertices(v1, v3) + GraphUtil.getDistanceBetweenVertices(v2, v4);
            newCost += costDelta;
            if (costDelta < -1) {
                currentCost = newCost;
                doTwoOptSwap(tour, i, j);
            } else {
                if (shouldAccept(newCost, currentCost)) {
                    currentCost = newCost;
                    doTwoOptSwap(tour, i, j);
                }
            }
            coolTemp();
        }
        return tour;
    }

    private void coolTemp() {
        this.temp = temp * gamma;
    }

    private boolean shouldAccept(double newCost, double currentCost) {
        double prob = Math.exp(-(newCost - currentCost) / this.temp);
        Random rand = new Random();
        if (newCost < currentCost) {
            //accept new solution
            return true;
        } else {
            //System.out.println(rand.nextDouble()+"\t"+prob);
            if (rand.nextDouble() < prob) {
                //accept new solution
                return true;
            }
        }
        return false;
    }

    private void doTwoOptSwap(List<Vertex> vertices, int i, int j) {
        List<Vertex> temp = new ArrayList<>();
        for (int k = j; k > i; k--) {
            temp.add(vertices.get(k));
        }
        int c = 0;
        for (int k = i + 1; k <= j; k++) {
            vertices.set(k, temp.get(c));
            c++;
        }
    }
}
