/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class XYTaskDatasetDemo2 extends ApplicationFrame
{
  public XYTaskDatasetDemo2(String paramString)
  {
    super(paramString);
    JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(500, 300));
    setContentPane(localJPanel);
  }

  private static XYPlot createSubplot1(XYDataset paramXYDataset)
  {
    XYLineAndShapeRenderer localXYLineAndShapeRenderer = new XYLineAndShapeRenderer();
    localXYLineAndShapeRenderer.setUseFillPaint(true);
    localXYLineAndShapeRenderer.setBaseFillPaint(Color.white);
    localXYLineAndShapeRenderer.setBaseShape(new Ellipse2D.Double(-4.0D, -4.0D, 8.0D, 8.0D));
    localXYLineAndShapeRenderer.setAutoPopulateSeriesShape(false);
    NumberAxis localNumberAxis = new NumberAxis("Y");
    localNumberAxis.setLowerMargin(0.1D);
    localNumberAxis.setUpperMargin(0.1D);
    XYPlot localXYPlot = new XYPlot(paramXYDataset, new DateAxis("Time"), localNumberAxis, localXYLineAndShapeRenderer);
    return localXYPlot;
  }

  private static XYPlot createSubplot2(IntervalXYDataset paramIntervalXYDataset)
  {
    DateAxis localDateAxis = new DateAxis("Date/Time");
    SymbolAxis localSymbolAxis = new SymbolAxis("Resources", new String[] { "Team A", "Team B", "Team C", "Team D", "Team E" });
    localSymbolAxis.setGridBandsVisible(false);
    XYBarRenderer localXYBarRenderer = new XYBarRenderer();
    localXYBarRenderer.setUseYInterval(true);
    XYPlot localXYPlot = new XYPlot(paramIntervalXYDataset, localDateAxis, localSymbolAxis, localXYBarRenderer);
    return localXYPlot;
  }

  private static JFreeChart createChart()
  {
    CombinedDomainXYPlot localCombinedDomainXYPlot = new CombinedDomainXYPlot(new DateAxis("Date/Time"));
    localCombinedDomainXYPlot.add(createSubplot1(createDataset1()));
    localCombinedDomainXYPlot.add(createSubplot2(createDataset2()));
    JFreeChart localJFreeChart = new JFreeChart("XYTaskDatasetDemo2", localCombinedDomainXYPlot);
    localJFreeChart.setBackgroundPaint(Color.white);
    ChartUtilities.applyCurrentTheme(localJFreeChart);
    return localJFreeChart;
  }

  public static JPanel createDemoPanel()
  {
    return new ChartPanel(createChart());
  }

  private static XYDataset createDataset1()
  {
    TimeSeriesCollection localTimeSeriesCollection = new TimeSeriesCollection();
    TimeSeries localTimeSeries = new TimeSeries("Time Series 1", Hour.class);
    localTimeSeries.add(new Hour(0, new Day()), 20214.5D);
    localTimeSeries.add(new Hour(4, new Day()), 73346.5D);
    localTimeSeries.add(new Hour(8, new Day()), 54643.599999999999D);
    localTimeSeries.add(new Hour(12, new Day()), 92683.800000000003D);
    localTimeSeries.add(new Hour(16, new Day()), 110235.39999999999D);
    localTimeSeries.add(new Hour(20, new Day()), 120742.5D);
    localTimeSeries.add(new Hour(24, new Day()), 90654.5D);
    localTimeSeriesCollection.addSeries(localTimeSeries);
    return localTimeSeriesCollection;
  }

  private static IntervalXYDataset createDataset2()
  {
    XYTaskDataset localXYTaskDataset = new XYTaskDataset(createTasks());
    localXYTaskDataset.setTransposed(true);
    localXYTaskDataset.setSeriesWidth(0.6D);
    return localXYTaskDataset;
  }

  private static TaskSeriesCollection createTasks()
  {
    TaskSeriesCollection localTaskSeriesCollection = new TaskSeriesCollection();
    TaskSeries localTaskSeries1 = new TaskSeries("Team A");
    localTaskSeries1.add(new Task("T1a", new Hour(11, new Day())));
    localTaskSeries1.add(new Task("T1b", new Hour(14, new Day())));
    localTaskSeries1.add(new Task("T1c", new Hour(16, new Day())));
    TaskSeries localTaskSeries2 = new TaskSeries("Team B");
    localTaskSeries2.add(new Task("T2a", new Hour(13, new Day())));
    localTaskSeries2.add(new Task("T2b", new Hour(19, new Day())));
    localTaskSeries2.add(new Task("T2c", new Hour(21, new Day())));
    TaskSeries localTaskSeries3 = new TaskSeries("Team C");
    localTaskSeries3.add(new Task("T3a", new Hour(13, new Day())));
    localTaskSeries3.add(new Task("T3b", new Hour(19, new Day())));
    localTaskSeries3.add(new Task("T3c", new Hour(21, new Day())));
    TaskSeries localTaskSeries4 = new TaskSeries("Team D");
    localTaskSeries4.add(new Task("T4a", new Day()));
    TaskSeries localTaskSeries5 = new TaskSeries("Team E");
    localTaskSeries5.add(new Task("T5a", new Day()));
    localTaskSeriesCollection.add(localTaskSeries1);
    localTaskSeriesCollection.add(localTaskSeries2);
    localTaskSeriesCollection.add(localTaskSeries3);
    localTaskSeriesCollection.add(localTaskSeries4);
    localTaskSeriesCollection.add(localTaskSeries5);
    return localTaskSeriesCollection;
  }

  public static void main(String[] paramArrayOfString)
  {
    XYTaskDatasetDemo2 localXYTaskDatasetDemo2 = new XYTaskDatasetDemo2("JFreeChart : XYTaskDatasetDemo2.java");
    localXYTaskDatasetDemo2.pack();
    RefineryUtilities.centerFrameOnScreen(localXYTaskDatasetDemo2);
    localXYTaskDatasetDemo2.setVisible(true);
  }
}