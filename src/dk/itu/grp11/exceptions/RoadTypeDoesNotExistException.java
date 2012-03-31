package dk.itu.grp11.exceptions;

public class RoadTypeDoesNotExistException extends RuntimeException {
  private static final long serialVersionUID = -8569251840278245242L;

  public RoadTypeDoesNotExistException() {}
  public RoadTypeDoesNotExistException(String s) { super(s); }
}
