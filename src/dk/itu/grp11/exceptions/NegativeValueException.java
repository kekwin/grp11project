package dk.itu.grp11.exceptions;

public class NegativeValueException extends RuntimeException {
	private static final long serialVersionUID = -3917374993145240355L;

public NegativeValueException() {
    super();
  }
  
  public NegativeValueException(String s) {
    super(s);
  }
}
