package dk.itu.grp11.main;

/**
 * Main Class, starts a fileserver and runs it.
 *
 * @author Group 11
 */
public class Main {
  public static void main(String[] args) {
    System.out.println("Program started");
    FileServer fs = FileServer.getFileServer();
    fs.run(); 
  }
}