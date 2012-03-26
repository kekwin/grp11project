package dk.itu.grp11.exceptions;

public class NegativeValueException extends RuntimeException {
  public NegativeValueException() {
    super();
  }
  
  public NegativeValueException(String s) {
    super(s);
  }
}
