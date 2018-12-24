package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;

import javax.json.JsonObject;

import static main.java.com.stormlin.common.Util.*;

public class HistogramXAxis extends HistogramFont {

    private String label;

    HistogramXAxis(JsonObject object) {
        label = parseRequiredString(object, "label");
        fontName = parseString(object, "font", Constants.DEFAULT_FONT_NAME);
        fontSize = parseInt(object, "fontSize", Constants.DEFAULT_FONT_SIZE);
        fontStyle = parseString(object, "fontStyle", Constants.DEFAULT_FONT_STYLE);
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
