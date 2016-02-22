package heatmap;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
public class Heatmap {
 


    private static final int halfCircle = 32;
    private static final String circle = System.getProperty("user.dir")+ File.separator + "heatmap"+ File.separator + "circle.png";
    private static final String colours = System.getProperty("user.dir")+ File.separator + "heatmap"+ File.separator+ "colors.png";
    
    
    

    private Map<Integer, List<Point>> map;
    private int maxOccurance      = 1;
    private int maxX;
    private int maxY;
    private final String heatMap;
    private final String outputMap;

    public Heatmap(final List<Point> points, final String output,
        final String lvlMap) {
        outputMap = output;
        this.heatMap = lvlMap;
        initMap(points);
    }

    private void initMap(final List<Point> points) {
        map = new HashMap<Integer, List<Point>>();
        final BufferedImage mapPic = loadImage(heatMap);
        maxX = mapPic.getWidth();
        maxY = mapPic.getHeight();

        final int pointSize = points.size();
        for (int i = 0; i < pointSize; i++) {
            final Point point = points.get(i);
            final int hash = getkey(point);
            if (map.containsKey(hash)) {
                final List<Point> thisList = map.get(hash);
                thisList.add(point);
                if (thisList.size() > maxOccurance) {
                    maxOccurance = thisList.size();
                }
            } else {
                final List<Point> newList = new LinkedList<Point>();
                newList.add(point);
                map.put(hash, newList);
            }
        }
    }


    public void createHeatMap(final float multiplier) {

        final BufferedImage circle = loadImage(Heatmap.circle);
        BufferedImage heatMap = new BufferedImage(maxX, maxY, 6);
        paintInColor(heatMap, Color.white);

        final Iterator<List<Point>> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            final List<Point> currentPoints = iterator.next();
            float opaque = currentPoints.size() / (float) maxOccurance;
            opaque = opaque * multiplier;
            if (opaque > 1) {
                opaque = 1;
            }

            final Point currentPoint = currentPoints.get(0);
            addImage(heatMap, circle, opaque,
                    (currentPoint.x - halfCircle),
                    (currentPoint.y - halfCircle));
        }
        
        heatMap = negateImage(heatMap);
        remap(heatMap);
        final BufferedImage output = loadImage(this.heatMap);
        addImage(output, heatMap, 0.4f);

        saveImage(output, outputMap);
        print("Heat map has been created");
    }


    private void remap(final BufferedImage map) {
        final BufferedImage colorGradiant = loadImage(colours);
        final int width = map.getWidth();
        final int height = map.getHeight();
        final int gradientHight = colorGradiant.getHeight() - 1;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                final int rGB = map.getRGB(i, j);
                float multiplier = rGB & 0xff; 
                multiplier *= ((rGB >>> 8)) & 0xff; 
                multiplier *= (rGB >>> 16) & 0xff; 
                multiplier /= 16581375; 
                final int y = (int) (multiplier * gradientHight);
                final int mapedRGB = colorGradiant.getRGB(0, y);
                map.setRGB(i, j, mapedRGB);
            }
        }
    }

    private BufferedImage negateImage(final BufferedImage img) {
        final int width = img.getWidth();
        final int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                final int rGB = img.getRGB(x, y);
                final int r = Math.abs(((rGB >>> 16) & 0xff) - 255);                                                                    
                final int g = Math.abs(((rGB >>> 8) & 0xff) - 255);                                                                    
                final int b = Math.abs((rGB & 0xff) - 255); 
                img.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return img;
    }


    private void paintInColor(final BufferedImage buff, final Color color) {
        final Graphics2D g2 = buff.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, buff.getWidth(), buff.getHeight());
        g2.dispose();
    }


    private void addImage(final BufferedImage buff1, final BufferedImage buff2,
            final float opaque) {
        addImage(buff1, buff2, opaque, 0, 0);
    }


    private void addImage(final BufferedImage buff1, final BufferedImage buff2,
            final float opaque, final int x, final int y) {
        final Graphics2D g2d = buff1.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                opaque));
        g2d.drawImage(buff2, x, y, null);
        g2d.dispose();
    }


    private void saveImage(final BufferedImage buff, final String dest) {
        try {
            final File outputfile = new File(dest);
            ImageIO.write(buff, "png", outputfile);
        } catch (final IOException e) {
            print("error saving the image: " + dest + ": " + e);
        }
    }

    private BufferedImage loadImage(final String ref) {
        BufferedImage b1 = null;
        try {
            b1 = ImageIO.read(new File(ref));
        } catch (final IOException e) {
            System.out.println("error loading the image: " + ref + " : " + e);
        }
        return b1;
    }

    private int getkey(final Point p) {
        return ((p.x << 19) | (p.y << 7));
    }


    private void print(final String s) {
        System.out.println(s);
    }
}
