package demo;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class XYTaskDatasetDemo1 extends ApplicationFrame
{
  public XYTaskDatasetDemo1(String paramString)
  {
    super(paramString);
    JPanel localJPanel = createDemoPanel();
    localJPanel.setPreferredSize(new Dimension(500, 300));
    setContentPane(localJPanel);
  }

  private static JFreeChart createChart(IntervalXYDataset paramIntervalXYDataset)
  {
    JFreeChart localJFreeChart = ChartFactory.createXYBarChart("XYTaskDatasetDemo1", "Resource", false, "Timing", paramIntervalXYDataset, PlotOrientation.HORIZONTAL, true, false, false);
    localJFreeChart.setBackgroundPaint(Color.white);
    XYPlot localXYPlot = (XYPlot)localJFreeChart.getPlot();
    SymbolAxis localSymbolAxis = new SymbolAxis("Series", new String[] { "Team A", "Team B", "Team C", "Team D" });
    localSymbolAxis.setGridBandsVisible(false);
    localXYPlot.setDomainAxis(localSymbolAxis);
    XYBarRenderer localXYBarRenderer = (XYBarRenderer)localXYPlot.getRenderer();
    localXYBarRenderer.setUseYInterval(true);
    localXYPlot.setRangeAxis(new DateAxis("Timing"));
    ChartUtilities.applyCurrentTheme(localJFreeChart);
    return localJFreeChart;
  }

  public static JPanel createDemoPanel()
  {
    return new ChartPanel(createChart(createDataset()));
  }

  private static IntervalXYDataset createDataset()
  {
    return new XYTaskDataset(createTasks());
  }

  private static TaskSeriesCollection createTasks()
  {
      TimeSeries timeseries = new TimeSeries("L&G European Index Trust");
      timeseries.add(new TimeSeriesDataItem(null, WIDTH));
      
      
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
    localTaskSeriesCollection.add(localTaskSeries1);
    localTaskSeriesCollection.add(localTaskSeries2);
    localTaskSeriesCollection.add(localTaskSeries3);
    localTaskSeriesCollection.add(localTaskSeries4);
    return localTaskSeriesCollection;
  }




  public static void main(String[] paramArrayOfString)
  {
    XYTaskDatasetDemo1 localXYTaskDatasetDemo1 = new XYTaskDatasetDemo1("JFreeChart : XYTaskDatasetDemo1.java");
    localXYTaskDatasetDemo1.pack();
    RefineryUtilities.centerFrameOnScreen(localXYTaskDatasetDemo1);
    localXYTaskDatasetDemo1.setVisible(true);
  }
}
