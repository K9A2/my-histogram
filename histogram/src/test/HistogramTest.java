package test;

import main.java.com.stormlin.histogram.Histogram;

public class HistogramTest {

    public static void main(String[] args) {

        String filePath = "./data/" + args[0];

        Histogram histogram = new Histogram(filePath);

        histogram.draw();

    }

}
