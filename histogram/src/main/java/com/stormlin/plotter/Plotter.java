package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramYAxis;
import main.java.com.stormlin.histogram.HistogramTitle;

import javax.swing.*;
import java.awt.*;
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
        int lineLength = Constants.DEFAULT_MARK_LINE_LENGTH;
        // Right-align the left ruler
        int rightEdge = (int) (coordinateX - 0.01 * histogram.getCanvasWidth());

        for (int i = min; i <= max; i += (int) leftAxis.getStep()) {
            // Draw the numbers, default align right
            g.drawString(String.valueOf(i), rightEdge - metrics.stringWidth(String.valueOf(i)),
                    (int) (coordinateY + 0.25 * metrics.getHeight()));
            // Draw the line mark for this ruler
            g.drawLine(coordinateX - lineLength, coordinateY, coordinateX, coordinateY);
            coordinateY -= (int) (leftAxis.getStep() * leftAxis.getPointsPerUnit());
        }

        HistogramYAxis rightAxis = null;
        if (histogram.getyYAxisList().size() > 1) {
            rightAxis = histogram.getyYAxisList().get(1);
            /* Plot the right ruler */
            coordinateX = (int) (histogram.getCanvasWidth() * (1 - histogram.getMargins()[Constants.MARGIN_RIGHT]));
            coordinateY = (int) (histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
            max = (int) rightAxis.getMax();
            min = (int) rightAxis.getMin();
            int leftEdge = (int) (coordinateX + 0.01 * histogram.getCanvasWidth());
            for (int i = min; i <= max; i += (int) rightAxis.getStep()) {
                g.drawString(String.valueOf(i), leftEdge, (int) (coordinateY + 0.25 * metrics.getHeight()));
                g.drawLine(coordinateX, coordinateY, coordinateX + lineLength, coordinateY);
                coordinateY -= (int) (rightAxis.getStep() * rightAxis.getPointsPerUnit());
            }
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

}
