package main.java.com.stormlin.histogram;

import java.awt.*;

public class HistogramFont {

    String fontName;
    int fontSize;
    String fontStyle;

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFontStyle() {
        switch (fontStyle) {
            case "PLAIN": return Font.PLAIN;
            case "BOLD" : return Font.BOLD;
            case "ITALIC": return Font.ITALIC;
        }
        return Font.PLAIN;
    }
}
