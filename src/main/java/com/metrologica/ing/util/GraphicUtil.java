package com.metrologica.ing.util;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.metrologica.ing.model.Measures;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.renderer.PolarItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.Size2D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GraphicUtil {

    BufferedImage grafica = null;

    @Autowired
    private ResourceLoader resourceLoader;

    public static BufferedImage spiderWebChart(List<Measures> measures, String title, String equipment, String pattern, String error) throws IOException {

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
            String category1 = equipment;
            String category2 = pattern;
            String category3 = error;
            for (int i = 0; i < measures.size(); i++) {
                String position = String.valueOf(i + 1);
                dataset.addValue(measures.get(i).getEquipmentH(), category1, position);
                dataset.addValue(measures.get(i).getPattern(), category2, position);
                dataset.addValue(measures.get(i).getError(), category3, position);
            }
            return dataset;
        }
        return null;
    }

    private static JFreeChart createChart(CategoryDataset dataset, String title) {

        //diseño de la grafica-------------------------------------------------------------------------------------------
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
//        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setLabelFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 7));
        plot.setAxisLinePaint(Color.lightGray); // // el color de las líneas de los radios
        plot.setBackgroundPaint(Color.WHITE); // color del fondo de la grafica
        plot.setOutlinePaint(Color.white); // borde de la grafica
        plot.setLabelPaint(Color.darkGray); // color numeros grafica
        plot.setOutlineVisible(false); // visible el marco
        plot.setSeriesOutlinePaint(Color.WHITE); //borde de puntos referentes de color
        plot.setInteriorGap(0.2); // Espacio entre las líneas y el centro del gráfico
        Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, 34, 3, 2, 2);  // Modificar los bordes de los elementos de la leyenda
        plot.setLegendItemShape(roundedRectangle);// Modificar el tamaño de los elementos de la leyenda

        // Configurar radios-------------------------------------------------------------------------------------------
        plot.setAxisLabelGap(0.1);//la separacion de los numeros a la grafica
        RectangleInsets rectangleInsets = new RectangleInsets(8,8,8,8);//el paddind de la grafica
        plot.setInsets(rectangleInsets,false);
        plot.setHeadPercent(0.01); // Tamaño de la cabeza de la línea en porcentaje
        plot.setWebFilled(true); //color fondo de las graficas
//        plot.setWebColor(Color.GRAY);


/*
        // Personalizar el eje radial
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Formato para los números en el eje
        plot.setAxisLabelGenerator(new DefaultSpiderWebAxisLabelGenerator(decimalFormat));
        plot.setAxisLabelFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 10)); // Fuente para los números en el eje
*/

        // Crear un color original-------------------------------------------------------------------------------------------
        Color originalColorBlue = Color.BLUE;
        Color originalColorGreen = Color.GREEN;
        Color originalColorRed = Color.RED;
        // Obtener los componentes RGB del color original
        int alphaBlue = originalColorBlue.getAlpha();
        int alphaGreen = originalColorGreen.getAlpha();
        int alphaRed = originalColorRed.getAlpha();
        Color newColorBlue = new Color(80, 105, 220, alphaBlue);
        Color newColorGreen = new Color(50, 205, 50, alphaGreen);
        Color newColorRed = new Color(255, 0, 0, alphaRed);
        // Modificar los colores de las series
        List<Color> customColors = new ArrayList<>();
        customColors.add(newColorBlue);
        customColors.add(newColorGreen);
        customColors.add(newColorRed);
        // Asignar los colores de la lista a las series
        for (int i = 0; i < customColors.size(); i++) {
            plot.setSeriesPaint(i, customColors.get(i));
        }

        //agregar estilos de titulo-------------------------------------------------------------------------------------------
        TextTitle textTitle = new TextTitle();
        textTitle.setText(title);
        textTitle.setPadding(8,25,10,25);
        textTitle.setPaint(Color.DARK_GRAY);
//        textTitle.setBackgroundPaint(Color.WHITE);
        textTitle.setFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 11));
        //diseño de la imagen-------------------------------------------------------------------------------------------
        JFreeChart chart = new JFreeChart(textTitle.getText(), textTitle.getFont(), plot, false);
        chart.setBorderVisible(true);
        chart.setBorderPaint(Color.lightGray);
        chart.setTitle(textTitle);
        chart.setBackgroundPaint(Color.WHITE);

        //agregar subtitulo-------------------------------------------------------------------------------------------
        LegendTitle legendtitle = new LegendTitle(plot);
        legendtitle.setPosition(RectangleEdge.TOP);
        legendtitle.setItemFont(new Font("arial", com.itextpdf.text.Font.NORMAL, 8));
        legendtitle.setPadding(1,1,1,1);
        legendtitle.setItemPaint(Color.DARK_GRAY); // letra de los item
        legendtitle.setItemLabelPadding(new RectangleInsets(1,3,1,3));//items
        RectangleEdge rectangleEdge = RectangleEdge.opposite(RectangleEdge.BOTTOM);
        legendtitle.setLegendItemGraphicEdge(rectangleEdge);
        legendtitle.setLegendItemGraphicPadding(new RectangleInsets(5,5,1,5));//paddinng de los puntos
        legendtitle.setItemLabelPadding(new RectangleInsets(2,5,2,5)); //paddinng de las letras
        legendtitle.setBorder(0.1,0.1,0.1,0.1);//borde de los items

//        Size2D size2D = new Size2D(5,5);
//        RectangleAnchor rectangleAnchor = null;
//        Rectangle2D rectangle2D = RectangleAnchor.createRectangle(size2D, 5, 8, RectangleAnchor.valueOf("hola"));
//        Rectangle2D rectangle = new Rectangle(2,8,2,8);
//        legendtitle.setBounds(rectangle);
//        legendtitle.setLegendItemGraphicAnchor(rectangle2D);
//        legendtitle.setMargin(1,1,1,1);

        chart.addSubtitle(legendtitle);

        return chart;
    }


/*
    public static BufferedImage createPolarPlot(String title) throws IOException {

        // Crear un conjunto de datos
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        XYSeries series = new XYSeries("Data Series",false,false);
        series.add(0, 1);
        series.add(Math.PI / 4, 0.5);
        series.add(Math.PI / 2, 0.8);
        // Agregar más puntos según sea necesario
        dataset.addSeries(series);

        // Crear el gráfico de polar plot
        JFreeChart chart = ChartFactory.createPolarChart(
                title,
                dataset,
                true,
                true,
                false
        );

        //guardar imagen
        BufferedImage image = chart.createBufferedImage(170, 210);
        ImageIO.write(image, "png", new File("img.png"));
        return image;
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
    }*/
}



