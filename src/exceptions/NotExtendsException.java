package exceptions;
/**
 * Simple custom exception class if class without functionality is used
 *
 * @author Steven Cybinski
 * @version 0.1
 */
public class NotExtendsException extends Exception {
	private static final long serialVersionUID = 2177455068671395760L;
	/**
	 * Constructor of the class
	 * 
	 * @param message Message of this exception
	 */
	public NotExtendsException(String message) {
       super(message);
    }
}
