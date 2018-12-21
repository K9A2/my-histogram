package main;

import java.awt.*;
import java.util.ArrayList;

public class HistogramDefault {

    public static final String DEFAULT_DIR = "./data/";

    // Default size of a 2D canvas
    public static final int SIZE_X = 1000;
    public static final int SIZE_Y = 1000;

    // Default color for foreground and background
    public static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

    // Default color array, represented in hex values
    public static final String[] DEFAULT_COLOR_ARRAY = {
            "#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd",
            "#8c564b", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"
    };
    // Default color list, represented in <Color> class
    public static final ArrayList<Color> DEFAULT_COLOR_LIST = new ArrayList<Color>() {{
        add(Color.decode("#1f77b4"));
        add(Color.decode("#ff7f0e"));
        add(Color.decode("#2ca02c"));
        add(Color.decode("#d62728"));
        add(Color.decode("#9467bd"));
        add(Color.decode("#8c564b"));
        add(Color.decode("#e377c2"));
        add(Color.decode("#7f7f7f"));
        add(Color.decode("#bcbd22"));
        add(Color.decode("#17becf"));
    }};

}
