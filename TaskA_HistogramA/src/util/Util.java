package util;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.awt.*;

public class Util {

    /**
     * Returns an integer array based on given JSON array
     *
     * @param array JSON array
     * @return An integer number array
     */
    public static int[] jsonArrayToIntArray(JsonArray array) {
        int[] intArray = new int[array.size()];
        for (int i = 0; i < array.size(); i++) {
            intArray[i] = array.getInt(i);
        }
        return intArray;
    }

    /**
     * Returns a double array based on given JSON array
     *
     * @param array JSON array
     * @return A double number array
     */
    public static double[] jsonArrayToDoubleArray(JsonArray array) {
        double[] doubleArray = new double[array.size()];
        for (int i = 0; i < array.size(); i++) {
            doubleArray[i] = array.getJsonNumber(i).doubleValue();
        }
        return doubleArray;
    }

    /**
     * Returns a String array based on given JSON array
     *
     * @param array JSON array
     * @return A String array
     */
    public static String[] jsonArrayToStringArray(JsonArray array) {
        String[] stringArray = new String[array.size()];
        for (int i = 0; i < array.size(); i++) {
            stringArray[i] = array.getString(i);
        }
        return stringArray;
    }

    /**
     * Get foregroundColor from JSON object
     *
     * @param array Color defined as RGB values in a JSON array
     * @return Pre-defined Java Color
     */
    private static Color getColorFrom(JsonArray array) {
        int[] c = jsonArrayToIntArray(array);
        return new Color(c[0], c[1], c[2]);
    }

    /**
     * Returns a double array composed with values in input JSON object, or return the default value if this field is
     * absent in JSON object
     *
     * @param jsonObject input JSON object
     * @param key the key of array
     * @param defaultValue the default value of this array
     * @return the value of given key
     */
    public static double[] parseDoubleArray(JsonObject jsonObject, String key, double[] defaultValue) {
        if (!jsonObject.containsKey(key)) {
            return defaultValue;
        }
        return jsonArrayToDoubleArray(jsonObject.getJsonArray(key));
    }

    /**
     * Returns some Color field specified by a key in this JSON object
     *
     * @param jsonObject input JSON object
     * @param key the key of this color
     * @param defaultColor the default value of this field
     * @return the Color of this field
     */
    public static Color parseColor(JsonObject jsonObject, String key, Color defaultColor) {
        if (!jsonObject.containsKey(key)) {
            return defaultColor;
        }
        return getColorFrom(jsonObject.getJsonArray(key));
    }

    /**
     * Returns the value of this boolean field specified by a key in this JSON object
     *
     * @param jsonObject input JSON object
     * @param key the key of this boolean field
     * @param defaultValue the default value of this field
     * @return the value of this field
     */
    public static boolean parseBoolean(JsonObject jsonObject, String key, boolean defaultValue) {
        if (!jsonObject.containsKey(key)) {
            return defaultValue;
        }
        return jsonObject.getBoolean(key);
    }

    /**
     * Returns the value of this field in double representation specified by a key and stored in given JSON object
     *
     * @param jsonObject given JSON object
     * @param key the key of this field
     * @param defaultValue the default value of this double field
     * @return the value of this field
     */
    public static double parseDouble(JsonObject jsonObject, String key, double defaultValue) {
        if (!jsonObject.containsKey(key)) {
            return defaultValue;
        }
        return jsonObject.getJsonNumber(key).doubleValue();
    }

}
