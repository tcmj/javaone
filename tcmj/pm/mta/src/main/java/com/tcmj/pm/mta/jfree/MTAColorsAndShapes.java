/*
 * Copyright (C) 2011 tcmj development
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.pm.mta.jfree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import org.jfree.chart.ChartColor;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author tcmj
 */
public class MTAColorsAndShapes {
    
    
       private static Shape[] createStandardSeriesShapes(double size) {

        Shape[] result = new Shape[10];

        double delta = size / 2.0;
        int[] xpoints = null;
        int[] ypoints = null;

        // circle
        result[0] = new Ellipse2D.Double(-delta, -delta, size, size);
        // square
        result[1] = new Rectangle2D.Double(-delta, -delta, size-1, size-1);

        // up-pointing triangle
        xpoints = intArray(0.0, delta, -delta);
        ypoints = intArray(-delta, delta, delta);
        result[2] = new Polygon(xpoints, ypoints, 3);

        // diamond
        xpoints = intArray(0.0, delta, 0.0, -delta);
        ypoints = intArray(-delta, 0.0, delta, 0.0);
        result[3] = new Polygon(xpoints, ypoints, 4);

        // horizontal rectangle
//        result[4] = new Rectangle2D.Double(-delta, -delta / 2, size, size / 2);
        result[4] = ShapeUtilities.createDiagonalCross((float)size / 2.5f, 1.0f);

        // down-pointing triangle
        xpoints = intArray(-delta, +delta, 0.0);
        ypoints = intArray(-delta, -delta, delta);
        result[5] = new Polygon(xpoints, ypoints, 3);

        // horizontal ellipse
        result[6] = new Ellipse2D.Double(-delta, -delta / 2, size, size / 2);

        result[6] = ShapeUtilities.createRegularCross((float)size / 2.0f, 2f);
        
        
        // right-pointing triangle
        xpoints = intArray(-delta, delta, -delta);
        ypoints = intArray(-delta, 0.0, delta);
        result[7] = new Polygon(xpoints, ypoints, 3);

        // vertical rectangle
        result[8] = new Rectangle2D.Double(-delta / 2, -delta, size / 2, size);

        // left-pointing triangle
        xpoints = intArray(-delta, delta, delta);
        ypoints = intArray(0.0, -delta, +delta);
        result[9] = new Polygon(xpoints, ypoints, 3);

        return result;

    }
    private static int[] intArray(double a, double b, double c) {
        return new int[] {(int) a, (int) b, (int) c};
    }

    private static int[] intArray(double a, double b, double c, double d) {
        return new int[] {(int) a, (int) b, (int) c, (int) d};
    }

    
    
    public static DrawingSupplier createDrawingSupplier(int seriesCount) {

        Paint[] mtapaints = new Paint[]{
            Color.BLACK,
            ChartColor.DARK_RED,
            ChartColor.VERY_DARK_GREEN,
            ChartColor.DARK_BLUE,
            ChartColor.VERY_DARK_MAGENTA,
            ChartColor.GRAY,
            ChartColor.VERY_DARK_BLUE,
            ChartColor.VERY_DARK_RED,
            ChartColor.VERY_DARK_YELLOW,
            ChartColor.DARK_YELLOW,
            ChartColor.VERY_DARK_CYAN,
            ChartColor.LIGHT_MAGENTA,
            ChartColor.DARK_CYAN, 
            ChartColor.VERY_LIGHT_BLUE,
            ChartColor.VERY_LIGHT_GREEN,
            ChartColor.VERY_LIGHT_MAGENTA,
            ChartColor.VERY_LIGHT_CYAN,
            ChartColor.ORANGE
        };


        
        //shape size
        double shapesize;
        float strokesize;
        if (seriesCount<=5) {
            shapesize = 8d;
            strokesize = 1.5f;
        }else{
            shapesize = 6d;
            strokesize = 1.0f;
        }
        Stroke[] strokes = new Stroke[] {new BasicStroke(strokesize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL)};
        
        
        DefaultDrawingSupplier supplier =
                new DefaultDrawingSupplier(
                mtapaints,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                strokes, //DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                createStandardSeriesShapes(shapesize));

        return supplier;

    }
    
    
}
