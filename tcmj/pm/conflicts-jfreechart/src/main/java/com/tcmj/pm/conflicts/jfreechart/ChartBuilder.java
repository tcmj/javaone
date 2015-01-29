
package com.tcmj.pm.conflicts.jfreechart;

import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.OutputBar;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.Year;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import static com.tcmj.common.date.DateTool.daysbetween;

/**
 *
 * @author Administrator
 */
public class ChartBuilder {

    protected ConflictFinder cfinder;
    protected TaskSeriesCollection localTaskSeriesCollection = new TaskSeriesCollection();

    public ChartBuilder(ConflictFinder cfinder) {
        this.cfinder = cfinder;
        
    }

    protected IntervalXYDataset createDataset() {
        XYTaskDataset xytds = new XYTaskDataset(localTaskSeriesCollection);
        
        xytds.setSeriesWidth(0.8D);

        return xytds;
    }

    protected void createSerie1() {


//        List<ConflictBar> barListc = cfinder.getOutputConflictList();
//
//        TaskSeries localTaskSeries3 = new TaskSeries("OutputConflictList");
//
//        for (ConflictBar bar : barListc) {
//
//            Task task = new MyTask(bar);
//            localTaskSeries3.add(task);
//        }
//        localTaskSeriesCollection.add(localTaskSeries3);




//----------------------------------------------------------------
        List<OutputBar> barListAll = cfinder.getOutputBarListAll();

        TaskSeries localTaskSeries1 = new TaskSeries("OutputBarList");
        int no = 0;
        for (Bar bar : barListAll) {
            no++;
            System.out.println("----->"+bar.getKey());
            Task task = new MyTask(bar);
            localTaskSeries1.add(task);
        }
        localTaskSeriesCollection.add(localTaskSeries1);

//-----------------------------------------------------------------

        int ino = 0;
        List<Bar> inputList = cfinder.getInputList();

        Collections.reverse(inputList);

        for (Bar bar : inputList) {
            ino++;
            TaskSeries localTaskSeries2 = new TaskSeries("InputTask");
            Task task = new MyTask(bar);
            localTaskSeries2.add(task);
            localTaskSeriesCollection.add(localTaskSeries2);
        }
//---------------------------------------------------------------------
    }



    public LegendItemCollection createLegend() {
        LegendItemCollection items = new LegendItemCollection();
        items.add(new LegendItem("Conflict Task", MyXYBarPainter.COLOR_CONFLICT));
        items.add(new LegendItem("Normal Task", MyXYBarPainter.COLOR_NO_CONFLICT));
        items.add(new LegendItem("InputItem", MyXYBarPainter.COLOR_INPUTBAR));

        return items;
    }

