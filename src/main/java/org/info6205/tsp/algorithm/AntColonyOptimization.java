package org.info6205.tsp.algorithm;

import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.util.GraphUtil;
import java.util.*;

public class AntColonyOptimization {

    double[][] probabilityMatrix;
    double[][] distanceMatrix;
    double[][] rewardMartrix;
    Graph graph;
    List<Edge> edges;
    int length;
    List<Vertex> vertices;

    public AntColonyOptimization(Graph graph) {
        vertices = new ArrayList<>(graph.getAllVertices());
        length = vertices.size();
        distanceMatrix = new double[length][length];
        probabilityMatrix = new double[length][length];
        rewardMartrix = new double[length][length];
        this.edges = new ArrayList<>(graph.getAllEdges());
        this.graph = graph;
    }

    public List<Vertex> startOptimization() {
        initializeDistanceMatrix();
        initializeRewardMatrix();
        initializeProbabilityMatrix();

        double minTour = Double.MAX_VALUE;
        Random ran = new Random();
        List<Vertex> minCircuit = new ArrayList<>();
        for (int j = 0; j < 1000000; j++) {
            for (int i = 0; i < 1; i++) {
                List<Integer> tour = calculateAntColonyTour(ran.nextInt(156));
                List<Vertex> vert = new ArrayList<>();
                for (int v : tour) {
                    vert.add(vertices.get(v));
                }

                double tmpCost = GraphUtil.getTotalCostOfTour(vert);
                minTour = minTour > tmpCost ? tmpCost : minTour;
                minCircuit = vert;
            }
        }

        return minCircuit;
    }

    public void updateRewards(List<Integer> tour) {
        List<Vertex> circuit = new ArrayList<>();
        for (Integer n: tour) circuit.add(vertices.get(n));
        double tourCost = GraphUtil.getTotalCostOfTour(circuit);
        for (int i = 0; i < tour.size() - 1; i++) {
            int first = tour.get(i);
            int second = tour.get(i+1);
            rewardMartrix[first][second] += rewardMartrix[first][second]/tourCost;
            rewardMartrix[second][first] += rewardMartrix[first][second]/tourCost;
        }
    }

    private void recalculateProbabilityMatrix(List<Integer> unvisited, int source) {
        double totalInverseDistance = 0.0;
        for (int i: unvisited) {
            double inverseDistance = (1/distanceMatrix[source][i]) * rewardMartrix[source][i];
            totalInverseDistance += inverseDistance;
        }

        for (int i: unvisited) {
            probabilityMatrix[source][i] = (1/distanceMatrix[source][i])*rewardMartrix[source][i]/totalInverseDistance;
        }
    }

    private List<Integer> calculateAntColonyTour(int source) {
        List<Integer> res = new ArrayList<>();
        List<Integer> unvisited = new ArrayList<>();
        for (int i = 0; i < length; i++) if(i != source) unvisited.add(i);
        int iterNode = source;
        res.add(iterNode);
        Random random = new Random();
        while(unvisited.size() != 0) {
            double numberPicked = random.nextDouble();
            double probabilityRuler = 0.0;
            for (int i: unvisited) {
                probabilityRuler += probabilityMatrix[iterNode][i];
                if (numberPicked <= probabilityRuler) {
                    iterNode = i;
                    break;
                }
            }

            unvisited.remove(Integer.valueOf(iterNode));

            res.add(iterNode);
            recalculateProbabilityMatrix(unvisited, iterNode);
        }

        res.add(source);
        return res;
    }

    private void initializeProbabilityMatrix() {
        for (int i = 0; i < distanceMatrix.length; i++) {
            double totalInversetDistance = 0.0;
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                totalInversetDistance += (1/distanceMatrix[i][j])*rewardMartrix[i][j];
            }
            for (int j = 0; j < distanceMatrix[0].length; j++) {
                probabilityMatrix[i][j] = (1/distanceMatrix[i][j])*rewardMartrix[i][j]/totalInversetDistance;
            }
        }
    }

    private void initializeRewardMatrix() {
        for (int i = 0; i < rewardMartrix.length; i++)
            for (int j = 0; j < rewardMartrix[0].length; j++)
                rewardMartrix[i][j] = 1.0;
    }

    private void initializeDistanceMatrix() {
        for (Edge edge: edges) {
            Vertex source = edge.getSource();
            Vertex destination = edge.getDestination();
            double weight = edge.getWeight();
            if (weight == 0) weight = 1;
            distanceMatrix[(int) source.getId()][(int) destination.getId()] = weight;
            distanceMatrix[(int) destination.getId()][(int) source.getId()] = weight;
        }

        for (int i = 0; i < distanceMatrix.length; i++)
                distanceMatrix[i][i] = 1;
    }
}
