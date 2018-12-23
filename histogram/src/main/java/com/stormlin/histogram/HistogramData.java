package main.java.com.stormlin.histogram;

import main.java.com.stormlin.common.Constants;

import javax.json.JsonValue;
import java.awt.*;

import static main.java.com.stormlin.common.Util.*;

public class HistogramData {
    private String[] key;
    private double[] value;

    private Color barBackgroundColor;
    private Color barBorderColor;

    public String[] getKey() {
        return key;
    }

    public double[] getValue() {
        return value;
    }

    public Color getBarBackgroundColor() {
        return barBackgroundColor;
    }

    public Color getBarBorderColor() {
        return barBorderColor;
    }

    HistogramData(JsonValue jsonValue) {
        key = parseRequiredStringArray(jsonValue.asJsonObject(), "key");
        value = parseRequiredDoubleArray(jsonValue.asJsonObject(), "value");
        barBackgroundColor = Color.decode(parseString(jsonValue.asJsonObject(), "barBackgroundColor",
                convertColorToHexString(Constants.DEFAULT_FOREGROUND_COLOR)));
        barBorderColor = Color.decode(parseString(jsonValue.asJsonObject(), "barBorderColor",
                convertColorToHexString(Constants.DEFAULT_BACKGROUND_COLOR)));
    }

}
