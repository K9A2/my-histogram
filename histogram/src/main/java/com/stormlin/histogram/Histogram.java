package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.common.RequiredKeyNotFoundException;
import main.java.com.stormlin.plotter.SimpleBarChartPlotter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static main.java.com.stormlin.common.Util.*;

public class Histogram extends JFrame {

    private String type;

    private int width;
    private int height;

    // Upper, Bottom, Left, Right
    private double[] margins;

    private Color foregroundColor;
    private Color backgroundColor;

    // Basic components
    private HistogramTitle title;
    private HistogramAxis xaxis;
    private HistogramAxis yaxis;
    private HistogramRuler leftRuler;
    private HistogramRuler rightRuler;
    private HistogramData data;

    public int getCanvasWidth() {
        return this.width;
    }

    public int getCanvasHeight() {
        return this.height;
    }

    public double[] getMargins() {
        return margins;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

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
            this.foregroundColor = Color.decode(parseString(object, "foregroundColor",
                    Constants.DEFAULT_FOREGROUND_COLOR));
            this.backgroundColor = Color.decode(parseString(object, "backgroundColor",
                    Constants.DEFAULT_BACKGROUND_COLOR));
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
        Container container = getContentPane();
        switch (this.type) {
            case Constants.SIMPLE_BAR_CHART:
                plotSimpleBarChart(container);
                break;
            case Constants.GROUPED_BAR_CHART:
                plotGroupedBarChart();
                break;
            case Constants.STACKED_BAR_CHART:
                plotStackedBarChart();
                break;
        }
    }

    private void plotSimpleBarChart(Container container) {
        SimpleBarChartPlotter plotter = new SimpleBarChartPlotter(this);
        plotter.setPreferredSize(new Dimension(this.width, this.height));
        container.add(plotter);
        setupCanvas();
    }

    private void plotGroupedBarChart() {

    }

    private void plotStackedBarChart() {

    }

    private void setupCanvas() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setTitle(this.title.getText());
        setVisible(true);
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

    // Colors are described in hex, such as "#FFFFFF"
    private String barBodyColor;
    private String barBorderColor;

    HistogramData(JsonObject object) {
        this.key = parseRequiredStringArray(object, "key");
        this.value = parseRequiredDoubleArray(object, "value");
        this.barBodyColor = parseString(object, "barBodyColor", Constants.DEFAULT_BACKGROUND_COLOR);
        this.barBorderColor = parseString(object, "barBorderColor", Constants.DEFAULT_FOREGROUND_COLOR);
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
