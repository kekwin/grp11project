package dk.itu.grp11.exceptions;

//TODO Should not extends RuntimeException but a checkedtype
public class DataNotInitialized extends RuntimeException {
  public DataNotInitialized() {
    super();
  }
  
  public DataNotInitialized(String s) {
    super(s);
  }
}