    public PeriodAxis createTimeAxis() {


        PeriodAxis timeaxis = new PeriodAxis("");

        
        timeaxis.setAutoTickUnitSelection(true);
        timeaxis.setTimeZone(TimeZone.getDefault());
        



        long days = daysbetween(cfinder.getMinStartDate(), cfinder.getMaxEndDate());
        System.err.println("days = " + days);

        PeriodAxisLabelInfo[] arrayOfPeriodAxisLabelInfo;


        if (days <= 31) { //2m
            timeaxis.setAutoRangeTimePeriodClass(Day.class);
            timeaxis.setMajorTickTimePeriodClass(Day.class);

            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[4];

            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Hour.class, new SimpleDateFormat("HH"),
                    new RectangleInsets(1.0D, 1.0D, 1.0D, 1.0D), new Font("SansSerif", 1, 10), Color.lightGray, false, new BasicStroke(0.0F), Color.lightGray);

            arrayOfPeriodAxisLabelInfo[1] = new PeriodAxisLabelInfo(Day.class, new SimpleDateFormat("d"),
                    new RectangleInsets(1.0D, 1.0D, 1.0D, 1.0D), new Font("SansSerif", Font.BOLD, 11), Color.BLACK, true, new BasicStroke(1.0F), Color.lightGray);


            arrayOfPeriodAxisLabelInfo[2] = new PeriodAxisLabelInfo(Month.class, new SimpleDateFormat("MMMM"),
                    new RectangleInsets(2.0D, 2.0D, 1.0D, 2.0D), new Font("SansSerif", 1, 10), Color.BLACK, false, new BasicStroke(0.0F), Color.lightGray);


            arrayOfPeriodAxisLabelInfo[3] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));

        } else if (days <= 92) { //2m
            timeaxis.setAutoRangeTimePeriodClass(Month.class);
            timeaxis.setMajorTickTimePeriodClass(Month.class);
            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[4];
            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Hour.class, new SimpleDateFormat("HH"));
            arrayOfPeriodAxisLabelInfo[1] = new PeriodAxisLabelInfo(Day.class, new SimpleDateFormat("d"));
            arrayOfPeriodAxisLabelInfo[2] = new PeriodAxisLabelInfo(Month.class, new SimpleDateFormat("MMM"),
                    new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D), new Font("SansSerif", 1, 10), Color.blue, false, new BasicStroke(0.0F), Color.lightGray);
            arrayOfPeriodAxisLabelInfo[3] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));

        } else if (days <= 183) { //6m
            timeaxis.setAutoRangeTimePeriodClass(Month.class);
            timeaxis.setMajorTickTimePeriodClass(Month.class);
            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[3];
            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Day.class, new SimpleDateFormat("d"));
            arrayOfPeriodAxisLabelInfo[1] = new PeriodAxisLabelInfo(Month.class, new SimpleDateFormat("MMM"), new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D), new Font("SansSerif", 1, 10), Color.BLACK, false, new BasicStroke(0.0F), Color.lightGray);
            arrayOfPeriodAxisLabelInfo[2] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));


        }else if (days <= 366) { //12m
            System.err.println("days = " + Month.class);

            timeaxis.setAutoRangeTimePeriodClass(Month.class);
            timeaxis.setMajorTickTimePeriodClass(Month.class);
            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[2];
            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Month.class, new SimpleDateFormat("MMM"), new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D), new Font("SansSerif", 1, 10), Color.BLACK, true, new BasicStroke(1.0F), Color.lightGray);
            arrayOfPeriodAxisLabelInfo[1] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));


        }else if (days <= 732) { //2y
            timeaxis.setAutoRangeTimePeriodClass(Month.class);
            timeaxis.setMajorTickTimePeriodClass(Month.class);
            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[2];
            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Month.class, new SimpleDateFormat("MMM"), new RectangleInsets(2.0D, 2.0D, 2.0D, 2.0D), new Font("SansSerif", 1, 10), Color.BLACK, true, new BasicStroke(1.0F), Color.lightGray);
            arrayOfPeriodAxisLabelInfo[1] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));


        }  else  {
            timeaxis.setAutoRangeTimePeriodClass(Month.class);
            timeaxis.setMajorTickTimePeriodClass(Month.class);
            arrayOfPeriodAxisLabelInfo = new PeriodAxisLabelInfo[1];
            arrayOfPeriodAxisLabelInfo[0] = new PeriodAxisLabelInfo(Year.class, new SimpleDateFormat("yyyy"));

        }

        

        timeaxis.setLabelInfo(arrayOfPeriodAxisLabelInfo);


        return timeaxis;
    }

    

    public SymbolAxis createSymbolAxis() {

        int count = localTaskSeriesCollection.getSeriesCount();

        String[] names = new String[count];

        for (int i = 0; i < count; i++) {
            TaskSeries serie =
                    localTaskSeriesCollection.getSeries(i);

            names[i] = serie.getKey().toString();

        }



        SymbolAxis localSymbolAxis =
                new SymbolAxis(null, names);


        localSymbolAxis.setGridBandsVisible(false);


        return localSymbolAxis;
    }



    public XYItemLabelGenerator createItemLabelGenerator() {

//        DateFormat d = new SimpleDateFormat("yyyy/MM/dd");
//        DateFormat d2 = null;

        MyXYItemLabelGenerator gen = new MyXYItemLabelGenerator();

        return gen;
    }

    public ItemLabelPosition createItemLabelPosition() {
//        return new ItemLabelPosition(ItemLabelAnchor.INSIDE10, TextAnchor.TOP_LEFT);
        return new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.TOP_CENTER);

    }

    
    public JFreeChart createChart() {
        createSerie1();
        return createChart(createDataset());
    }
    
    
    public JFreeChart createChart(IntervalXYDataset paramIntervalXYDataset) {



//        IntervalXYDataset paramIntervalXYDataset = createDataset();


        JFreeChart jfreechart = ChartFactory.createXYBarChart(
                "ConflictFinder", //title
                "Resource", //xAxisLabel
                false, //dateAxis
                "Timing", //yAxisLabel
                paramIntervalXYDataset, //dataset
                PlotOrientation.HORIZONTAL, //orientation
                true, //legend
                true,  //tooltips
                false); //urls

        XYPlot xyplot = (XYPlot) jfreechart.getPlot();

        xyplot.setBackgroundPaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.darkGray);//vertical
        xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);//horizontal
        xyplot.setDomainAxis(createSymbolAxis());
        xyplot.setFixedLegendItems(createLegend());

        MyXYBarRenderer xyBarRenderer = new MyXYBarRenderer();
        xyBarRenderer.setUseYInterval(true);

        xyBarRenderer.setShadowVisible(false);

        xyBarRenderer.setDrawBarOutline(true);
        xyBarRenderer.setBaseOutlinePaint(Color.DARK_GRAY);

        xyBarRenderer.setBaseItemLabelFont(new Font("Arial",Font.PLAIN,10));

        xyBarRenderer.setMargin(0.2);
        xyplot.setRenderer(0, xyBarRenderer, true);

//xyBarRenderer.


        MyXYBarPainter barPainter = new MyXYBarPainter((XYTaskDataset) paramIntervalXYDataset);
        barPainter.setPainterType(MyXYBarPainter.PainterType.GRADIENT);
        xyBarRenderer.setBarPainter(barPainter);



        //Item-Label:
        xyBarRenderer.setBaseItemLabelsVisible(true);
        xyBarRenderer.setBaseItemLabelGenerator(createItemLabelGenerator());
        xyBarRenderer.setBasePositiveItemLabelPosition(createItemLabelPosition());



xyBarRenderer.setBaseToolTipGenerator(createTooltipGenerator());



//PeriodAxis xaxis= new PeriodAxis("kk");

        xyplot.setRangeAxis(createTimeAxis());



//        DateAxis xaxis = new DateAxis();
//        xaxis.setVerticalTickLabels(true);
//        xyplot.setRangeAxis(xaxis);
        xyplot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

//        ChartUtilities.applyCurrentTheme(jfreechart);

        return jfreechart;
    }

    public XYToolTipGenerator createTooltipGenerator() {


        
        return new BarToolTipGenerator();


    }
}
