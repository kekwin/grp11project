package dk.itu.grp11.exceptions;

/**
 * This exception is invoked if an unexisting RoadType is being called in our application.
 *
 * @author Group 11
 */
@SuppressWarnings("serial") //We never serialize our classes
public class RoadTypeDoesNotExistException extends RuntimeException {
  public RoadTypeDoesNotExistException() {}
  public RoadTypeDoesNotExistException(String s) { super(s); }
}
