package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramTitle;
import main.java.com.stormlin.histogram.HistogramYAxis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

class Plotter extends JPanel {

    void plotBorder(Histogram histogram, Graphics g) {
        g.drawRect((int) histogram.getPlotAreaX(), (int) histogram.getPlotAreaY(), (int) histogram.getPlotAreaWidth(),
                (int) histogram.getPlotAreaHeight());
    }

    int getSpans(int nGroups, int barsPerGroup) {
        return nGroups * (barsPerGroup + 1) + 1;
    }

    void plotLineMark(int x, int y, int lineLength, Graphics g) {
        g.drawLine(x - lineLength, y, x + lineLength, y);
        g.drawLine(x, y - lineLength, x, y + lineLength);
    }

    void plotYAxis(Histogram histogram, Graphics g) {
        ArrayList<HistogramYAxis> histogramYAxisList = histogram.getyYAxisList();

        /* Plot the left ruler */
        HistogramYAxis leftAxis = histogramYAxisList.get(0);
        Font fontLeft = new Font(leftAxis.getFontName(), leftAxis.getFontStyle(), leftAxis.getFontSize());
        FontMetrics metrics = g.getFontMetrics(fontLeft);
        g.setFont(fontLeft);

        int coordinateX = (int) (histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int coordinateY = (int) (histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
        int max = (int) leftAxis.getMax();
        int min = (int) leftAxis.getMin();
        int markLineLength = Constants.DEFAULT_MARK_LINE_LENGTH;
        int maxLineLength = 0;
        // Right-align the left ruler
        int rightEdge = (int) (coordinateX - 0.01 * histogram.getCanvasWidth());

        for (int i = min; i <= max; i += (int) leftAxis.getStep()) {
            int lineLength = metrics.stringWidth(String.valueOf(i));
            // Draw the numbers, default align right
            g.drawString(String.valueOf(i), rightEdge - lineLength, (int) (coordinateY + 0.25 * metrics.getHeight()));
            // Draw the line mark for this ruler
            g.drawLine(coordinateX - markLineLength, coordinateY, coordinateX, coordinateY);
            coordinateY -= (int) (leftAxis.getStep() * leftAxis.getPointsPerUnit());
            if (lineLength > maxLineLength) {
                maxLineLength = lineLength;
            }
        }
        // Draw the label for left YAxis
        int labelX = (int) (rightEdge - maxLineLength - 0.01 * histogram.getCanvasWidth());
        int labelY = (int) (histogram.getCanvasHeight() * histogram.getMargins()[Constants.MARGIN_UPPER] +
                histogram.getPlotAreaHeight() * 0.5);
        AffineTransform rotateNegative90 = AffineTransform.getRotateInstance(Math.toRadians(-90), labelX, labelY);
        plotYAxisLabel(g, rotateNegative90, leftAxis.getLabel(),
                (int) (labelX - 0.5 * metrics.stringWidth(leftAxis.getLabel())), labelY);

        /* Plot the right ruler */
        HistogramYAxis rightAxis;
        if (histogram.getyYAxisList().size() > 1) {
            rightAxis = histogram.getyYAxisList().get(1);
            coordinateX = (int) (histogram.getCanvasWidth() * (1 - histogram.getMargins()[Constants.MARGIN_RIGHT]));
            coordinateY = (int) (histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
            max = (int) rightAxis.getMax();
            min = (int) rightAxis.getMin();
            int leftEdge = (int) (coordinateX + 0.01 * histogram.getCanvasWidth());
            maxLineLength = 0;
            for (int i = min; i <= max; i += (int) rightAxis.getStep()) {
                int lineLength = metrics.stringWidth(String.valueOf(i));
                g.drawString(String.valueOf(i), leftEdge, (int) (coordinateY + 0.25 * metrics.getHeight()));
                g.drawLine(coordinateX, coordinateY, coordinateX + markLineLength, coordinateY);
                coordinateY -= (int) (rightAxis.getStep() * rightAxis.getPointsPerUnit());
                if (lineLength > markLineLength) {
                    maxLineLength = lineLength;
                }
            }
            // Draw the label for right YAxis
            labelX = (int) (leftEdge + maxLineLength + 0.01 * histogram.getCanvasWidth());
            labelY = (int) (histogram.getCanvasHeight() * histogram.getMargins()[Constants.MARGIN_UPPER] +
                    histogram.getPlotAreaHeight() * 0.5);
            AffineTransform rotatePositive90 = AffineTransform.getRotateInstance(Math.toRadians(90), labelX, labelY);
            plotYAxisLabel(g, rotatePositive90, rightAxis.getLabel(),
                    (int) (labelX - 0.5 * metrics.stringWidth(leftAxis.getLabel())), labelY);
        }
    }

    void plotTitle(Histogram histogram, Graphics g) {
        HistogramTitle title = histogram.getHistogramTitle();
        Font titleFont = new Font(title.getFontName(), title.getFontStyle(), title.getFontSize());
        FontMetrics metrics = g.getFontMetrics(titleFont);
        g.setFont(titleFont);

        int coordinateX = (int) (histogram.getCanvasWidth() * 0.5);
        int coordinateY = (int) (histogram.getCanvasHeight() * histogram.getMargins()[Constants.MARGIN_UPPER] * 0.5);
        // Align center for default
        int titleLength = metrics.stringWidth(title.getText());

        g.drawString(title.getText(), (int) (coordinateX - 0.5 * titleLength), coordinateY);
    }

    void plotYAxisLabel(Graphics g, AffineTransform transform, String label, int labelX, int labelY) {
        Graphics2D temp = (Graphics2D) g.create();
        temp.transform(transform);
        temp.drawString(label, labelX, labelY);
    }

}
