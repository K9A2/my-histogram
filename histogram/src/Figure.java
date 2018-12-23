import main.java.com.stormlin.histogram.Histogram;

import javax.swing.*;

public class Figure extends JFrame {

    public static void main(String[] args) {
        String filePath = "./data/" + args[0];
        Histogram histogram = new Histogram(filePath);
        SwingUtilities.invokeLater(histogram::draw);
    }

}
