package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.common.RequiredKeyNotFoundException;
import main.java.com.stormlin.plotter.SimpleBarChartPlotter;

import javax.json.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
    private ArrayList<HistogramData> histogramDataList;
    private HistogramAxis xAxis;
    private HistogramAxis yAxis;
    private HistogramRuler leftRuler;
    private HistogramRuler rightRuler = null;

    private double plotAreaX;
    private double plotAreaY;
    private double plotAreaWidth;
    private double plotAreaHeight;

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

    public HistogramTitle getHistogramTitle() {
        return title;
    }

    public ArrayList<HistogramData> getHistogramDataList() {
        return histogramDataList;
    }

    public HistogramAxis getxAxis() {
        return xAxis;
    }

    public HistogramAxis getyAxis() {
        return yAxis;
    }

    public HistogramRuler getLeftRuler() {
        return leftRuler;
    }

    public HistogramRuler getRightRuler() {
        return rightRuler;
    }

    public double getPlotAreaX() {
        return plotAreaX;
    }

    public double getPlotAreaY() {
        return plotAreaY;
    }

    public double getPlotAreaWidth() {
        return plotAreaWidth;
    }

    public double getPlotAreaHeight() {
        return plotAreaHeight;
    }

    public Histogram(String filePath) {

        /* Set up the basic parameters for this histogram */
        try {
            System.out.println("Loading JSON file: " + filePath);

            InputStream stream = new FileInputStream(new File(filePath));
            JsonReader reader = Json.createReader(stream);
            JsonObject object = reader.readObject();

            /* Load data */
            type = parseRequiredString(object, "type");
            width = parseRequiredInt(object, "width");
            height = parseRequiredInt(object, "height");
            margins = parseRequiredDoubleArray(object, "margins");
            title = new HistogramTitle(object.getJsonObject("title"));

            JsonArray dataArray = getRequiredObjectArray(object, "data");
            histogramDataList = new ArrayList<>();
            for (JsonValue jsonValue : dataArray) {
                histogramDataList.add(new HistogramData(jsonValue));
            }

            foregroundColor = Color.decode(parseString(object, "foregroundColor",
                    convertColorToHexString(Constants.DEFAULT_FOREGROUND_COLOR)));
            backgroundColor = Color.decode(parseString(object, "backgroundColor",
                    convertColorToHexString(Constants.DEFAULT_BACKGROUND_COLOR)));
            xAxis = new HistogramAxis(object.getJsonObject("xAxis"));
            yAxis = new HistogramAxis(object.getJsonObject("yAxis"));

            /* Left ruler is mandatory, while right ruler is optional */
            leftRuler = new HistogramRuler(getRequiredJsonObject(object, "leftRuler"));
            if (parseRequiredBoolean(object, "hasRightRuler")) {
                rightRuler = new HistogramRuler(getRequiredJsonObject(object, "rightRuler"));
            }

            /* Calculate parameters for plot area */
            plotAreaX = width * margins[Constants.MARGIN_LEFT];
            plotAreaY = height * margins[Constants.MARGIN_UPPER];
            plotAreaWidth = width * (1 - margins[Constants.MARGIN_LEFT] - margins[Constants.MARGIN_RIGHT]);
            plotAreaHeight = height * (1 - margins[Constants.MARGIN_UPPER] - margins[Constants.MARGIN_BOTTOM]);
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

