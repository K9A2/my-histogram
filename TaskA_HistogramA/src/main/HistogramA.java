package main;

import util.StdDraw;

import java.awt.Font;

public class HistogramA {

    /* Key components */
    private HistogramCanvas canvas;
    private HistogramFormat format;
    private HistogramData data;

    /* Two dimension parameters: MIN/MAX */
    private double[] xValue = new double[2];
    private double[] yValue = new double[2];
    private double[] xScale = new double[2];
    private double[] yScale = new double[2];

    /* Set the format of ruler */
    private int rulerGrade;
    private double rulerStep;

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
     * @param data A HistogramData component
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

        xValue[MIN] = -1;
        xValue[MAX] = values.length;

        yValue[MIN] = data.minValue;

        /* Get the max in values[] */
        double max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (max < values[i]) {
                max = values[i];
            }
        }

        /*  */
        double span = max - yValue[MIN];
        double factor = 1.0;
        if (span >= 1) {
            while (span >= 10) {
                span /= 10;
                factor *= 10;
            }
        } else {
            while (span < 1) {
                span *= 10;
                factor /= 10;
            }
        }
        // Calculate the count of spans
        int nSpan = (int) Math.ceil(span);
        yValue[MAX] = yValue[MIN] + factor * nSpan;
        switch (nSpan) {
            case 1:
                rulerGrade = 5;
                rulerStep = factor / 5;
                break;
            case 2:
            case 3:
                rulerGrade = nSpan * 2;
                rulerStep = factor / 2;
                break;
            default:
                rulerGrade = nSpan;
                rulerStep = factor;
                break;
        }
    }

    public void draw() {
        setCanvas();
        plotBars();
        plotRuler();
        plotKeys();
        if (format.hasBorder) {
            plotBorder();
        }
        if (format.hasRightRuler) {
            plotRightRuler();
        }
        if (format.hasHeader) {
            plotHeader();
        }
        if (format.hasFooter) {
            plotFooter();
        }
    }

    private void setCanvas() {
        StdDraw.setCanvasSize(canvas.x, canvas.y);
        setOriginalScale();
        StdDraw.clear(canvas.backgroundColor);
        StdDraw.setPenColor(canvas.foregroundColor);
    }

    private void setHistogramScale(int nBars) {
        double span = yValue[MAX] - yValue[MIN] + 1;
        double ySpacing = span / (1 - format.margins[UPPER] - format.margins[BOTTOM]);
        yScale[MIN] = yValue[MIN] - format.margins[BOTTOM] * ySpacing - 1;
        yScale[MAX] = yValue[MAX] + format.margins[UPPER] * ySpacing;
        StdDraw.setYscale(yScale[MIN], yScale[MAX]);

        double xSpacing = (nBars + 1) / (1 - format.margins[LEFT] - format.margins[RIGHT]);
        xScale[MIN] = -format.margins[LEFT] * xSpacing - 1;
        xScale[MAX] = nBars + format.margins[RIGHT] * xSpacing;
        StdDraw.setXscale(xScale[MIN], xScale[MAX]);
    }

    private void setOriginalScale() {
        StdDraw.setXscale(canvas.xScale[MIN], canvas.xScale[MAX]);
        StdDraw.setYscale(canvas.yScale[MIN], canvas.yScale[MAX]);
    }

    private void plotBars() {
        double[] a = data.values;
        int n = a.length;
        setHistogramScale(n);
        if (format.isBarFilled) {
            StdDraw.setPenColor(format.barFillColor);
            for (int i = 0; i < n; i++) {
                StdDraw.filledRectangle(i, a[i] / 2, 0.25, a[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
        if (format.hasBarBorder) {
            StdDraw.setPenColor(format.barBorderColor);
            for (int i = 0; i < n; i++) {
                StdDraw.rectangle(i, a[i] / 2, 0.25, a[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
    }

    private void plotRuler() {
        Font font = new Font("consolas", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        StdDraw.setPenColor(format.rulerColor);
        final double x0 = xValue[MIN] - 0.05, x1 = xValue[MIN] + 0.05;
        String[] mark = new String[rulerGrade + 1];
        for (int i = 0; i <= rulerGrade; i++) {
            double y = yValue[MIN] + i * rulerStep;
            mark[i] = numberForRuler(y);
            StdDraw.line(x0, y, x1, y);
        }
        int len = maxMarkLength(mark);
        final double xs = xScale[MIN] + 0.7 * (xValue[MIN] - xScale[MIN]);
        for (int i = 0; i <= rulerGrade; i++) {
            double y = yValue[MIN] + i * rulerStep;
            StdDraw.text(xs, y, String.format("%" + len + "s", mark[i]));
        }
    }

    private String numberForRuler(double x) {   // TODO: TO BE Customized
        if (yValue[MAX] >= 5 && rulerStep > 1) {
            return "" + (int) x;
        }
        if (rulerStep > 0.1) {
            return String.format("%.1f", x);
        }
        if (rulerStep > 0.01) {
            return String.format("%.2f", x);
        }
        if (rulerStep > 0.001) {
            return String.format("%.3f", x);
        }
        if (rulerStep > 0.0001) {
            return String.format("%.4f", x);
        }
        if (rulerStep > 0.00001) {
            return String.format("%.5f", x);
        }
        return String.format("%g", x);
    }

    private int maxMarkLength(String[] sa) {
        int n = sa[0].length();
        for (String s : sa) {
            if (n < s.length()) {
                n = s.length();
            }
        }
        return n;
    }

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

    private void plotBorder() {
        double x = .5 * (xValue[MIN] + xValue[MAX]);
        double y = .5 * (yValue[MIN] + yValue[MAX]);
        double halfWidth = .5 * (xValue[MAX] - xValue[MIN]);
        double halfHeight = .5 * (yValue[MAX] - yValue[MIN]);
        StdDraw.setPenColor(format.borderColor);
        StdDraw.rectangle(x, y, halfWidth, halfHeight);
    }

    private void plotRightRuler() {
        //TODO: Implemented this method
    }

    private void plotHeader() {
        Font font = new Font("calibri", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        double x = .5 * (xScale[MIN] + xScale[MAX]);
        double y = .5 * (yValue[MAX] + yScale[MAX]);
        StdDraw.setPenColor(format.headerColor);
        StdDraw.text(x, y, data.header);
    }

    private void plotFooter() {
        Font font = new Font("calibri", Font.PLAIN, 16); // TODO: TO BE Customized
        StdDraw.setFont(font);
        double x = .5 * (xScale[MIN] + xScale[MAX]);
        double y = .5 * (yScale[MIN] + yValue[MIN]);
        StdDraw.setPenColor(format.footerColor);
        StdDraw.text(x, y, data.footer);
    }

}
