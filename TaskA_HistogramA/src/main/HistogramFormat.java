package main;

import java.awt.Color;

public class HistogramFormat {

    /* Parameters and their default values */

    // UPPER, BOTTOM, LEFT, RIGHT margins
    public double[] margins = {0.15, 0.15, 0.1, 0.05};

    public boolean isBarFilled = true;
    public Color barFillColor = Color.BLACK;

    public boolean hasBarFrame = true;
    public Color barFrameColor = Color.BLACK;

    public boolean hasBorder = true;
    public Color borderColor = Color.BLACK;

    public Color rulerColor = Color.BLACK;
    public Color rulerMarkColor = Color.BLACK;
    public boolean hasRightRuler = true;

    public Color keyColor = Color.BLACK;

    public boolean hasHeader = true;
    public Color headerColor = Color.BLACK;

    public boolean hasFooter = true;
    public Color footerColor = Color.BLACK;
}

