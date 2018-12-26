package main.java.com.stormlin.histogram;

import javax.json.JsonObject;

import static main.java.com.stormlin.common.Util.*;

public class HistogramLegend extends HistogramFont {

    private String position;

    public HistogramLegend(JsonObject object) {
        this.position = parseRequiredString(object, "position");
        this.fontName = parseString(object, "fontName", "Consolas");
        this.fontSize = parseInt(object, "fontSize", 32);
        this.fontStyle = parseString(object, "fontStyle", "PLAIN");
    }

    public String getPosition() {
        return position;
    }
}
