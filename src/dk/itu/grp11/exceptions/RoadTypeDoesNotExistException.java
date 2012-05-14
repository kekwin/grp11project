package dk.itu.grp11.exceptions;

@SuppressWarnings("serial") //We never serialize our class
public class RoadTypeDoesNotExistException extends RuntimeException {
  public RoadTypeDoesNotExistException() {}
  public RoadTypeDoesNotExistException(String s) { super(s); }
}
