package org.info6205.tsp;

import org.info6205.tsp.io.Preprocess;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for pre-process methods
 */
public class PreprocessTest {

    private Preprocess preprocess = new Preprocess();

    /**
     * Test reading of the pre-processed data
     */
    @Test
    public void preprocessDataCheck()
    {
        String[] lines = preprocess.start("sample.csv").toArray(new String[0]);
        String[] testLines = new String[] {
                "1,-0.172148,51.479017",
                "2,-0.084419,51.568244",
                "3,0.022465,51.533861",
                "4,-0.305044,51.393823",
                "5,0.05328,51.604349",
                "6,-0.075555,51.520492"
        };

        assertArrayEquals( lines, testLines );
    }
}
