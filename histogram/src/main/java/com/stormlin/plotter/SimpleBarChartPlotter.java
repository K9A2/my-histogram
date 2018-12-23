package main.java.com.stormlin.plotter;

import main.java.com.stormlin.histogram.Histogram;

import javax.swing.*;
import java.awt.*;

public class SimpleBarChartPlotter extends JPanel implements Plotter {

    private Histogram histogram;

    public SimpleBarChartPlotter(Histogram histogram) {
        this.histogram = histogram;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(this.histogram.getBackgroundColor());

        plotBorder(this.histogram, g);

//        g.setColor(Color.BLACK);
//        g.drawLine(30, 40, 100, 200);
//        g.drawOval(150, 180, 10, 10);
//        g.drawRect(200, 210, 20, 30);
//        g.setColor(Color.RED);       // change the drawing color
//        g.fillOval(300, 310, 30, 50);
//        g.fillRect(400, 350, 60, 50);
//        // Printing texts
//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
//        g.drawString("Testing custom drawing ...", 10, 20);
    }
}

