package dk.itu.grp11.main;

import java.io.*;
public class Main {
  public static void main(String[] args) {
    System.out.println("Program compiles as well :D :D :D :D");
    File node = new File("..\\..\\..\\..\\..\\src\\dk\\itu\\grp11\\main\\kdv_node_unload.txt");
    File road = new File("..\\..\\..\\..\\..\\src\\dk\\itu\\grp11\\main\\kdv_unload.txt");
    Parser p = new Parser(node, road);
    Point[] points = p.parsePoints();
    System.out.println(points[0]);
  }
}
