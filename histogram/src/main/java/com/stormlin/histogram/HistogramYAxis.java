package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;

import javax.json.JsonObject;
import javax.json.JsonValue;

import static main.java.com.stormlin.common.Util.*;

public class HistogramYAxis extends HistogramFont {
    private String label;

    private double min;
    private double max;
    private double step;
    private double pointsPerUnit;

    HistogramYAxis(JsonValue value) {
        JsonObject object = value.asJsonObject();
        label = parseRequiredString(object, "label");
        fontName = parseString(object, "font", Constants.DEFAULT_FONT_NAME);
        fontSize = parseInt(object, "fontSize", Constants.DEFAULT_FONT_SIZE);
        fontStyle = parseString(object, "fontStyle", Constants.DEFAULT_FONT_STYLE);

        min = parseRequiredDouble(object, "min");
        max = parseRequiredDouble(object, "max");
        step = parseRequiredDouble(object, "step");
    }

    public String getLabel() {
        return label;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }

    public double getPointsPerUnit() {
        return pointsPerUnit;
    }

    public void setPointsPerUnit(double pointsPerUnit) {
        this.pointsPerUnit = pointsPerUnit;
    }
}

