package main;

import util.StdDraw;

import java.awt.Font;

public class HistogramA {

    /* Key components */
    private HistogramCanvas canvas;
    private HistogramFormat format;
    private HistogramData data;

    /* Two dimension parameters: MIN/MAX */
    // use xValue to align the xticks
    private double[] xValue = new double[2];
    // use yValue to set the tick and step of left ruler
    private double[] yValue = new double[2];
    private double[] xScale = new double[2];
    private double[] yScale = new double[2];

    /* The width of left ruler and right ruler (if has) */
    private double rulerWidth[] = {0.0, 0.0};

    /*
    The ruler in Histogram is determined by the <rulerStep> and <nStep>. <rulerStep> decides the value of each step in
    this ruler, and <nStep> decides how many steps it has. When plotting the ruler, the drawer will plot from the
    smallest number to the largest.
    */
    private double rulerStep;
    private int nStep;

    /* Use them to set the margin of this HistogramA instance */
    private final static int UPPER = 0;
    private final static int BOTTOM = 1;
    private final static int LEFT = 2;
    private final static int RIGHT = 3;

    private final static int MIN = 0;
    private final static int MAX = 1;

    /**
     * Use this constructor to set the components of this HistogramA instance
     *
     * @param canvas A HistogramCanvas component
     * @param format A HistogramFormat component
     * @param data   A HistogramData component
     */
    public HistogramA(HistogramCanvas canvas, HistogramFormat format, HistogramData data) {
        // Incoming components
        this.canvas = canvas;
        this.format = format;
        this.data = data;

        setHistogramParameters();
    }

    /**
     * Set various parameters of this HistogramA instance
     */
    private void setHistogramParameters() {

        // The data extracted from JSON file
        double[] values = this.data.values;

        // The size of data.values, use to plot the xticks
        xValue[MIN] = -1.0;
        xValue[MAX] = values.length;

        double max = values[0];
        for (double num : values) {
            if (num > max) {
                max = num;
            }
        }

        /* set the step of left ruler */
        // the value range of data.values
        double span = max - yValue[MIN];
        // the step of left ruler
        double step = 1.0;
        if (span >= 1) {
            while (span >= 10) {
                span /= 10;
                step *= 10;
            }
        } else {
            while (span < 1) {
                span *= 10;
                step /= 10;
            }
        }

        int nSpan = (int) Math.ceil(span);
        yValue[MAX] = yValue[MIN] + step * nSpan;
        switch (nSpan) {
            case 1:
                nStep = 5;
                rulerStep = step / 5;
                break;
            case 2:
            case 3:
                nStep = nSpan * 2;
                rulerStep = step / 2;
                break;
            default:
                nStep = nSpan;
                rulerStep = step;
                break;
        }
    }

    public void draw() {

        /* Set up the canvas */
        StdDraw.setCanvasSize(canvas.x, canvas.y);
        StdDraw.clear(canvas.backgroundColor);
        StdDraw.setPenColor(canvas.foregroundColor);

        double[] values = data.values;
        int n = values.length;
        setHistogramScale(n);

        // Plot ruler (right ruler) before bars to avoid the right ruler is outside the figure
        plotRuler("left");
        if (format.hasRightRuler) {
            plotRuler("right");
        }

        plotBars(values, n);

        plotKeys();

        if (format.hasBorder) {
            plotBorder();
        }

        if (format.hasHeader) {
            plotHeader();
        }
        if (format.hasFooter) {
            plotFooter();
        }
    }

    /**
     * Set the scale of this Histogram instance
     *
     * @param nBars The number of all bars
     */
    private void setHistogramScale(int nBars) {
        // The value range of left ruler
        double span = yValue[MAX] - yValue[MIN] + 1;

        // The height of plotting area in percentage
        double heightInPercent = 1 - format.margins[UPPER] - format.margins[BOTTOM];
        // Get the change in Y value for one percent increase on screen
        double ySpacing = span / heightInPercent;
        yScale[MIN] = yValue[MIN] - format.margins[BOTTOM] * ySpacing - 1;
        yScale[MAX] = yValue[MAX] + format.margins[UPPER] * ySpacing;
        StdDraw.setYscale(yScale[MIN], yScale[MAX]);

        // The width of plotting area in percentage
        double widthInPercentage = 1 - format.margins[LEFT] - format.margins[RIGHT];
        // Get the change in X value for one percent increase on screen
        double xSpacing = (nBars + 1) / widthInPercentage;
        xScale[MIN] = -format.margins[LEFT] * xSpacing - 1;
        xScale[MAX] = nBars + format.margins[RIGHT] * xSpacing;
        StdDraw.setXscale(xScale[MIN], xScale[MAX]);
    }

