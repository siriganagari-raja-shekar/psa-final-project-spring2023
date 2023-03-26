package org.info6205.tsp;

import org.info6205.tsp.util.FileHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Preprocess {

    public void start(String fileName) {
        List<String> lines = readData(fileName);
        testLineRead(lines);
    }

    private List<String> readData(String fileName) {
        List<String> rawLines = null;
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
