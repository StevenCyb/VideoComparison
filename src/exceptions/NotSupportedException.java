package exceptions;
/**
 * Simple custom exception class if action is not supported in this version
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class NotSupportedException extends Exception {
	private static final long serialVersionUID = 1889837977001071866L;
	
	/**
	 * Constructor of the class
	 * 
	 * @param message Message of this exception
	 */
	public NotSupportedException(String message) {
       super(message);
    }
}
