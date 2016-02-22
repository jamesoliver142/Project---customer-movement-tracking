package heatmap;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {


    public static void main(final String[] args) {
        
        
        final List<Point> points = new ArrayList<Point>();
        int x, y;   
        Scanner in = new Scanner(System.in);

        //this is a simple for loop putting in how many x and y points you would like to use
        // change the value in the for loop to how many coordinates you would like to put in.
        for (int i = 0; i < 1; i++) {
                    System.out.println("Enter x");
                    x = in.nextInt() * 10;
      
                    System.out.println("Enter y");
                    y = in.nextInt() * 10;
                    final Point p = new Point(x, y);
                    points.add(p);
        }

        final String outputFile = System.getProperty("user.dir")+ File.separator + "heatmap" + File.separator + "heatmap.png";
        final String originalImage = System.getProperty("user.dir")+ File.separator + "heatmap" + File.separator + "template.png";
        final Heatmap myMap = new Heatmap(points, outputFile, originalImage);
        myMap.createHeatMap(5f);
    }

}
