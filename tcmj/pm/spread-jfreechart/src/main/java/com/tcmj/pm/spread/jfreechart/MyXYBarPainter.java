/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.pm.spread.jfreechart;

import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.ui.GradientPaintTransformer;
import org.jfree.ui.RectangleEdge;

/**
 *
 * @author Administrator
 */
public class MyXYBarPainter implements XYBarPainter, Serializable {

    public static Paint COLOR_CONFLICT = new Color(255, 105, 106);
    public static Paint COLOR_NO_CONFLICT = new Color(72, 163, 74);
    public static Paint COLOR_INPUTBAR = new Color(127, 146, 219);
    private XYTaskDataset dataset;
    /** The division point between the first and second gradient regions. */
    private double g1 = 0.0;
    /** The division point between the second and third gradient regions. */
    private double g2 = 0.20;
    /** The division point between the third and fourth gradient regions. */
    private double g3 = 0.80;

    /**
     * @return the painterType
     */
    public PainterType getPainterType() {
        return painterType;
    }

    /**
     * @param painterType the painterType to set
     */
    public void setPainterType(PainterType painterType) {
        this.painterType = painterType;
    }

    public enum PainterType {

        STANDART, GRADIENT
    };
    private PainterType painterType = PainterType.STANDART;

    public MyXYBarPainter(XYTaskDataset dataset) {
        this.dataset = dataset;
    }

    private Paint createItemPaint(int row, int column) {
        Paint itemPaint = null;
        XYTaskDataset taskdataset = dataset;
        TaskSeriesCollection tcol = taskdataset.getTasks();
        TaskSeries taskseries = tcol.getSeries(row);
        MyTask task = (MyTask) taskseries.get(column);
//        double dd = task.getPercentComplete();

        SpreadPeriod xbar = task.getBar();
        System.out.println("" + xbar.getClass());

//        if (xbar instanceof OutputBar) {
//
//            OutputBar cbar = (OutputBar) xbar;
//
//            if (cbar.getWeight() <= 1.0) {
                itemPaint = COLOR_NO_CONFLICT;
//            } else {
//                itemPaint = COLOR_CONFLICT;
//            }
//
//
//        } else if (xbar instanceof SimpleBar) {
//
//            SimpleBar sbar = (SimpleBar) xbar;
//            Integer type = (Integer)sbar.getProperty("type");
//
//            //TODO repair
//            if (type==null)type = 999;
//
//            switch (type) {
//                case 10:
//                    itemPaint = new Color(72, 189, 97);
//                    break;
//                case 20:
//                    itemPaint = new Color(188, 161, 241);
//                    break;
//                default:
//                    itemPaint = COLOR_INPUTBAR;
//            }


//        }
        return itemPaint;
    }

    public void paintBar(Graphics2D g2, XYBarRenderer renderer, int row,
            int column, RectangularShape bar, RectangleEdge base) {

        if (PainterType.STANDART == painterType) {
            paintBar1(g2, renderer, row, column, bar, base);
        } else {
            paintBar2(g2, renderer, row, column, bar, base);
        }



    }

    /**
     * Paints a single bar instance.
     *
     * @param g2  the graphics target.
     * @param renderer  the renderer.
     * @param row  the row index.
     * @param column  the column index.
     * @param bar  the bar
     * @param base  indicates which side of the rectangle is the base of the
     *              bar.
     */
    public void paintBar1(Graphics2D g2, XYBarRenderer renderer, int row,
            int column, RectangularShape bar, RectangleEdge base) {

        Paint itemPaint = renderer.getItemPaint(row, column);
        GradientPaintTransformer t = renderer.getGradientPaintTransformer();
        if (t != null && itemPaint instanceof GradientPaint) {
            itemPaint = t.transform((GradientPaint) itemPaint, bar);
        }


        ///


        itemPaint = createItemPaint(row, column);



        ///
        g2.setPaint(itemPaint);
        g2.fill(bar);

        // draw the outline...
        if (renderer.isDrawBarOutline()) {
            // && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = renderer.getItemOutlineStroke(row, column);
            Paint paint = renderer.getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }

    }

