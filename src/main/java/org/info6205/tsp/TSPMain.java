package org.info6205.tsp;

/**
 * Hello world!
 *
 */
public class TSPMain
{
    public static void main( String[] args )
    {
        System.out.println("*".repeat(5) + " Starting application " + "*".repeat(5));
        Preprocess preprocess = new Preprocess();
        preprocess.start("crimeSample.csv");
        System.out.println("*".repeat(5) + " Application has completed running " + "*".repeat(5));
    }
}
