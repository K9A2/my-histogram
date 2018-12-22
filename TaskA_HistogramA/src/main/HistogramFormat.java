package main;

import java.awt.Color;

public class HistogramFormat {

    /* Parameters and their default values */

    // UPPER, BOTTOM, LEFT, RIGHT margins, in 0.0 to 1.0, relative to the whole figure
    public double[] margins = {0.15, 0.15, 0.1, 0.05};

    public boolean isBarFilled = true;
    public Color barFillColor = Color.BLACK;

    public boolean hasBarBorder = true;
    public Color barBorderColor = Color.BLACK;

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

