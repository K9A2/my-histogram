package main.java.com.stormlin.plotter;

import main.java.com.stormlin.histogram.Histogram;

import java.awt.*;

public class AnimatedGroupedBarChart extends Plotter {

    private int clock = 0;

    public void nextFrame() {
        clock++;
    }

    public AnimatedGroupedBarChart(Histogram histogram) {
        super(histogram);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(histogram.getBackgroundColor());

        switch (clock) {
            case 5:
                plotLegend(histogram, g);
            case 4:
                plotTitle(histogram, g);
            case 3:
                plotYAxis(histogram, g);
            case 2:
                plotXAxis(histogram, g);
            case 1:
                GroupedBarChartPlotter plotter = new GroupedBarChartPlotter(histogram);
                plotter.plotBars(g);
            case 0:
                plotBorder(histogram, g);
                break;
        }
    }

}
