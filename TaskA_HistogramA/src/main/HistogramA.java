package main;

import util.StdDraw;

import java.awt.Font;

public class HistogramA {

    private HistogramCanvas c;
    private HistogramFormat f;
    private HistogramData d;
    private double[] xValue;  // MIN, MAX
    private double[] yValue;  // MIN, MAX
    private double[] xScale;  // MIN, MAX
    private double[] yScale;  // MIN, MAX
    private int rulerGrade;
    private double rulerStep;

    private final static int NORTH = 0;
    private final static int SOUTH = 1;
    private final static int WEST = 2;
    private final static int EAST = 3;
    private final static int MIN = 0;
    private final static int MAX = 1;

    public HistogramA(HistogramCanvas c, HistogramFormat f, HistogramData d) {
        this.c = c;
        this.f = f;
        this.d = d;
        xValue = new double[2];
        yValue = new double[2];
        xScale = new double[2];
        yScale = new double[2];
        setHistogramParameters();
    }

    private void setHistogramParameters() {
        double[] a = d.values;
        xValue[MIN] = -1;
        xValue[MAX] = a.length;

        yValue[MIN] = d.minValue;

        double max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }

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
        if (f.hasBorder) {
            plotBorder();
        }
        if (f.hasRightRuler) {
            plotRightRuler();
        }
        if (f.hasHeader) {
            plotHeader();
        }
        if (f.hasFooter) {
            plotFooter();
        }
    }

    private void setCanvas() {
        StdDraw.setCanvasSize(c.x, c.y);
        setOriginalScale();
        StdDraw.clear(c.bgColor);
        StdDraw.setPenColor(c.color);
    }

    private void setHistogramScale(int nBars) {
        double span = yValue[MAX] - yValue[MIN] + 1;
        double ySpacing = span / (1 - f.margins[NORTH] - f.margins[SOUTH]);
        yScale[MIN] = yValue[MIN] - f.margins[SOUTH] * ySpacing - 1;
        yScale[MAX] = yValue[MAX] + f.margins[NORTH] * ySpacing;
        StdDraw.setYscale(yScale[MIN], yScale[MAX]);

        double xSpacing = (nBars + 1) / (1 - f.margins[WEST] - f.margins[EAST]);
        xScale[MIN] = -f.margins[WEST] * xSpacing - 1;
        xScale[MAX] = nBars + f.margins[EAST] * xSpacing;
        StdDraw.setXscale(xScale[MIN], xScale[MAX]);
    }

    private void setOriginalScale() {
        StdDraw.setXscale(c.xScale[MIN], c.xScale[MAX]);
        StdDraw.setYscale(c.yScale[MIN], c.yScale[MAX]);
    }

    private void plotBars() {
        double[] a = d.values;
        int n = a.length;
        setHistogramScale(n);
        if (f.isBarFilled) {
            StdDraw.setPenColor(f.barFillColor);
            for (int i = 0; i < n; i++) {
                StdDraw.filledRectangle(i, a[i] / 2, 0.25, a[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
        if (f.hasBarFrame) {
            StdDraw.setPenColor(f.barFrameColor);
            for (int i = 0; i < n; i++) {
                StdDraw.rectangle(i, a[i] / 2, 0.25, a[i] / 2);
                // (x, y, halfWidth, halfHeight)
            }
        }
    }

    private void plotRuler() {
        Font font = new Font("consolas", Font.PLAIN, 12); // TO BE Customized
        StdDraw.setFont(font);
        StdDraw.setPenColor(f.rulerColor);
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

    private String numberForRuler(double x) {   // TO BE Customized
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
        Font font = new Font("consolas", Font.PLAIN, 12); // TO BE Customized
        StdDraw.setFont(font);
        StdDraw.setPenColor(f.keyColor);
        final double y = yValue[MIN] - 0.5 * rulerStep;
        for (int i = 0; i < d.keys.length; i++) {
            if (d.keys[i].length() >= 1) {
                double x = xValue[MIN] + 1 + i;
                StdDraw.text(x, y, d.keys[i]);
            }
        }
    }

    private void plotBorder() {
        double x = .5 * (xValue[MIN] + xValue[MAX]);
        double y = .5 * (yValue[MIN] + yValue[MAX]);
        double halfWidth = .5 * (xValue[MAX] - xValue[MIN]);
        double halfHeight = .5 * (yValue[MAX] - yValue[MIN]);
        StdDraw.setPenColor(f.borderColor);
        StdDraw.rectangle(x, y, halfWidth, halfHeight);
    }

    private void plotRightRuler() {
        //TODO: Implemented this method
    }

    private void plotHeader() {
        Font font = new Font("calibri", Font.PLAIN, 20); // TO BE Customized
        StdDraw.setFont(font);
        double x = .5 * (xScale[MIN] + xScale[MAX]);
        double y = .5 * (yValue[MAX] + yScale[MAX]);
        StdDraw.setPenColor(f.headerColor);
        StdDraw.text(x, y, d.header);
    }

    private void plotFooter() {
        Font font = new Font("consolas", Font.BOLD, 16); // TO BE Customized
        StdDraw.setFont(font);
        double x = .5 * (xScale[MIN] + xScale[MAX]);
        double y = .5 * (yScale[MIN] + yValue[MIN]);
        StdDraw.setPenColor(f.footerColor);
        StdDraw.text(x, y, d.footer);
    }

}
