package com.metrologica.ing.util;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.metrologica.ing.model.Measures;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphicUtil {

    BufferedImage grafica = null;

    @Autowired
    private ResourceLoader resourceLoader;

    public static BufferedImage SpiderWebChart(List<Measures> measures, String title, String equipment, String pattern, String error) throws IOException {

        CategoryDataset dataset = createDataset(measures, equipment, pattern, error);
        JFreeChart jfreechart = createChart(dataset, title);
        //guardar imagen
        BufferedImage image = jfreechart.createBufferedImage(170, 210);
        ImageIO.write(image, "png", new File("img.png"));
        return image;
    }

    private static CategoryDataset createDataset(List<Measures> measures, String equipment, String pattern, String error) {
        if (measures != null) {
            //dataset.addValue(valor, "Categoría", "Eje");
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            String blue = equipment;
            String green = pattern;
            String red = error;
            for (int i = 0; i < measures.size(); i++) {
                String position = String.valueOf(i + 1);
                dataset.addValue(measures.get(i).getError(), red, position);
                dataset.addValue(measures.get(i).getPattern(), blue, position);
                dataset.addValue(measures.get(i).getEquipmentH(), green, position);
            }
            return dataset;
        }
        return null;
    }

    private static JFreeChart createChart(CategoryDataset dataset, String title) {
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setLabelFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 8));
        plot.setAxisLinePaint(Color.lightGray); // linea ejes internos grafica
        plot.setBackgroundPaint(Color.WHITE); // color del fondo de la grafica
        plot.setOutlinePaint(Color.white); // borde de la grafica
        plot.setLabelPaint(Color.darkGray); // color numeros grafica
        plot.setOutlineVisible(true);

//        plot.setBaseSeriesOutlinePaint(Color.CYAN);
        //plot.setSeriesOutlinePaint(Color.BLUE); // puntos referentes de color
//        plot.setSeriesPaint(Color.CYAN);
//        plot.setLabelFont(new Font("Arial", Font.BOLD, 8));
//        plot.setHeadPercent(5);

        //agregar stilos de titulo
        TextTitle textTitle = new TextTitle();
        textTitle.setText(title);
        textTitle.setPadding(4,2,4,2);
        textTitle.setBackgroundPaint(Color.WHITE);
        textTitle.setFont(new Font("arial", Font.BOLD, 12));
        JFreeChart chart = new JFreeChart(textTitle.getText(), textTitle.getFont(), plot, false);
        chart.setBorderPaint(Color.lightGray);
        chart.setBackgroundPaint(Color.WHITE);

        //agregar subtitulo
        LegendTitle legendtitle = new LegendTitle(plot);
        legendtitle.setPosition(RectangleEdge.TOP);
        legendtitle.setItemFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 8));
        legendtitle.setPadding(1,1,1,1);
        legendtitle.setItemPaint(Color.GRAY); // letra de los item
        legendtitle.setItemLabelPadding(new RectangleInsets(4,4,4,4));//items
        legendtitle.setLegendItemGraphicEdge(RectangleEdge.opposite(RectangleEdge.BOTTOM));
        legendtitle.setLegendItemGraphicPadding(new RectangleInsets(8,8,8,8));//grafica
        legendtitle.setBorder(0.1,0.1,0.1,0.1);//borde de los items
//        legendtitle.setMargin(1,1,1,1);
        chart.addSubtitle(legendtitle);

        return chart;
    }


    public static BufferedImage createImage(List<Measures> measures, String title) throws IOException {
        //XYSeries es una clase que viene con el paquete JFreeChart
        //funciona como un arreglo con un poco mas de posibilidades

        XYSeries series = new XYSeries(title);
        //como su nombre lo indica el primer valor sera asignado al eje X
        //y el segundo al eje Y
        DefaultXYDataset dataSet = new DefaultXYDataset();
        float x = -20;
        for (int i = 1; i < measures.size(); i++) {
            series.add(i, x);
            x = x + 10;
        }
        XYDataset dataXY = new XYSeriesCollection(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "numero del registro",
                "Medida",
                dataXY,
                PlotOrientation.VERTICAL,
                false,
                false,
                true
        );

        //guardar imagen
        int name = 0;
        BufferedImage image = chart.createBufferedImage(400, 400);
        ImageIO.write(image, "png", new File(name + ".png"));
        name = name + 1;
        return image;
    }
}



