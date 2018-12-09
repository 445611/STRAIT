package fi.muni.cz.models.exception;

/**
 * This excepeiton is thrown when there occures problem in Model
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class ModelException extends RuntimeException {

    /**
     * Constructor of <code>ModelException</code>
     */
    public ModelException() {
    }
    
    /**
     * Constructor of <code>ModelException</code> with message.
     * 
     * @param message message of exception
     */
    public ModelException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>ModelException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
