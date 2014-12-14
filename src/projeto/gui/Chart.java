package projeto.gui;

/**
 *
 * The MIT License
 *
 * Copyright (c) 2008 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import static com.googlecode.charts4j.Color.*;
import static com.googlecode.charts4j.UrlUtil.normalize;
import static org.junit.Assert.assertEquals;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;

/**
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
public class Chart {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Logger.global.setLevel(Level.ALL);
    }

    private static String[] colors = {"CA3D05","00FF00","FF3399","0000CC","FF8000","4C0099","FFFF33","E0E0E0"};
    private int colorsIndex;

    public HashMap<String, ArrayList<Double>> chartLines = new HashMap<>();
    public String player;

    private JFrame frame;

    public Chart(String player){
        this.player = player;

        frame = new JFrame();
        frame.pack();
        frame.setVisible(false);
    }

    public void addLine(ArrayList<Double> values, String source, String category){
        String sourceCategory = source.split("@")[0]+"_"+category;

        try{//validate new data
            DataUtil.scale(values);
            chartLines.put(sourceCategory, values);
        } catch (IllegalArgumentException e){
            //Don't add it
        }
    }

    public void draw(){


        colorsIndex = 0;

        ArrayList<Line> lines= new ArrayList<>();

        for (Iterator i = chartLines.entrySet().iterator(); i.hasNext(); ) {
            HashMap.Entry pairs = (HashMap.Entry) i.next();
            String sourceCategory = (String) pairs.getKey();
            ArrayList<Double> values = (ArrayList<Double>) pairs.getValue();

            //define line
            String color = colors[colorsIndex];
            colorsIndex = (colorsIndex<colors.length-1) ? colorsIndex+1 : colorsIndex;
            Line line = Plots.newLine(DataUtil.scale(values), Color.newColor(color), sourceCategory);
            line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
            line.addShapeMarkers(Shape.DIAMOND, Color.newColor(color), 12);
            line.addShapeMarkers(Shape.DIAMOND, Color.WHITE, 8);
            lines.add(line);

        }

        //define chart
        LineChart chart = GCharts.newLineChart(lines);
        chart.setSize(600, 500);
        chart.setTitle(player, WHITE, 14);
        chart.setGrid(25, 25, 3, 2);

        //define axis
        AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);

        AxisLabels xAxis = AxisLabelsFactory.newAxisLabels("", "", "", "", "");
        xAxis.setAxisStyle(axisStyle);

        AxisLabels yAxis = AxisLabelsFactory.newAxisLabels("", "", "", "", "");
        yAxis.setAxisStyle(axisStyle);

        AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Time", 50.0);
        xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));


        AxisLabels yAxis2 = AxisLabelsFactory.newAxisLabels("Hits", 50.0);
        yAxis2.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));

        // Adding axis info to chart.
        chart.addXAxisLabels(xAxis);
        chart.addXAxisLabels(xAxis3);
        chart.addYAxisLabels(yAxis);
        chart.addYAxisLabels(yAxis2);

        // Defining background and chart fills.
        chart.setBackgroundFill(Fills.newSolidFill(Color.newColor("1F1D1D")));
        LinearGradientFill fill = Fills.newLinearGradientFill(0, Color.newColor("363433"), 100);
        fill.addColorAndOffset(Color.newColor("2E2B2A"), 0);
        chart.setAreaFill(fill);
        String url = chart.toURLString();

        try {
            JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(url))));
            frame.getContentPane().removeAll();
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            //openWebpage(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void openWebpage(URL url) {
        try {
            URI uri = new URI(url.getProtocol(),url.getAuthority(),url.getPath(),url.getQuery(),url.getRef());
            openWebpage(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}


