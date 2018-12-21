package test;

import main.*;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static util.Util.*;

public class HistogramATest {

    /**
     * Returns a HistogramA instance based on the parameters specified in the designated JSON file or system default value
     *
     * @param fileName the file name of designated JSON file
     * @return a HistogramA instance
     */
    private static HistogramA createHistogramAFromJsonFile(String fileName) {

        HistogramA histogram = null;

        /* Load data and parameters from designated JSON file */
        try (
                InputStream is = new FileInputStream(new File(HistogramDefault.DEFAULT_DIR + fileName));
                JsonReader reader = Json.createReader(is)
        ) {
            System.out.println("Loading " + HistogramDefault.DEFAULT_DIR + fileName);
            JsonObject obj = reader.readObject().getJsonObject("histogram");

            // Retrieve data and parameters
            HistogramCanvas canvas = getCanvasFrom(obj.getJsonObject("canvas"));
            HistogramFormat format = getFormatsFrom(obj.getJsonObject("formats"));
            HistogramData data = getDataFrom(obj.getJsonObject("data"));

            // Initialize a HistogramA instance, and plot within it
            histogram = new HistogramA(canvas, format, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return histogram;
    }

    /**
     * Returns a HistogramCanvas instance according to settings stored in JSON object
     *
     * @param jsonObject JSON object includes the customized settings
     * @return A HistogramCanvas instance
     */
    private static HistogramCanvas getCanvasFrom(JsonObject jsonObject) {

        HistogramCanvas canvas = new HistogramCanvas();

        /* Set the size of canvas */
        JsonArray sizeArray = jsonObject.getJsonArray("size");
        if (sizeArray != null) {
            // otherwise, use the default size
            int[] size = jsonArrayToIntArray(sizeArray);
            canvas.x = size[0];
            canvas.y = size[1];
        }

        // TODO: What xScale and yScale mean?
        canvas.xScale = parseDoubleArray(jsonObject, "xScale", canvas.xScale);
        canvas.yScale = parseDoubleArray(jsonObject, "yScale", canvas.yScale);

        canvas.backgroundColor = parseColor(jsonObject, "backgroundColor", canvas.backgroundColor);
        canvas.foregroundColor = parseColor(jsonObject, "foregroundColor", canvas.foregroundColor);

        return canvas;
    }

    /**
     * Returns a HistogramFormat instance according to settings stored in JSON object
     *
     * @param jsonObject JSON object includes the customized settings
     * @return A HistogramFormat instance
     */
    private static HistogramFormat getFormatsFrom(JsonObject jsonObject) {

        HistogramFormat format = new HistogramFormat();

        // default: [ 0.15, 0.25, 0.1, 0.05 ]
        format.margins = parseDoubleArray(jsonObject, "margins", format.margins);

        // default: true
        format.isBarFilled = parseBoolean(jsonObject, "isBarFilled", format.isBarFilled);
        // default: Color.BLACK
        format.barFillColor = parseColor(jsonObject, "barFillColor", HistogramDefault.DEFAULT_COLOR_LIST.get(0));

        // default: true
        format.hasBarBorder = parseBoolean(jsonObject, "hasBarBorder", format.hasBarBorder);
        // default: Color.BLACK
        format.barBorderColor = parseColor(jsonObject, "barBorderColor", HistogramDefault.DEFAULT_COLOR_LIST.get(0));

        // default: true
        format.hasRightRuler = parseBoolean(jsonObject, "hasRightRuler", format.hasRightRuler);
        // default: Color.BLACK
        format.rulerColor = parseColor(jsonObject, "rulerColor", Color.BLACK);
        // default: Color.BLACK
        format.rulerMarkColor = parseColor(jsonObject, "rulerMarkColor", format.rulerMarkColor);

        // default: true
        format.hasBorder = parseBoolean(jsonObject, "hasBorder", format.hasBorder);
        // default: Color.BLACK
        format.borderColor = parseColor(jsonObject, "borderColor", Color.BLACK);

        // default: Color.BLACK
        format.keyColor = parseColor(jsonObject, "keyColor", Color.BLACK);

        // default: true
        format.hasHeader = parseBoolean(jsonObject, "hasHeader", format.hasHeader);
        // default: Color.BLACK
        format.headerColor = parseColor(jsonObject, "headerColor", Color.BLACK);

        // default: true
        format.hasFooter = parseBoolean(jsonObject, "hasFooter", format.hasFooter);
        // default: Color.BLACK
        format.footerColor = parseColor(jsonObject, "footerColor", Color.BLACK);

        return format;
    }

    /**
     * Returns a HistogramData instance according to settings stored in JSON object
     *
     * @param jsonObject JSON object includes the customized settings
     * @return A HistogramData instance
     */
    private static HistogramData getDataFrom(JsonObject jsonObject) {

        HistogramData data = new HistogramData();

        data.header = jsonObject.getString("header", "");
        data.footer = jsonObject.getString("footer", "");
        data.values = jsonArrayToDoubleArray(jsonObject.getJsonArray("values"));
        data.minValue = parseDouble(jsonObject, "minvalue", 0.0);
        data.keys = jsonArrayToStringArray(jsonObject.getJsonArray("keys"));

        return data;
    }

    public static void main(String[] args) {
        HistogramA histogram = createHistogramAFromJsonFile(args[0]);
        histogram.draw();
    }

}
