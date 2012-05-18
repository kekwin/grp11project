package dk.itu.grp11.main;

import java.util.Locale;

public class Main {
  public static void main(String[] args) {
    Locale.setDefault(new Locale("da"));
    System.out.println("Program started");
    FileServer fs = FileServer.getFileServer();
    fs.run(); 
  }
}