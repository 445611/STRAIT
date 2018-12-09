package fi.muni.cz.models.exception;

/**
 * This excepeiton is thrown when there is no such trend in data.
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class TrendModelException extends ModelException {
    
    /**
     * Constructor of <code>TrendModelException</code>
     */
    public TrendModelException() {
    }
    
    /**
     * Constructor of <code>TrendModelException</code> with message.
     * 
     * @param message message of exception
     */
    public TrendModelException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>TrendModelException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public TrendModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
