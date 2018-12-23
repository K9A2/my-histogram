package main.java.com.stormlin.common;

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

    /* Use methods below to parse required or non-required fields from given JSON object */

    /**
     * Get the required String representation specified by a key from designated JSON object
     *
     * @param object A designated JSON object
     * @param key    The key of this String field
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static String parseRequiredString(JsonObject object, String key) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            throw new RequiredKeyNotFoundException("String", "key");
        }
        return object.getString(key);
    }

    /**
     * Get the String representation specified by a key from designated JSON object
     *
     * @param object       A designated JSON object
     * @param key          The key of this String field
     * @param defaultValue The default value for this field
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static String parseString(JsonObject object, String key, String defaultValue) {
        if (!object.containsKey(key)) {
            return defaultValue;
        }
        return object.getString(key);
    }

    /**
     * Returns required a double array composed with values in input JSON object, or return the default value if this
     * field is absent in JSON object
     *
     * @param object A designated JSON object
     * @param key    The key of this double array
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static double[] parseRequiredDoubleArray(JsonObject object, String key) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            throw new RequiredKeyNotFoundException("double[]", key);
        }
        return jsonArrayToDoubleArray(object.getJsonArray(key));
    }

    /**
     * Returns a double array composed with values in input JSON object, or return the default value if this
     * field is absent in JSON object
     *
     * @param object       A designated JSON object
     * @param key          The key of this double array
     * @param defaultValue The default value for this field
     * @return The value of this field
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static double[] parseDoubleArray(JsonObject object, String key, double[] defaultValue) {
        if (!object.containsKey(key)) {
            return defaultValue;
        }
        return jsonArrayToDoubleArray(object.getJsonArray(key));
    }

    /**
     * Returns required a String array composed with values in input JSON object, or return the default value if this
     * field is absent in JSON object
     *
     * @param object A designated JSON object
     * @param key    The key of this double array
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static String[] parseRequiredStringArray(JsonObject object, String key) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            throw new RequiredKeyNotFoundException("String[]", key);
        }
        return jsonArrayToStringArray(object.getJsonArray(key));
    }

    /**
     * Returns a String array composed with values in input JSON object, or return the default value if this field is
     * absent in JSON object
     *
     * @param object       A designated JSON object
     * @param key          The key of this double array
     * @param defaultValue The default of this field
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static String[] parseStringArray(JsonObject object, String key, String[] defaultValue) {
        if (!object.containsKey(key)) {
            return defaultValue;
        }
        return jsonArrayToStringArray(object.getJsonArray(key));
    }

    /**
     * Returns some Color field specified by a key in this JSON object
     *
     * @param jsonObject   input JSON object
     * @param key          the key of this color
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
     * Returns required the value of this boolean field specified by a key in this JSON object
     *
     * @param jsonObject input JSON object
     * @param key        the key of this boolean field
     * @return the value of this field
     */
    public static boolean parseRequiredBoolean(JsonObject jsonObject, String key) throws RequiredKeyNotFoundException {
        if (!jsonObject.containsKey(key)) {
            throw new RequiredKeyNotFoundException("boolean", key);
        }
        return jsonObject.getBoolean(key);
    }

    /**
     * Returns the value of this boolean field specified by a key in this JSON object
     *
     * @param jsonObject   input JSON object
     * @param key          the key of this boolean field
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
     * Returns required the value of this field in double representation specified by a key and stored in given JSON object
     *
     * @param jsonObject given JSON object
     * @param key        the key of this field
     * @return the value of this field
     */
    public static double parseRequiredDouble(JsonObject jsonObject, String key) throws RequiredKeyNotFoundException {
        if (!jsonObject.containsKey(key)) {
            throw new RequiredKeyNotFoundException("double", key);
        }
        return jsonObject.getJsonNumber(key).doubleValue();
    }

    /**
     * Returns the value of this field in double representation specified by a key and stored in given JSON object
     *
     * @param jsonObject   given JSON object
     * @param key          the key of this field
     * @param defaultValue the default value of this double field
     * @return the value of this field
     */
    public static double parseDouble(JsonObject jsonObject, String key, double defaultValue) {
        if (!jsonObject.containsKey(key)) {
            return defaultValue;
        }
        return jsonObject.getJsonNumber(key).doubleValue();
    }

    /**
     * Get the required int representation specified by a key from designated JSON object
     *
     * @param object A designated JSON object
     * @param key    The key of this String field
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static int parseRequiredInt(JsonObject object, String key) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            throw new RequiredKeyNotFoundException("int", "key");
        }
        return object.getJsonNumber(key).intValue();
    }

    /**
     * Get the int representation specified by a key from designated JSON object
     *
     * @param object       A designated JSON object
     * @param key          The key of this String field
     * @param defaultValue The default value for this field
     * @return The value
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static int parseInt(JsonObject object, String key, int defaultValue) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            return defaultValue;
        }
        return object.getJsonNumber(key).intValue();
    }

    /**
     * Get a required JSON object from input JSON object
     *
     * @param object Input JSON object
     * @param key    The key of this required object
     * @return This required JSON object
     * @throws RequiredKeyNotFoundException If this field is not exists, throw this exception
     */
    public static JsonObject getRequiredJsonObject(JsonObject object, String key) throws RequiredKeyNotFoundException {
        if (!object.containsKey(key)) {
            throw new RequiredKeyNotFoundException("JSON object", key);
        }
        return object.getJsonObject(key);
    }

}
