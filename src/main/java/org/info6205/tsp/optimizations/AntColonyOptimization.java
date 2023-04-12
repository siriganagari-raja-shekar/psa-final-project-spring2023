package org.info6205.tsp.optimizations;

import org.info6205.tsp.core.Edge;
import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.Vertex;
import org.info6205.tsp.util.GraphUtil;
import java.util.*;
import java.util.stream.Collectors;

public class AntColonyOptimization {

    double[][] probabilityMatrix;
    double[][] distanceMatrix;
    double[][] rewardMartrix;
    Graph graph;
    List<Edge> edges;
    int length;
    public static double alpha = 1.0;
    public static double beta = 2.0;
    public static double decay = 0.7;
    List<Vertex> vertices;

    public AntColonyOptimization(Graph graph) {
        vertices = new ArrayList<>(graph.getAllVertices().stream().sorted(Comparator.comparingLong(Vertex::getId)).collect(Collectors.toList()));
        length = vertices.size();
        distanceMatrix = new double[length][length];
        probabilityMatrix = new double[length][length];
        rewardMartrix = new double[length][length];
        this.edges = new ArrayList<>(graph.getAllEdges().stream().sorted().collect(Collectors.toList()));
        this.graph = graph;
//        this.alpha = 24.0;
//        this.beta = 25.0;
    }

    public List<Vertex> startOptimization() {
        initializeDistanceMatrix();
        initializeRewardMatrix();
        initializeProbabilityMatrix();

        double minTour = Double.MAX_VALUE;
        Random random = new Random();
        List<Vertex> minCircuit = new ArrayList<>();
        double prevPheromoneTrail = 0.0;
        for (int i = 0; i < 5000; i++) {
            List<Integer> tour = calculateAntColonyTour(random.nextInt(length - 1));
            List<Vertex> tourVertices = new ArrayList<>();
            for (int v : tour) {
                tourVertices.add(vertices.get(v));
            }

            double tourCost = GraphUtil.getTotalCostOfTour(tourVertices);
            minTour = minTour > tourCost ? tourCost : minTour;
            minCircuit = tourVertices;
            updateRewards(tourCost, prevPheromoneTrail, tourVertices);
            initializeProbabilityMatrix();
            prevPheromoneTrail = tourCost;
        }

        return minCircuit;
    }

    public void updateRewards(double tourCost, double prevPheromoneTrail, List<Vertex> circuit) {
        for (int i = 0; i < circuit.size() - 1; i++) {
            int first = (int) circuit.get(i).getId();
            int second = (int) circuit.get(i+1).getId();
            rewardMartrix[first][second] += rewardMartrix[first][second]/tourCost;
            rewardMartrix[second][first] += rewardMartrix[first][second]/tourCost;
            rewardMartrix[first][second] = (1 - decay) * rewardMartrix[first][second] + decay * (tourCost - prevPheromoneTrail);
            rewardMartrix[second][first] = (1 - decay) * rewardMartrix[second][first] + decay * (tourCost - prevPheromoneTrail);
        }

        for (int i = 0; i < rewardMartrix.length; i++) {
            for (int j = 0; j < rewardMartrix[i].length; j++) {
                double decayFactor = (1 - decay) * rewardMartrix[i][j] + decay * (tourCost - prevPheromoneTrail);
                rewardMartrix[i][j] = decayFactor;
            }
        }
    }

    private void recalculateProbabilityMatrix(List<Integer> unvisited, int source) {
        double totalInverseRewardDistance = 0.0;
        for (int i: unvisited) {
            double inverseDistance = Math.pow(1/distanceMatrix[source][i], alpha) * Math.pow(rewardMartrix[source][i], beta);
            totalInverseRewardDistance += inverseDistance;
        }

        for (int i: unvisited) {
            probabilityMatrix[source][i] = Math.pow(1/distanceMatrix[source][i], alpha) * Math.pow(rewardMartrix[source][i], beta)/totalInverseRewardDistance;
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
            double totalInverseRewardDistance = 0.0;
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                totalInverseRewardDistance += Math.pow(1/distanceMatrix[i][j], alpha) * Math.pow(rewardMartrix[i][j], beta);
            }

            for (int j = 0; j < distanceMatrix[i].length; j++) {
                probabilityMatrix[i][j] = Math.pow(1/distanceMatrix[i][j], alpha) * Math.pow(rewardMartrix[i][j], beta) / totalInverseRewardDistance;
            }
        }
    }

    private void initializeRewardMatrix() {
        for (int i = 0; i < rewardMartrix.length; i++) {
            for (int j = 0; j < rewardMartrix[i].length; j++) {
                rewardMartrix[i][j] = 1.0;
            }
        }
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
