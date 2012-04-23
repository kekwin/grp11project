package dk.itu.grp11.main;

public class Main {
  public static void main(String[] args) {
    System.out.println("Program started");
    FileServer fs = FileServer.getFileServer();
    fs.run(); 
  }
}
