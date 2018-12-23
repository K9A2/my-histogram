package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.common.RequiredKeyNotFoundException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static main.java.com.stormlin.common.Util.*;

public class Histogram {

    private String type;

    private int width;
    private int height;

    // Upper, Bottom, Left, Right
    private double[] margins;

    // Basic components
    private HistogramTitle title;
    private HistogramAxis xaxis;
    private HistogramAxis yaxis;
    private HistogramRuler leftRuler;
    private HistogramRuler rightRuler;
    private HistogramData data;

    public Histogram(String filePath) {

        /* Load the designated JSON file to form a Histogram instance */
        try {
            System.out.println("Loading JSON file: " + filePath);

            InputStream stream = new FileInputStream(new File(filePath));
            JsonReader reader = Json.createReader(stream);
            JsonObject object = reader.readObject();

            this.type = parseRequiredString(object, "type");
            this.width = parseRequiredInt(object, "width");
            this.height = parseRequiredInt(object, "height");
            this.margins = parseRequiredDoubleArray(object, "margins");
            this.title = new HistogramTitle(object.getJsonObject("title"));
            this.xaxis = new HistogramAxis(object.getJsonObject("xaxis"));
            this.yaxis = new HistogramAxis(object.getJsonObject("yaxis"));
            this.leftRuler = new HistogramRuler(getRequiredJsonObject(object, "leftRuler"));
            if (parseRequiredBoolean(object, "hasRightRuler")) {
                this.rightRuler = new HistogramRuler(getRequiredJsonObject(object, "rightRuler"));
            }
            this.data = new HistogramData(getRequiredJsonObject(object, "data"));
        } catch (IOException | RequiredKeyNotFoundException exception) {
            System.out.println(exception.toString());
            System.exit(1);
        }
    }

    public void draw() {
        switch (this.type) {
            case Constants.SIMPLE_BAR_CHART:
                plotSimpleBarChart();
                break;
            case Constants.STACKED_BAR_CHART:
                plotStackedBarChart();
                break;
            case Constants.GROUPED_BAR_CHART:
                plotGroupedBarChart();
                break;
        }
    }

    private void plotSimpleBarChart() {

    }

    private void plotStackedBarChart() {

    }

    private void plotGroupedBarChart() {

    }

}

class HistogramTitle {
    private String text;
    private String font;
    private int fontSize;
    private String fontStyle;

    HistogramTitle(JsonObject object) {
        this.text = parseRequiredString(object, "text");
        this.font = parseString(object, "font", Constants.DEFAULT_FONT_NAME);
        this.fontSize = parseInt(object, "fontSize", Constants.DEFAULT_FONT_SIZE);
        this.fontStyle = parseString(object, "fontStyle", Constants.DEFAULT_FONT_STYLE);
    }

    public String getText() {
        return text;
    }

    public String getFont() {
        return font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontStyle() {
        return fontStyle;
    }
}

class HistogramAxis {
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

    public String getFontStyle() {
        return fontStyle;
    }
}


class HistogramRuler {
    private double min;
    private double max;
    private double step;

    HistogramRuler(JsonObject object) {
        this.min = parseRequiredDouble(object, "min");
        this.max = parseRequiredDouble(object, "max");
        this.step = parseRequiredDouble(object, "step");
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
}

class HistogramData {
    private String[] key;
    private double[] value;

    // Colors are described in hex
    private String barBodyColor;
    private String barBorderColor;

    HistogramData(JsonObject object) {
        this.key = parseRequiredStringArray(object, "key");
        this.value = parseRequiredDoubleArray(object, "value");
        this.barBodyColor = parseString(object, "barBodyColor", "#FFFFFF");
        this.barBorderColor = parseString(object, "barBorderColor", "#000000");
    }

    public String[] getKey() {
        return key;
    }

    public double[] getValue() {
        return value;
    }

    public String getBarBodyColor() {
        return barBodyColor;
    }

    public String getBarBorderColor() {
        return barBorderColor;
    }
}
