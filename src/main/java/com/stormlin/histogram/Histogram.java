package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;
import main.java.com.stormlin.common.RequiredKeyNotFoundException;
import main.java.com.stormlin.plotter.AnimatedGroupedBarChart;
import main.java.com.stormlin.plotter.GroupedBarChartPlotter;
import main.java.com.stormlin.plotter.SimpleBarChartPlotter;
import main.java.com.stormlin.plotter.StackedBarChartPlotter;

import javax.json.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private HistogramXAxis xAxis;
    private ArrayList<HistogramYAxis> histogramYAxisList;
    private HistogramLegend legend;

    private double plotAreaX;
    private double plotAreaY;
    private double plotAreaWidth;
    private double plotAreaHeight;

    public String getHistogramType() {
        return this.type;
    }

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

    public HistogramXAxis getXAxis() {
        return xAxis;
    }

    public ArrayList<HistogramYAxis> getyYAxisList() {
        return histogramYAxisList;
    }

    public HistogramLegend getLegend() {
        return legend;
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
            xAxis = new HistogramXAxis(object.getJsonObject("xAxis"));

            JsonArray yAxisArray = getRequiredObjectArray(object, "yAxis");
            histogramYAxisList = new ArrayList<>();
            for (JsonValue value : yAxisArray) {
                histogramYAxisList.add(new HistogramYAxis(value));
            }

            /* Calculate parameters for plot area */
            plotAreaX = width * margins[Constants.MARGIN_LEFT];
            plotAreaY = height * margins[Constants.MARGIN_UPPER];
            plotAreaWidth = width * (1 - margins[Constants.MARGIN_LEFT] - margins[Constants.MARGIN_RIGHT]);
            plotAreaHeight = height * (1 - margins[Constants.MARGIN_UPPER] - margins[Constants.MARGIN_BOTTOM]);

            /* Left ruler is mandatory, while right ruler is optional */
            HistogramYAxis leftAxis = histogramYAxisList.get(0);
            histogramYAxisList.get(0).setPointsPerUnit(getPlotAreaHeight() / (leftAxis.getMax() - leftAxis.getMin()));
            if (histogramYAxisList.size() > 1) {
                HistogramYAxis rightAxis = histogramYAxisList.get(1);
                rightAxis.setPointsPerUnit(getPlotAreaHeight() / (rightAxis.getMax() - rightAxis.getMin()));
            }

            /* The legend */
            legend = new HistogramLegend(getOptionalJsonObject(object, "legend"));
        } catch (IOException | RequiredKeyNotFoundException exception) {
            System.out.println(exception.toString());
            System.exit(1);
        }
    }

    public void draw() {
        Container container = getContentPane();
        switch (this.type) {
            case Constants.SIMPLE_BAR_CHART:
                System.out.println("SimpleBarChart");
                plotSimpleBarChart(container);
                break;
            case Constants.GROUPED_BAR_CHART:
                System.out.println("GroupedBarChart");
                plotGroupedBarChart(container);
                break;
            case Constants.STACKED_BAR_CHART:
                System.out.println("StackedBarChart");
                plotStackedBarChart(container);
                break;
            case Constants.ANIMATED_GROUPED_BAR_CHART:
                System.out.println("AnimatedGroupedBarChart");
                plotAnimatedGroupedBarChart(container);
        }
    }

    private void plotSimpleBarChart(Container container) {
        SimpleBarChartPlotter plotter = new SimpleBarChartPlotter(this);
        plotter.setPreferredSize(new Dimension(this.width, this.height));
        container.add(plotter);
        setupCanvas();
    }

    private void plotGroupedBarChart(Container container) {
        GroupedBarChartPlotter plotter = new GroupedBarChartPlotter(this);
        plotter.setPreferredSize(new Dimension(this.width, this.height));
        container.add(plotter);
        setupCanvas();
    }

    private void plotStackedBarChart(Container container) {
        StackedBarChartPlotter plotter = new StackedBarChartPlotter(this);
        plotter.setPreferredSize(new Dimension(this.width, this.height));
        container.add(plotter);
        setupCanvas();
    }

    private void plotAnimatedGroupedBarChart(Container container) {
        AnimatedGroupedBarChart plotter = new AnimatedGroupedBarChart(this);
        plotter.setPreferredSize(new Dimension(this.width, this.height));
        container.add(plotter);
        setupCanvas();

        Timer animationTimer = new Timer(500, new ActionListener() {
            private int clock = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                plotter.repaint();
                plotter.nextFrame();
                clock++;
                if (clock == 5) {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        animationTimer.setRepeats(true);
        animationTimer.start();
    }

    private void setupCanvas() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setTitle(this.title.getText());
        setVisible(true);
    }
}
