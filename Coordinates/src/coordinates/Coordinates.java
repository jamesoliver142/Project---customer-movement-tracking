/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinates;

/**
 *
 * @author James Oliver Shannon Ness
 */

import java.util.Scanner;

public class Coordinates {
public static void main(String[] args) {
    
    Scanner in = new Scanner(System.in);
    
    int x1, y1, x2, y2 ;
    int A, B, C, AB, BC;
    
      System.out.println("Enter Distance from A");
      A = in.nextInt();
      
      System.out.println("Enter Distance from B");
      B = in.nextInt();
      A = A * A;
      B = B * B;
      AB = A - B;
      
      System.out.println("Enter Distance from C");
      C = in.nextInt();
      C = C * C;
      BC = B - C;
      
      System.out.println("A - B = "+ AB);
      System.out.println("B - C = "+ BC);
  

    // this is from the equation we worked out manually
    x1 = 140;
    y1 = 60;

    int finalAB = AB + 9800;
    x2 = -120;
    y2 = 60;

    int finalBC = BC - 3800;

    // try to eliminate x from the two equations to get the value of y
    int leastCommMultiplier =
            Math.abs(x1) * Math.abs(x2) / maxCommFactor(Math.abs(x1), Math.abs(x2));

    int cancelFactor = -1;
    if (x1 * y1 < 0) cancelFactor = 1;

    int multiplier = leastCommMultiplier / x1;
    int multiplierTwo = cancelFactor * leastCommMultiplier / x2;
    // remove x, solve for y
    int y = (finalAB * multiplier + finalBC * multiplierTwo) / (y1 * multiplier + y2 * multiplierTwo);

    //find x based of y value
    int x = (finalAB - y1 * y) / x1;

    System.out.println("your x co-ordinate is : " + x);
    System.out.println("your y co-ordinate is : " + y);
    
    //close our scanner
    in.close();
}

public static int maxCommFactor(int a, int b) {
    if (a < b) {
        int temp = a;
        a = b;
        b = temp;
    }

    while (a % b != 0) {
        int temp = a % b;
        a = b;
        b = temp;
    }

    return b;
}
}
