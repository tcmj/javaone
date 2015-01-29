/**
 * Copyright(c) 2003 - 2015 by tcmj
 * All Rights Reserved.
 */
package com.tcmj.pm.mta;

//  RChart
//  Copyright (C)
//
//  RReport@Confluencia.net
//  All rights reserved
//
// Adquisition , use and distribution of this code is subject to restriction:
//  - You may modify the source code in order to adapt it to your needs.
//  - Redistribution of this ( or a modifed version) source code is prohibited. You may only redistribute compiled versions.
//  - You may not remove this notice from the source code
//  - This notice disclaim all warranties of all material
//
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import com.tcmj.pm.mta.bo.ChartData;
import com.tcmj.pm.mta.jfree.ChartBuilder;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import static com.tcmj.common.lang.Check.notNull;

/**
 * Milestone Trend Analysis Chart.
 * This class takes data and builds
 * a milestone trend analysis from it.
 * RReport@Confluencia.net
 * @author tdeut
 */
public class MTABuilder {

    /** slf4j Logging Framework. */
    private static final Logger LOG = LoggerFactory.getLogger(MTABuilder.class);
    /** data model. */
    private final ChartData imtaChart;
    private static final int DEFAULT_CHART_WIDTH = 650;
    private static final int DEFAULT_CHART_HEIGHT = 700;
    private int chartWidth = DEFAULT_CHART_WIDTH;
    private int chartHeight = DEFAULT_CHART_HEIGHT;
    private String xAxisName = "Report Data";
    private String yAxisName = "Milestone Data";

    /**
     * constructor which takes the data.
     * @param chart data model
     */
    public MTABuilder(ChartData chart) {
        LOG.debug("Instantiate a new MTA Chart: {}", chart);

        this.imtaChart = notNull(chart, "Parameter ChartData cannot be null");

        if (chart.getTaskSeriesList() == null || chart.getTaskSeriesList().isEmpty()) {
            throw new IllegalArgumentException("ChartData does not contain any milestones!");
        }

    }

    public void createSVG(File file) throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException {
        Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        createSVG(out);
    }

    public void createSVG(Writer out) throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException {

        ChartBuilder chartBuilder = new ChartBuilder(imtaChart);

        JFreeChart chart = chartBuilder.createChart();

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // set the precision to avoid a null pointer exception in Batik 1.5
        svgGenerator.getGeneratorContext().setPrecision(6);

        // Ask the chart to render into the SVG Graphics2D implementation
        chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, getChartWidth(), getChartHeight()), null);

        // Finally, stream out SVG to a file using UTF-8 character to byte encoding
        boolean useCSS = true;
        svgGenerator.stream(out, useCSS);
        out.flush();
    }

    public Graphics2D createSVG() throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException {

        ChartBuilder chartBuilder = new ChartBuilder(imtaChart);

        JFreeChart chart = chartBuilder.createChart();

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // set the precision to avoid a null pointer exception in Batik 1.5
        svgGenerator.getGeneratorContext().setPrecision(6);

        // Ask the chart to render into the SVG Graphics2D implementation
        chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, getChartWidth(), getChartHeight()), null);

        // Finally, stream out SVG to a file using UTF-8 character to byte encoding
        return svgGenerator;

    }

    public String createSVGString() throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException {

        ChartBuilder chartBuilder = new ChartBuilder(imtaChart);

        JFreeChart chart = chartBuilder.createChart();

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument(null, "svg", null);

        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        // set the precision to avoid a null pointer exception in Batik 1.5
        svgGenerator.getGeneratorContext().setPrecision(6);

        // Ask the chart to render into the SVG Graphics2D implementation
        chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, getChartWidth(), getChartHeight()), null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer svgOutput = new OutputStreamWriter(baos, "UTF-8");
        svgGenerator.stream(svgOutput, true);

        String ggg = baos.toString();
        System.out.println(ggg);
        return ggg;
    }

    /**
     * Creates JFreeChart.
     * @return a JFreeChart
     */
    public JFreeChart createJFreeChart() {
        ChartBuilder chartBuilder = new ChartBuilder(imtaChart);
        JFreeChart chart = chartBuilder.createChart();
        return chart;
    }

    /**
     * Creates the iMTA image.
     * @return a buffered image
     */
    public BufferedImage createImage() throws UnsupportedEncodingException, FileNotFoundException, SVGGraphics2DIOException, IOException {

        ChartBuilder chartBuilder = new ChartBuilder(imtaChart);
        JFreeChart chart = chartBuilder.createChart();

        return chart.createBufferedImage(chartWidth, chartHeight);

//        ChartUtilities.saveChartAsPNG(new File("freespace.png"), chart, 600, 400, info);
//        PrintWriter pw = new PrintWriter(System.out);
////         ChartUtilities.writeImageMap(pw,"FreeSpace",info);
//        pw.flush();
//        BufferedImage image = buildIMTA(parameterMap);
//        BufferedImage image = null;
//        return image;
    }

