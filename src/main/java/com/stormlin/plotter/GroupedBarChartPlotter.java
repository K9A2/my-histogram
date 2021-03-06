package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramData;
import main.java.com.stormlin.histogram.HistogramYAxis;

import java.awt.*;
import java.util.ArrayList;

public class GroupedBarChartPlotter extends Plotter {

    public GroupedBarChartPlotter(Histogram histogram) {
        super(histogram);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(histogram.getBackgroundColor());

        plotBorder(histogram, g);
        plotBars(g);
        plotXAxis(histogram, g);
        plotYAxis(histogram, g);
        plotTitle(histogram, g);
        plotLegend(histogram, g);
    }

    public void plotBars(Graphics g) {
        /* Grouped bar chart only have one Y axis */
        HistogramYAxis leftAxis = histogram.getyYAxisList().get(0);
        ArrayList<HistogramData> dataList = histogram.getHistogramDataList();

        int barsPerGroup = dataList.size();
        // Assuming that the number of bars in each group is identical
        int nGroups = dataList.get(0).getValue().length;
        int spans = nGroups * (barsPerGroup + 1) + 1;
        int pointsPerSpan = (int) histogram.getPlotAreaWidth() / spans;
        int barStartX = (int) ((double) histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int barStartY = (int) (histogram.getCanvasHeight() * (1.0 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
        double pointsPerUnit = histogram.getPlotAreaHeight() / (leftAxis.getMax() - leftAxis.getMin());

        // Traverse these arrays to avoid wrong order between groups
        double[][] values = new double[nGroups][barsPerGroup];
        for (int i = 0; i < nGroups; i++) {
            for (int j = 0; j < barsPerGroup; j++) {
                HistogramData data = dataList.get(j);
                values[i][j] = data.getValue()[i];
            }
        }

        // Get the colors for bars
        Color[] barBackgroundColorArray = new Color[barsPerGroup];
        Color[] barBorderColorArray = new Color[barsPerGroup];
        for (int i = 0; i < dataList.size(); i++) {
            Color backgroundColor = dataList.get(i).getBarBackgroundColor();
            Color borderColor = dataList.get(i).getBarBorderColor();
            if (!dataList.get(i).getBarBackgroundColor().equals(Constants.DEFAULT_BACKGROUND_COLOR)) {
                barBackgroundColorArray[i] = backgroundColor;
            }
            if (!dataList.get(i).getBarBorderColor().equals(Constants.DEFAULT_FOREGROUND_COLOR)) {
                barBorderColorArray[i] = borderColor;
            }
        }

        for (int i = 0; i < nGroups; i++) {
            barStartX += pointsPerSpan;
            for (int j = 0; j < barsPerGroup; j++) {
                int barHeight = (int) (values[i][j] * pointsPerUnit);
                if (barBorderColorArray[j] != Constants.DEFAULT_FOREGROUND_COLOR) {
                    // There is a customized border color
                    g.setColor(barBorderColorArray[j]);
                }
                g.drawRect(barStartX, barStartY - barHeight, pointsPerSpan, barHeight);
                g.setColor(Constants.DEFAULT_FOREGROUND_COLOR);
                if (barBackgroundColorArray[j] != Constants.DEFAULT_BACKGROUND_COLOR) {
                    // There is a customized background color
                    g.setColor(barBackgroundColorArray[j]);
                    g.fillRect(barStartX + 1, barStartY - barHeight + 1, pointsPerSpan - 1, barHeight - 1);
                }
                barStartX += pointsPerSpan;
            }
        }
    }

}
