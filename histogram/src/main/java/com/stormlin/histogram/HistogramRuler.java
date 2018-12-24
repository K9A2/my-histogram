package main.java.com.stormlin.histogram;

import javax.json.JsonObject;

import static main.java.com.stormlin.common.Util.parseRequiredDouble;

public class HistogramRuler {
    private double min;
    private double max;
    private double step;

    private double pointsPerUnit;

    HistogramRuler(JsonObject object) {
        min = parseRequiredDouble(object, "min");
        max = parseRequiredDouble(object, "max");
        step = parseRequiredDouble(object, "step");
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

    void setPointsPerUnit(double pointsPerUnit) {
        this.pointsPerUnit = pointsPerUnit;
    }
}
