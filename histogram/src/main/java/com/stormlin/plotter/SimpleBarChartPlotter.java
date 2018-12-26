package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramData;
import main.java.com.stormlin.histogram.HistogramYAxis;

import java.awt.*;
import java.util.ArrayList;

public class SimpleBarChartPlotter extends Plotter {

    public SimpleBarChartPlotter(Histogram histogram) {
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
        plotXAxis(histogram, g);
        plotLegend(histogram, g);
    }

    private void plotBars(Graphics g) {
        HistogramYAxis leftAxis = histogram.getyYAxisList().get(0);
        HistogramYAxis rightAxis = null;
        if (histogram.getyYAxisList().size() > 1) {
            rightAxis = histogram.getyYAxisList().get(1);
        }

        ArrayList<HistogramData> dataList = histogram.getHistogramDataList();
        HistogramData leftRulerData = dataList.get(0);
        HistogramData rightRulerData = dataList.get(1);

        // Assuming that the number of bars in each group is identical
        int barsPerGroup = 1;
        int nGroups = leftRulerData.getValue().length;
        int spans = getSpans(nGroups, barsPerGroup);
        int pointsPerSpan = (int) histogram.getPlotAreaWidth() / spans;
        int barStartX = (int) ((double) histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int barStartY = (int) (histogram.getCanvasHeight() * (1.0 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));

        boolean hasRightRuler = (rightAxis != null);
        // Use this to store the X and Y coordinates for all bars
        double[][] barCoordinates = new double[nGroups][2];

        int barHeight;
        /* Plot the bars for left ruler */
        for (int i = 0; i < nGroups; i++) {
            barStartX += pointsPerSpan;
            barHeight = (int) (leftRulerData.getValue()[i] * leftAxis.getPointsPerUnit());
            g.setColor(leftRulerData.getBarBorderColor());
            // Draw the bar borders
            g.drawRect(barStartX, barStartY - barHeight, pointsPerSpan, barHeight);
            g.setColor(leftRulerData.getBarBackgroundColor());
            g.fillRect(barStartX + 1, barStartY - barHeight + 1, pointsPerSpan - 1, barHeight - 1);
            g.setColor(Constants.DEFAULT_FOREGROUND_COLOR);
            if (hasRightRuler) {
                barCoordinates[i][0] = barStartX;
                barCoordinates[i][1] = barStartY;
            }
            barStartX += pointsPerSpan;
        }

        /* Plot the line for right ruler */
        if (hasRightRuler) {
            int pointStartX, pointStartY;
            int pointEndX, pointEndY;
            int pointStartHeight, pointEndHeight;
            int[][] markCoordinates = new int[nGroups][2];
            for (int i = 0; i < nGroups - 1; i++) {
                pointStartHeight = (int) (rightRulerData.getValue()[i] * rightAxis.getPointsPerUnit());
                pointEndHeight = (int) (rightRulerData.getValue()[i + 1] * rightAxis.getPointsPerUnit());
                pointStartX = (int) (barCoordinates[i][0] + 0.5 * pointsPerSpan);
                pointStartY = barStartY - pointStartHeight;
                pointEndX = (int) (barCoordinates[i + 1][0] + 0.5 * pointsPerSpan);
                pointEndY = barStartY - pointEndHeight;

                /* Store the coordinates of all marks */
                markCoordinates[i][0] = pointStartX;
                markCoordinates[i][1] = pointStartY;
                markCoordinates[i + 1][0] = pointEndX;
                markCoordinates[i + 1][1] = pointEndY;

                g.setColor(Color.BLACK);
                g.drawLine(pointStartX, pointStartY, pointEndX, pointEndY);
            }
            for (int[] coordinate : markCoordinates) {
                plotLineMark(coordinate[0], coordinate[1], Constants.DEFAULT_MARK_LINE_LENGTH, g);
            }
            g.setColor(Constants.DEFAULT_FOREGROUND_COLOR);
        }
    }

}

