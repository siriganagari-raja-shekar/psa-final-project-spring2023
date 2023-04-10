package org.info6205.tsp.io;

import org.info6205.tsp.core.Graph;
import org.info6205.tsp.core.UndirectedGraph;
import org.info6205.tsp.core.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs pre-processing activities:
 *  1. Reads data from given CSV file
 *  2. Replaces node hash id with an integer equivalent
 */
public class Preprocess {
    private Map<Integer, String> nodeMap;
    public Preprocess() {
        nodeMap = new HashMap<>();
    }

    /**
     * Starts the pre-processing steps
     * @param fileName Filename with extension
     * @return The list of strings after pre-processing
     */
    public Graph start(String fileName) throws Exception {
        List<String> rawLines = readData(fileName);
        rawLines = substituteNodeHash(rawLines);
        return getGraph(rawLines);
    }

    /**
     * Returns a map of the simplified node id and hash
     * @return Map of the simplified node id and hash
     */
    public Map<Integer, String> getNodeMap() {
        return nodeMap;
    }

    /**
     * Reads data from the given filename
     * @param fileName Filename with extension
     * @return The list of strings obtained from the file
     */
    private List<String> readData(String fileName) {
        List<String> rawLines;
        FileHelper fh = new FileHelper();
        try {
            rawLines = fh.read(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return rawLines;
    }

    /**
     * The node hash obtained from file is substituted with simple integer values.
     * @param rawLines Raw lines read from the file
     * @return Substituted raw lines with simplified node id
     */
    private List<String> substituteNodeHash(List<String> rawLines) {
        int nodeNumber = 0;
        List<String> lines = new ArrayList<>();

        // Remove column headings
        rawLines.remove(0);
        for(String line: rawLines) {
            String[] words = line.split(",");
            nodeMap.put(nodeNumber, words[0]);
            words[0] = nodeNumber++ + "";
            lines.add(String.join(",", words));
        }

        return lines;
    }

    /**
     * A graph is created
     * @param lines the raw lines that contains id and coordinates
     * @return the graph generated
     * @throws Exception
     */
    private Graph getGraph(List<String> lines) throws Exception {
        Graph graph = new UndirectedGraph();
        for(String line: lines){
            String[] lineSplit = line.split(",");
            graph.addVertex(new Vertex(Long.parseLong(lineSplit[0]), Double.parseDouble(lineSplit[2]), Double.parseDouble(lineSplit[1])));
        }

        List<Vertex> vertexList = new ArrayList<>(graph.getAllVertices());

        for (int i = 0; i < vertexList.size(); i++) {
            for (int j = i+1; j < vertexList.size(); j++) {
                Vertex a = vertexList.get(i);
                Vertex b = vertexList.get(j);
                graph.addEdge(a, b);
            }
        }

        return graph;
    }
}
