package fi.muni.cz.reliability.tool.utils.exception;

/**
 * This excepeiton is thrown when there occures problem in Utils
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class UtilsException extends RuntimeException {

    /**
     * Constructor of <code>UtilsException</code>
     */
    public UtilsException() {
    }
    
    /**
     * Constructor of <code>UtilsException</code> with message.
     * 
     * @param message message of exception
     */
    public UtilsException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>UtilsException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public UtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
