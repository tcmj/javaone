/**
 * Copyright(c) 2003 - 2015 by tcmj
 * All Rights Reserved.
 */
package com.tcmj.pm.mta;

import javax.imageio.ImageIO;
import java.io.File;
import com.lowagie.text.pdf.codec.Base64.OutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.InputStream;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import java.util.HashMap;
import java.util.Map;
import com.tcmj.pm.mta.bo.ChartData;
import com.tcmj.pm.mta.bo.DataPoint;
import com.tcmj.pm.mta.bo.DataSeries;
import com.tcmj.pm.mta.dots.Symbol;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tcmj.common.date.DateTool.*;

/**
 *
 * @author TDEUT
 */
public class MTABuilderTest {

    static final String SYSP_OUTPUT_FOLDER = "com.tcmj.pm.mta.junit.outputfolder";
    static String OUTPUT_FOLDER = System.getProperty(SYSP_OUTPUT_FOLDER, System.getProperty("user.home"));

    public MTABuilderTest() {
        System.out.println("output folder can be set through system property: '" + SYSP_OUTPUT_FOLDER + "'");
        System.out.println("current output folder: '" + OUTPUT_FOLDER + "'");
    }

    @Test
    public void shouldCreateASimpleChartImage() throws IOException {
        System.out.println("shouldCreateASimpleChartImage");
        //given
        ChartData chart = createTestData();
        MTABuilder instance = new MTABuilder(chart);
        //when
        BufferedImage result = instance.createImage();
        //then
        assertNotNull(result);
        write(result, "imta_shouldCreateASimpleChartImage.png");
    }

    @Test
    public void shouldCreateSeriesWithSameSymbolsAndColors() throws IOException {
        System.out.println("shouldCreateSeriesWithSameSymbolsAndColors");
        //given
        ChartData chart = new ChartData("5 Same Symbols");

        for (int i = 1; i <= 15; i++) {
            int modifier = (i + 1);
            DataSeries serie = new DataSeries("Milestone " + i);

            serie.setColor("0xff0000");
            serie.setSymbol(Symbol.No_18);

            serie.addDataPoint(new DataPoint(date(2011, 2, 1), date(2011, 5 + modifier, 1)));
            serie.addDataPoint(new DataPoint(date(2011, 4, 1), date(2011, 5 + modifier, 25)));
            serie.addDataPoint(new DataPoint(date(2011, 5, 1), date(2011, 5 + modifier, 26)));
            serie.addDataPoint(new DataPoint(date(2011, 11, 1), date(2011, 5 + modifier, 30)));
            serie.addDataPoint(new DataPoint(date(2011, 12, 1), date(2011, 5 + modifier, 12)));
            chart.addTaskSeries(serie);
        }

        MTABuilder instance = new MTABuilder(chart);
        instance.setChartHeight(333);
        instance.setChartWidth(333);
        //when
        BufferedImage result = instance.createImage();
        //then
        assertNotNull(result);
        write(result, "shouldCreateSeriesWithSameSymbolsAndColors.png");

        outaspdf(instance);

    }

    @Test
    public void shouldCreateManySeries() throws IOException {
        System.out.println("shouldCreateManySeries");
        //given
        ChartData chart = new ChartData("500 Milestones");

        for (int i = 1; i <= 50; i++) {
            int modifier = (i + 1);
            DataSeries serie = new DataSeries("Milestone " + i);

            //serie.setColor("0xff0000");
            //serie.setSymbol(IMTASymbol.No_18);
            serie.addDataPoint(new DataPoint(date(2011, 2, 1), date(2011, 5 + modifier, 1)));
            serie.addDataPoint(new DataPoint(date(2011, 4, 1), date(2011, 5 + modifier, 25)));
            serie.addDataPoint(new DataPoint(date(2011, 5, 1), date(2011, 5 + modifier, 26)));
            serie.addDataPoint(new DataPoint(date(2011, 11, 1), date(2011, 5 + modifier, 30)));
            serie.addDataPoint(new DataPoint(date(2011, 12, 1), date(2011, 5 + modifier, 12)));
            chart.addTaskSeries(serie);
        }

        MTABuilder instance = new MTABuilder(chart);
        //when
        BufferedImage result = instance.createImage();
        //then
        assertNotNull(result);
        write(result, "shouldCreateManySeries.png");
    }

