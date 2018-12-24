package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;

import javax.json.JsonObject;

import java.awt.*;

import static main.java.com.stormlin.common.Util.*;

public class HistogramAxis {
    private String label;
    private String font;
    private int fontSize;
    private String fontStyle;

    HistogramAxis(JsonObject object) {
        this.label = parseRequiredString(object, "label");
        this.font = parseString(object, "font", Constants.DEFAULT_FONT_NAME);
        this.fontSize = parseInt(object, "fontSize", Constants.DEFAULT_FONT_SIZE);
        this.fontStyle = parseString(object, "fontStyle", Constants.DEFAULT_FONT_STYLE);
    }

    public String getLabel() {
        return label;
    }

    public String getFont() {
        return font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getFontStyle() {
        switch (fontStyle) {
            case "PLAIN": return Font.PLAIN;
            case "BOLD": return Font.BOLD;
            case "ITALIC": return Font.ITALIC;
        }
        return Font.PLAIN;
    }
}