    /**
     * Paints a single bar instance.
     *
     * @param g2  the graphics target.
     * @param renderer  the renderer.
     * @param row  the row index.
     * @param column  the column index.
     * @param bar  the bar
     * @param base  indicates which side of the rectangle is the base of the
     *              bar.
     */
    public void paintBar2(Graphics2D g2, XYBarRenderer renderer, int row,
            int column, RectangularShape bar, RectangleEdge base) {

//        Paint itemPaint = renderer.getItemPaint(row, column);


        ///


        Paint itemPaint = createItemPaint(row, column);



        ///

        Color c0, c1;
        if (itemPaint instanceof Color) {
            c0 = (Color) itemPaint;
            c1 = c0.brighter();
        } else if (itemPaint instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint) itemPaint;
            c0 = gp.getColor1();
            c1 = gp.getColor2();
        } else {
            c0 = Color.blue;
            c1 = Color.blue.brighter();
        }

        // as a special case, if the bar colour has alpha == 0, we draw
        // nothing.
        if (c0.getAlpha() == 0) {
            return;
        }

        if (base == RectangleEdge.TOP || base == RectangleEdge.BOTTOM) {
            Rectangle2D[] regions = splitVerticalBar(bar, this.g1, this.g2,
                    this.g3);
            GradientPaint gp = new GradientPaint((float) regions[0].getMinX(),
                    0.0f, c0, (float) regions[0].getMaxX(), 0.0f, Color.white);
            g2.setPaint(gp);
            g2.fill(regions[0]);

            gp = new GradientPaint((float) regions[1].getMinX(), 0.0f,
                    Color.white, (float) regions[1].getMaxX(), 0.0f, c0);
            g2.setPaint(gp);
            g2.fill(regions[1]);

            gp = new GradientPaint((float) regions[2].getMinX(), 0.0f, c0,
                    (float) regions[2].getMaxX(), 0.0f, c1);
            g2.setPaint(gp);
            g2.fill(regions[2]);

            gp = new GradientPaint((float) regions[3].getMinX(), 0.0f, c1,
                    (float) regions[3].getMaxX(), 0.0f, c0);
            g2.setPaint(gp);
            g2.fill(regions[3]);
        } else if (base == RectangleEdge.LEFT || base == RectangleEdge.RIGHT) {
            Rectangle2D[] regions = splitHorizontalBar(bar, this.g1, this.g2,
                    this.g3);
            GradientPaint gp = new GradientPaint(0.0f,
                    (float) regions[0].getMinY(), c0, 0.0f,
                    (float) regions[0].getMaxX(), Color.white);
            g2.setPaint(gp);
            g2.fill(regions[0]);

            gp = new GradientPaint(0.0f, (float) regions[1].getMinY(),
                    Color.white, 0.0f, (float) regions[1].getMaxY(), c0);
            g2.setPaint(gp);
            g2.fill(regions[1]);

            gp = new GradientPaint(0.0f, (float) regions[2].getMinY(), c0,
                    0.0f, (float) regions[2].getMaxY(), c1);
            g2.setPaint(gp);
            g2.fill(regions[2]);

            gp = new GradientPaint(0.0f, (float) regions[3].getMinY(), c1,
                    0.0f, (float) regions[3].getMaxY(), c0);
            g2.setPaint(gp);
            g2.fill(regions[3]);

        }


// draw the outline...
        if (renderer.isDrawBarOutline()) {
            // && state.getBarWidth() > BAR_OUTLINE_WIDTH_THRESHOLD) {
            Stroke stroke = renderer.getItemOutlineStroke(row, column);
            Paint paint = renderer.getItemOutlinePaint(row, column);
            if (stroke != null && paint != null) {
                g2.setStroke(stroke);
                g2.setPaint(paint);
                g2.draw(bar);
            }
        }
    }

    /**
     * Paints a single bar instance.
     *
     * @param g2  the graphics target.
     * @param renderer  the renderer.
     * @param row  the row index.
     * @param column  the column index.
     * @param bar  the bar
     * @param base  indicates which side of the rectangle is the base of the
     *              bar.
     * @param pegShadow  peg the shadow to the base of the bar?
     */
    public void paintBarShadow(Graphics2D g2, XYBarRenderer renderer, int row,
            int column, RectangularShape bar, RectangleEdge base,
            boolean pegShadow) {

        // handle a special case - if the bar colour has alpha == 0, it is
        // invisible so we shouldn't draw any shadow
        Paint itemPaint = renderer.getItemPaint(row, column);
        if (itemPaint instanceof Color) {
            Color c = (Color) itemPaint;
            if (c.getAlpha() == 0) {
                return;
            }
        }

        RectangularShape shadow = createShadow(bar, renderer.getShadowXOffset(),
                renderer.getShadowYOffset(), base, pegShadow);
        g2.setPaint(Color.gray);
        g2.fill(shadow);

    }

    /**
     * Creates a shadow for the bar.
     *
     * @param bar  the bar shape.
     * @param xOffset  the x-offset for the shadow.
     * @param yOffset  the y-offset for the shadow.
     * @param base  the edge that is the base of the bar.
     * @param pegShadow  peg the shadow to the base?
     *
     * @return A rectangle for the shadow.
     */
    private Rectangle2D createShadow(RectangularShape bar, double xOffset,
            double yOffset, RectangleEdge base, boolean pegShadow) {
        double x0 = bar.getMinX();
        double x1 = bar.getMaxX();
        double y0 = bar.getMinY();
        double y1 = bar.getMaxY();
        if (base == RectangleEdge.TOP) {
            x0 += xOffset;
            x1 += xOffset;
            if (!pegShadow) {
                y0 += yOffset;
            }
            y1 += yOffset;
        } else if (base == RectangleEdge.BOTTOM) {
            x0 += xOffset;
            x1 += xOffset;
            y0 += yOffset;
            if (!pegShadow) {
                y1 += yOffset;
            }
        } else if (base == RectangleEdge.LEFT) {
            if (!pegShadow) {
                x0 += xOffset;
            }
            x1 += xOffset;
            y0 += yOffset;
            y1 += yOffset;
        } else if (base == RectangleEdge.RIGHT) {
            x0 += xOffset;
            if (!pegShadow) {
                x1 += xOffset;
            }
            y0 += yOffset;
            y1 += yOffset;
        }
        return new Rectangle2D.Double(x0, y0, (x1 - x0), (y1 - y0));
    }

    /**
     * Splits a bar into subregions (elsewhere, these subregions will have
     * different gradients applied to them).
     *
     * @param bar  the bar shape.
     * @param a  the first division.
     * @param b  the second division.
     * @param c  the third division.
     *
     * @return An array containing four subregions.
     */
    private Rectangle2D[] splitVerticalBar(RectangularShape bar, double a,
            double b, double c) {
        Rectangle2D[] result = new Rectangle2D[4];
        double x0 = bar.getMinX();
        double x1 = Math.rint(x0 + (bar.getWidth() * a));
        double x2 = Math.rint(x0 + (bar.getWidth() * b));
        double x3 = Math.rint(x0 + (bar.getWidth() * c));
        result[0] = new Rectangle2D.Double(bar.getMinX(), bar.getMinY(),
                x1 - x0, bar.getHeight());
        result[1] = new Rectangle2D.Double(x1, bar.getMinY(), x2 - x1,
                bar.getHeight());
        result[2] = new Rectangle2D.Double(x2, bar.getMinY(), x3 - x2,
                bar.getHeight());
        result[3] = new Rectangle2D.Double(x3, bar.getMinY(),
                bar.getMaxX() - x3, bar.getHeight());
        return result;
    }

    /**
     * Splits a bar into subregions (elsewhere, these subregions will have
     * different gradients applied to them).
     *
     * @param bar  the bar shape.
     * @param a  the first division.
     * @param b  the second division.
     * @param c  the third division.
     *
     * @return An array containing four subregions.
     */
    private Rectangle2D[] splitHorizontalBar(RectangularShape bar, double a,
            double b, double c) {
        Rectangle2D[] result = new Rectangle2D[4];
        double y0 = bar.getMinY();
        double y1 = Math.rint(y0 + (bar.getHeight() * a));
        double y2 = Math.rint(y0 + (bar.getHeight() * b));
        double y3 = Math.rint(y0 + (bar.getHeight() * c));
        result[0] = new Rectangle2D.Double(bar.getMinX(), bar.getMinY(),
                bar.getWidth(), y1 - y0);
        result[1] = new Rectangle2D.Double(bar.getMinX(), y1, bar.getWidth(),
                y2 - y1);
        result[2] = new Rectangle2D.Double(bar.getMinX(), y2, bar.getWidth(),
                y3 - y2);
        result[3] = new Rectangle2D.Double(bar.getMinX(), y3, bar.getWidth(),
                bar.getMaxY() - y3);
        return result;
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the obj (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StandardXYBarPainter)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    public int hashCode() {
        int hash = 37;
        // no fields to compute...
        return hash;
    }
}