    @Test
    public void specialTest() throws IOException {
        System.out.println("specialTest");
        //given
        ChartData chart = new ChartData("500 Milestones");

        DataSeries serie = new DataSeries("Finished_in_Past ");

        /*
         * Linie: Finished_in_Past_10052e82067ed
         Punkt: RD: 2011-03-25   MD: 2011-04-04
         Punkt: RD: 2011-04-04   MD: 2011-04-04
         Linie: Planned_in_Past_100530609d476
         Punkt: RD: 2011-03-25   MD: 2011-04-04
        
         */
        //serie.setColor("0xff0000");
        //serie.setSymbol(IMTASymbol.No_18);
        serie.addDataPoint(new DataPoint(date(2011, 3, 25), date(2011, 4, 4)));
        serie.addDataPoint(new DataPoint(date(2011, 4, 4), date(2011, 4, 4)));
        chart.addTaskSeries(serie);

        DataSeries serie2 = new DataSeries("Planned_in_Past ");
        serie2.addDataPoint(new DataPoint(date(2011, 3, 25), date(2011, 4, 4)));
        chart.addTaskSeries(serie2);

        MTABuilder instance = new MTABuilder(chart);
        //when
        BufferedImage result = instance.createImage();
        //then
        assertNotNull(result);
        write(result, "specialTest.png");

    }

    private void outaspdf(MTABuilder instance) {
        try {

            File file = File.createTempFile("test", "svg");

            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            instance.createSVG(outputstreamwriter);
            outputstreamwriter.flush();
            outputstreamwriter.close();

            InputStream in = new FileInputStream(file);

            String reportDest = "mtareport1.pdf";

            Map<String, Object> params = new HashMap<String, Object>();

            params.put("mtaimg", in);

            String strSVG = (instance.createSVGString()); // convert chart to svg format
            net.sf.jasperreports.renderers.BatikRenderer rend1 = net.sf.jasperreports.renderers.BatikRenderer.getInstance(in);
            params.put("mtaimg", rend1);
//        InputStream ins = new ByteArrayInputStream(instance.createSVG().g)
//                ImageIO.createInputStream(result);
            //ImageIO.write(outImg, "png", file);

//        InputStream mtastream = new InputStream() {}
            InputStream jrxmlstream = MTABuilderTest.class.getResourceAsStream("mtareport1.jrxml");

            JasperReport jasperReport
                    = JasperCompileManager.compileReport(jrxmlstream);

            JasperPrint jasperPrint
                    = JasperFillManager.fillReport(
                            jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(
                    jasperPrint, reportDest);

            JasperViewer.viewReport(jasperPrint);

            Thread.sleep(2222);

            String uuu = "ooo";

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private ChartData createTestData() {
        ChartData chart = new ChartData("MyJUnitTestChart");

        for (int i = 1; i <= 15; i++) {
            int modifier = (i + 5);
            DataSeries serie = new DataSeries("Milestone " + i);
            serie.addDataPoint(new DataPoint(date(2010, 1, 1), date(2011, 5 + modifier, 1)));
            serie.addDataPoint(new DataPoint(date(2010, 4, 1), date(2011, 5 + modifier, 25)));
            serie.addDataPoint(new DataPoint(date(2010, 5, 1), date(2011, 5 + modifier, 26)));
            serie.addDataPoint(new DataPoint(date(2010, 11, 1), date(2011, 5 + modifier, 30)));
            serie.addDataPoint(new DataPoint(date(2010, 12, 1), date(2011, 5 + modifier, 12)));
            serie.addDataPoint(new DataPoint(date(2011, 6, 1), date(2012, 5 + modifier, 2)));
            chart.addTaskSeries(serie);
        }

        return chart;
    }

    private void write(BufferedImage outImg, String filename) throws IOException {
        File file = new File(OUTPUT_FOLDER, filename);
        ImageIO.write(outImg, "png", file);
        System.out.println("iMTA Chart written to " + file);
    }
}
