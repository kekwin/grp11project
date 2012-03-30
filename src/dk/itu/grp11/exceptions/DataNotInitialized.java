package dk.itu.grp11.exceptions;

//TODO Should probably not extend RuntimeException but a checked type
public class DataNotInitialized extends RuntimeException {
	private static final long serialVersionUID = -7696051533490392426L;

public DataNotInitialized() {
    super();
  }
  
  public DataNotInitialized(String s) {
    super(s);
  }
}
