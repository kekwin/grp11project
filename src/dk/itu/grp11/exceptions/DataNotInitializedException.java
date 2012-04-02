package dk.itu.grp11.exceptions;

public class DataNotInitializedException extends RuntimeException {
	private static final long serialVersionUID = -7696051533490392426L;

	public DataNotInitializedException() {}
  public DataNotInitializedException(String s) { super(s); }
}