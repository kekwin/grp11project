package dk.itu.grp11.exceptions;
/**
 * This exception is invoked when negative values are used where it is not allowed.
 *
 * @author Group 11
 */
@SuppressWarnings("serial") //We never serialize our classes
public class NegativeValueException extends RuntimeException {
	public NegativeValueException() {}
	public NegativeValueException(String s) { super(s); }
}
