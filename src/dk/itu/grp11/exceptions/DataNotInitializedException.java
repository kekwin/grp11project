package dk.itu.grp11.exceptions;

//TODO Should probably not extend RuntimeException but a checked type
public class DataNotInitializedException extends RuntimeException {
	private static final long serialVersionUID = -7696051533490392426L;

	public DataNotInitializedException() {}
  public DataNotInitializedException(String s) { super(s); }
}