    private void plotRuler(String position) {
        /* Set up font and color for this ruler */
        Font font = new Font("consolas", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        StdDraw.setPenColor(format.rulerColor);

        /* The selected position fot this ruler, left or right. Default use the left (MIN). */
        int selectedPosition = MIN;
        if (position.equals("right")) {
            selectedPosition = MAX;
        }

        /* The left and right end of scale marks */
        final double markLeftX = xValue[selectedPosition] - 0.05;
        final double markRightX = xValue[selectedPosition] + 0.05;

        /* The left start of numbers */
        final double numberStartX = xScale[selectedPosition] + 0.6 * (xValue[selectedPosition] - xScale[selectedPosition]);

        /* Calculate and store the String representation of these marks, and the print them on the screen */
        // First just plot the scale mark
        String[] marks = new String[nStep + 1];
        for (int i = 0; i <= nStep; i++) {
            double y = yValue[MIN] + i * rulerStep;
            marks[i] = numberForRuler(y);
            StdDraw.line(markLeftX, y, markRightX, y);
        }
        int maxMarkWidth = getMaxMarkLength(marks);
        rulerWidth[selectedPosition] = maxMarkWidth;

        // Then plot the number on screen
        for (int i = 0; i <= nStep; i++) {
            double y = yValue[MIN] + i * rulerStep;
            StdDraw.text(numberStartX, y, String.format("%" + maxMarkWidth + "s", marks[i]));
        }
    }

    /**
     * Get the length of numerical mark
     *
     * @param marks All marks that are going to be plotted at the ruler
     * @return The maximum length of all marks
     */
    private int getMaxMarkLength(String[] marks) {
        int length = marks[0].length();
        for (String mark : marks) {
            if (length < mark.length()) {
                length = mark.length();
            }
        }
        return length;
    }

    private String numberForRuler(double number) {   // TODO: TO BE Customized
        if (yValue[MAX] >= 5 && rulerStep > 1) {
            return "" + (int) number;
        }
        if (rulerStep > 0.1) {
            return String.format("%.1f", number);
        }
        if (rulerStep > 0.01) {
            return String.format("%.2f", number);
        }
        if (rulerStep > 0.001) {
            return String.format("%.3f", number);
        }
        if (rulerStep > 0.0001) {
            return String.format("%.4f", number);
        }
        if (rulerStep > 0.00001) {
            return String.format("%.5f", number);
        }
        return String.format("%g", number);
    }

    private void plotBars(double[] values, int n) {
        if (format.isBarFilled) {
            StdDraw.setPenColor(format.barFillColor);
            for (int i = 0; i < n; i++) {
                StdDraw.filledRectangle(i, values[i] / 2, 0.25, values[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
        if (format.hasBarBorder) {
            StdDraw.setPenColor(format.barBorderColor);
            for (int i = 0; i < n; i++) {
                StdDraw.rectangle(i, values[i] / 2, 0.25, values[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
    }

    /**
     * Use this method to plot the keys for all bars
     */
    private void plotKeys() {
        Font font = new Font("calibri", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        StdDraw.setPenColor(format.keyColor);
        final double y = yValue[MIN] - 0.5 * rulerStep;
        for (int i = 0; i < data.keys.length; i++) {
            if (data.keys[i].length() >= 1) {
                double x = xValue[MIN] + 1 + i;
                StdDraw.text(x, y, data.keys[i]);
            }
        }
    }

    /**
     * Use this method to plot the border for plotting area
     */
    private void plotBorder() {
        double x = 0.5 * (xValue[MIN] + xValue[MAX]);
        double y = 0.5 * (yValue[MIN] + yValue[MAX]);
        double halfWidth = 0.5 * (xValue[MAX] - xValue[MIN]);
        double halfHeight = 0.5 * (yValue[MAX] - yValue[MIN]);
        StdDraw.setPenColor(format.borderColor);
        StdDraw.rectangle(x, y, halfWidth, halfHeight);
    }

    /**
     * Use this method to plot the header for this figure
     */
    private void plotHeader() {
        Font font = new Font("calibri", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        // Align center
        double x = 0.5 * (xScale[MIN] + xScale[MAX]);
        double y = 0.5 * (yValue[MAX] + yScale[MAX]);
        StdDraw.setPenColor(format.headerColor);
        StdDraw.text(x, y, data.header);
    }

    /**
     * Use this method to plot the footer for this figure
     */
    private void plotFooter() {
        Font font = new Font("calibri", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        // Align center
        double x = 0.5 * (xScale[MIN] + xScale[MAX]);
        double y = 0.5 * (yScale[MIN] + yValue[MIN]);
        StdDraw.setPenColor(format.footerColor);
        StdDraw.text(x, y, data.footer);
    }

}
