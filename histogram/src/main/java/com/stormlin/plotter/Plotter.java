package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

class Plotter extends JPanel {

    Histogram histogram;

    Plotter(Histogram histogram) {
        this.histogram = histogram;
    }

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
        g.setColor(Constants.DEFAULT_FOREGROUND_COLOR);

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

    void plotXAxisKeys(Histogram histogram, Graphics g) {
        // Uses the keys of first group of data
        HistogramData dataWithKey = histogram.getHistogramDataList().get(0);
        String[] keys = dataWithKey.getKey();
        HistogramXAxis xAxis = histogram.getXAxis();

        Font xFont = new Font(xAxis.getFontName(), xAxis.getFontStyle(), xAxis.getFontSize());
        FontMetrics metrics = g.getFontMetrics(xFont);

        int barsPerGroup = (histogram.getHistogramType().equals("GroupedBarChart")) ?
                histogram.getHistogramDataList().size() : 1;
        int nGroups = dataWithKey.getValue().length;
        int spans = getSpans(nGroups, barsPerGroup);
        int pointsPerSpan = (int) histogram.getPlotAreaWidth() / spans;
        int keyX = (int) ((double) histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int keyY = (int) (histogram.getCanvasHeight() * (1.0 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
        int groupWidth = pointsPerSpan * barsPerGroup;

        /* Plot keys */
        int stringLength;
        int stringHeight = 0;
        int groupCenterX;
        for (int i = 0; i < nGroups; i++) {
            keyX += pointsPerSpan;
            stringLength = metrics.stringWidth(keys[i]);
            stringHeight = metrics.getHeight();
            groupCenterX = (int) (keyX + 0.5 * groupWidth);
            g.drawString(keys[i], (int) (groupCenterX - 0.5 * stringLength), (int) (keyY + 0.75 * stringHeight));
            keyX += groupWidth;
        }

        /* Draw XAxis label */
        Font labelFont = new Font(xAxis.getFontName(), xAxis.getFontStyle(), xAxis.getFontSize());
        metrics = g.getFontMetrics(labelFont);
        g.setFont(labelFont);

        int coordinateX = (int) (histogram.getCanvasWidth() * 0.5);
        int coordinateY = (int) ((histogram.getCanvasHeight() +
                histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM])) * 0.5) +
                (int) (0.5 * stringHeight);
        // Align center for default
        int titleLength = metrics.stringWidth(xAxis.getLabel());

        g.drawString(xAxis.getLabel(), (int) (coordinateX - 0.5 * titleLength), coordinateY);
    }

    void plotLegend(Histogram histogram, Graphics g) {
        HistogramLegend legend = histogram.getLegend();
        ArrayList<HistogramData> dataList = histogram.getHistogramDataList();

        Font legendFont = new Font(legend.getFontName(), legend.getFontStyle(), legend.getFontSize());
        FontMetrics metrics = g.getFontMetrics();

        String[] names = new String[dataList.size()];
        int[] maxWidthAndHeight = new int[2];
        final int WIDTH = 0;
        final int HEIGHT = 1;
        for (int i = 0; i < dataList.size(); i++) {
            names[i] = dataList.get(i).getName();
        }
        getMaxStringWidthAndHeight(names, g, maxWidthAndHeight);

        int plotAreaLeftUpperX = (int) (histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int plotAreaLeftUpperY = (int) (histogram.getCanvasHeight() * histogram.getMargins()[Constants.MARGIN_UPPER]);

        int margin = (int) (plotAreaLeftUpperX * 0.1);

        int legendX = plotAreaLeftUpperX + margin;
        int legendY = plotAreaLeftUpperY + margin;
        int lineSpacing = (int) (0.75 * maxWidthAndHeight[HEIGHT]);

        int legendWidth = margin + (int) (0.5 * maxWidthAndHeight[WIDTH]) + margin + maxWidthAndHeight[WIDTH] + margin;
        int legendHeight = 2 * margin + maxWidthAndHeight[HEIGHT] * dataList.size() + lineSpacing * (dataList.size() - 1);

        /* Draw the border of this legend */
        g.drawRect(legendX, legendY, legendWidth, legendHeight);

        /* Draw the colors and labels */
        int itemX = legendX + margin;
        int itemY = legendY + margin;
        for (HistogramData data : dataList) {
            g.setColor(data.getBarBackgroundColor());
            g.fillRect(itemX, itemY, (int) (maxWidthAndHeight[WIDTH] * 0.5), maxWidthAndHeight[1]);
            drawStringAt(data.getName(), itemX + (int) (maxWidthAndHeight[WIDTH] * 0.5) + margin, itemY, g, metrics,
                    maxWidthAndHeight[HEIGHT]);
            itemY += (maxWidthAndHeight[1] + lineSpacing);
        }
    }

    private void getMaxStringWidthAndHeight(String[] names, Graphics g, int[] maxValues) {
        Graphics2D g2d = (Graphics2D) g.create();
        FontRenderContext context = g2d.getFontRenderContext();
        GlyphVector vector;
        Rectangle bounds;
        for (String name : names) {
            vector = g2d.getFont().createGlyphVector(context, name);
            bounds = vector.getPixelBounds(null, 0, 0);
            if (bounds.getWidth() > maxValues[0]) {
                maxValues[0] = (int) bounds.getWidth();
            }
            if (bounds.getHeight() > maxValues[1]) {
                maxValues[1] = (int) bounds.getHeight();
            }
        }
    }

    private void drawStringAt(String text, int x, int y, Graphics g, FontMetrics metrics, int maxLineHeight) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x, y + metrics.getAscent() - metrics.getDescent() - metrics.getLeading() + (int) (0.25 * maxLineHeight));
    }
}
