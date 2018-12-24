package main.java.com.stormlin.plotter;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;
import main.java.com.stormlin.histogram.HistogramAxis;
import main.java.com.stormlin.histogram.HistogramRuler;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    void plotRuler(Histogram histogram, Graphics g) {
        HistogramRuler leftRuler = histogram.getLeftRuler();
        HistogramAxis yAxis = histogram.getyAxis();

        Font defaultFont = new Font(yAxis.getFont(), yAxis.getFontStyle(), yAxis.getFontSize());
        FontMetrics metrics = g.getFontMetrics(defaultFont);
        g.setFont(defaultFont);

        int coordinateX = (int) (histogram.getCanvasWidth() * histogram.getMargins()[Constants.MARGIN_LEFT]);
        int coordinateY = (int) (histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
        int max = (int) leftRuler.getMax();
        int min = (int) leftRuler.getMin();
        int lineLength = Constants.DEFAULT_MARK_LINE_LENGTH;
        // Right-align the left ruler
        int rightEdge = (int) (coordinateX - 0.01 * histogram.getCanvasWidth());

        /* Plot the left ruler */
        for (int i = min; i <= max; i += (int) leftRuler.getStep()) {
            // Draw the numbers, default align right
            g.drawString(String.valueOf(i), rightEdge - metrics.stringWidth(String.valueOf(i)),
                    (int) (coordinateY + 0.25 * metrics.getHeight()));
            // Draw the line mark for this ruler
            g.drawLine(coordinateX - lineLength, coordinateY, coordinateX, coordinateY);
            coordinateY -= (int) (leftRuler.getStep() * leftRuler.getPointsPerUnit());
        }

        HistogramRuler rightRuler = histogram.getRightRuler();
        if (rightRuler != null) {
            /* Plot the right ruler */
            coordinateX = (int) (histogram.getCanvasWidth() * (1 - histogram.getMargins()[Constants.MARGIN_RIGHT]));
            coordinateY = (int) (histogram.getCanvasHeight() * (1 - histogram.getMargins()[Constants.MARGIN_BOTTOM]));
            max = (int) rightRuler.getMax();
            min = (int) leftRuler.getMin();
            int leftEdge = (int) (coordinateX + 0.01 * histogram.getCanvasWidth());
            for (int i = min; i <= max; i += (int) rightRuler.getStep()) {
                g.drawString(String.valueOf(i), leftEdge, (int) (coordinateY + 0.25 * metrics.getHeight()));
                g.drawLine(coordinateX, coordinateY, coordinateX + lineLength, coordinateY);
                coordinateY -= (int) (rightRuler.getStep() * rightRuler.getPointsPerUnit());
            }
        }
    }

}
