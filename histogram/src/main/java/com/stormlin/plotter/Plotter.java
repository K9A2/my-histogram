package main.java.com.stormlin.plotter;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.histogram.Histogram;

import java.awt.*;

public interface Plotter {

    default void plotBorder(Histogram histogram, Graphics g) {
        double width = histogram.getCanvasWidth();
        double height = histogram.getCanvasHeight();
        double[] margins = histogram.getMargins();

        double plotAreaX = width * margins[Constants.MARGIN_LEFT];
        double plotAreaY = height * margins[Constants.MARGIN_UPPER];
        double plotAreaWidth = width * (1 - margins[Constants.MARGIN_LEFT] - margins[Constants.MARGIN_RIGHT]);
        double plotAreaHeight = height * (1 - margins[Constants.MARGIN_UPPER] - margins[Constants.MARGIN_BOTTOM]);

        g.drawRect((int) plotAreaX, (int) plotAreaY, (int) plotAreaWidth, (int) plotAreaHeight);
    }

}
