package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;

import javax.json.JsonObject;

import static main.java.com.stormlin.common.Util.*;

public class HistogramTitle extends HistogramFont {
    private String text;

    public HistogramTitle(JsonObject object) {
        text = parseRequiredString(object, "text");
        fontName = parseString(object, "fontName", Constants.DEFAULT_FONT_NAME);
        fontSize = parseInt(object, "fontSize", Constants.DEFAULT_FONT_SIZE);
        fontStyle = parseString(object, "fontStyle", Constants.DEFAULT_FONT_STYLE);
    }

    public String getText() {
        return text;
    }
}

