package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramData;
import main.java.com.stormlin.histogram.HistogramYAxis;

import java.awt.*;
import java.util.ArrayList;

public class StackedBarChartPlotter extends Plotter {

    public StackedBarChartPlotter(Histogram histogram) {
        super(histogram);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(histogram.getBackgroundColor());

        plotBorder(histogram, g);
        plotBars(g);
        plotYAxis(histogram, g);
        plotTitle(histogram, g);
        plotXAxisKeys(histogram, g);
        plotLegend(histogram, g);
    }

    private void plotBars(Graphics g) {
        HistogramYAxis leftAxis = histogram.getyYAxisList().get(0);

        ArrayList<HistogramData> dataList = histogram.getHistogramDataList();

        // Assuming that the number of bars in each group is identical
        int barsPerGroup = 1;
        int nGroups = dataList.get(0).getValue().length;
        int spans = getSpans(nGroups, barsPerGroup);
        int pointsPerSpan = (int) histogram.getPlotAreaWidth() / spans;
        int barStartX = (int) ((double) histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int barStartY = (int) (histogram.getCanvasHeight() * (1.0 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));

        int barHeight;
        int stackedHeight;
        /* Plot the bars */
        for (int i = 0; i < nGroups; i++) {
            barStartX += pointsPerSpan;
            barHeight = 0;
            stackedHeight = 0;
            for (int j = 0; j < dataList.size(); j++) {
                HistogramData data = dataList.get(j);
                barHeight = (int) (data.getValue()[i] * leftAxis.getPointsPerUnit());
                g.setColor(data.getBarBorderColor());
                // Draw the bar borders
                g.drawRect(barStartX, barStartY - stackedHeight - barHeight, pointsPerSpan, barHeight);
                g.setColor(data.getBarBackgroundColor());
                g.fillRect(barStartX + 1, barStartY - stackedHeight - barHeight + 1, pointsPerSpan - 1, barHeight - 1);
                stackedHeight += barHeight;
            }
            barStartX += pointsPerSpan;
        }
        // Reset the pen color
        g.setColor(Constants.DEFAULT_FOREGROUND_COLOR);
    }

}