//            java.awt.image.BufferedImage ChartImage = new java.awt.image.BufferedImage(getChartWidth(), getChartHeight(),
//                    java.awt.image.BufferedImage.TYPE_INT_RGB);
//
//            java.awt.Graphics2D ChartGraphics = ChartImage.createGraphics();
//
//            // get Chart
//            Chart c = getChart(parameters);
//
//            c.paint(ChartGraphics);
//
//            return ChartImage;
//
//
//
//            //    c.saveToFile("a.png","PNG");
////      }
////      c.paint(ChartGraphics);
//
////      if(encode.toLowerCase().compareTo("gif") == 0) {
////	// download gif encoder from http://www.acme.com/resources/classes/Acme.tar.gz
////	// encode buffered image to a gif
////	Class enClass = Class.forName("Acme.JPM.Encoders.GifEncoder");
////	Class[] constructorParams = new Class[2];
////	constructorParams[0] = Class.forName("java.awt.Image");
////	constructorParams[1] = Class.forName("java.io.OutputStream");
////	Object[] constructorObj = new Object[2];
////	constructorObj[0] = ChartImage;
////	constructorObj[1] = outb;
////
////	Object encoder = enClass.getConstructor(constructorParams).newInstance(
////	    constructorObj);
////
////	Class[] encodeParams = new Class[0];
////	Object[] encodeObj = new Object[0];
////
////	enClass.getMethod("encode", encodeParams).invoke(encoder, encodeObj);
////
////	//Acme.JPM.Encoders.GifEncoder encoder = new Acme.JPM.Encoders.GifEncoder(ChartImage ,outb);
////	//encoder.encode();
////      }
////      else {
////
////
////	if(encode.toLowerCase().compareTo("png") == 0) {
////	  // download from http://home.boone.net/~wbrameld/pngencoder/
////	  Class enClass = Class.forName("com.bigfoot.bugar.image.PNGEncoder");
////	  Class[] constructorParams = new Class[2];
////	  constructorParams[0] = Class.forName("java.awt.Image");
////	  constructorParams[1] = Class.forName("java.io.OutputStream");
////	  Object[] constructorObj = new Object[2];
////	  constructorObj[0] = ChartImage;
////	  constructorObj[1] = outb;
////
////	  Object encoder = enClass.getConstructor(constructorParams).newInstance(
////	      constructorObj);
////
////	  Class[] encodeParams = new Class[0];
////	  Object[] encodeObj = new Object[0];
////
////	  enClass.getMethod("encodeImage", encodeParams).invoke(encoder, encodeObj);
////
////	  // create PNG image
////	  //com.bigfoot.bugar.image.PNGEncoder encoder= new com.bigfoot.bugar.image.PNGEncoder(ChartImage  ,outb);
////	  //encoder.encodeImage( );
////	}
////	else {
////	  // create JPEG image
////	  com.sun.image.codec.jpeg.JPEGImageEncoder encoder = com.sun.image.codec.jpeg.
////	      JPEGCodec.createJPEGEncoder(outb);
////	  encoder.encode(ChartImage);
////	}
////      }
//
//            // use this to create a gif image
//            //GifEncoder encoder = new GifEncoder(ChartImage,outb);
//            //encoder.encode();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//
//
//        return null;
//
//    }
    /**
     * Width of the image. Default: 650
     * @return the chartWidth
     */
    public int getChartWidth() {
        return chartWidth;
    }

    /**
     * Width of the image. Default: 650
     * @param chartWidth the chartWidth to set
     */
    public void setChartWidth(int chartWidth) {
        this.chartWidth = chartWidth;
    }

    /**
     * Height of the image. Default: 700
     * @return the chartHeight
     */
    public int getChartHeight() {
        return chartHeight;
    }

    /**
     * Height of the image. Default: 700
     * @param chartHeight the chartHeight to set
     */
    public void setChartHeight(int chartHeight) {
        this.chartHeight = chartHeight;
    }

    /**
     * Returns xAxisName.
     * @return the xAxisName
     */
    public String getxAxisName() {
        return xAxisName;
    }

    /**
     * Sets xAxisName.
     * @param xAxisName the xAxisName to set
     */
    public void setxAxisName(String xAxisName) {
        this.xAxisName = xAxisName;
    }

    /**
     * Returns yAxisName.
     * @return the yAxisName
     */
    public String getyAxisName() {
        return yAxisName;
    }

    /**
     * Sets yAxisName.
     * @param yAxisName the yAxisName to set
     */
    public void setyAxisName(String yAxisName) {
        this.yAxisName = yAxisName;
    }
}
