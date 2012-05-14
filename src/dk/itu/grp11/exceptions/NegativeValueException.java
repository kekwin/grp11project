package dk.itu.grp11.exceptions;

@SuppressWarnings("serial") //We never serialize our class
public class NegativeValueException extends RuntimeException {
	public NegativeValueException() {}
	public NegativeValueException(String s) { super(s); }
}
