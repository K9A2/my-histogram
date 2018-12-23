package main.java.com.stormlin.histogram;

import javax.json.JsonObject;

import static main.java.com.stormlin.common.Util.parseRequiredDouble;

public class HistogramRuler {
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
