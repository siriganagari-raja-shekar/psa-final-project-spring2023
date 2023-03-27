package org.info6205.tsp;

import org.info6205.tsp.util.FileHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocess {
    private Map<Integer, String> nodeMap;
    Preprocess() {
        nodeMap = new HashMap<>();
    }

    public List<String> start(String fileName) {
        return readData(fileName);
//        testLineRead(lines);
    }

    public Map<Integer, String> getNodeMap() {
        return nodeMap;
    }

    private List<String> readData(String fileName) {
        List<String> rawLines;
        FileHelper fh = new FileHelper();
        try {
            rawLines = fh.read(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private void testLineRead(List<String> lines) {
        System.out.println("*".repeat(20));
        System.out.println("Substituted lines:");
        for (String line: lines) {
            System.out.println(line);
        }
        System.out.println("*".repeat(20));
    }
}
