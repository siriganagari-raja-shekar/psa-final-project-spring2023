package org.info6205.tsp;

import org.info6205.tsp.util.FileHelper;

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
    Preprocess() {
        nodeMap = new HashMap<>();
    }

    /**
     * Starts the pre-processing steps
     * @param fileName Filename with extension
     * @return The list of strings after pre-processing
     */
    public List<String> start(String fileName) {
        List<String> rawLines = readData(fileName);
        return substituteNodeHash(rawLines);
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
        int nodeNumber = 1;
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
}
