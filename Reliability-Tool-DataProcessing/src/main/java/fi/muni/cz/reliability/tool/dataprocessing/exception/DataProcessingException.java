package fi.muni.cz.reliability.tool.dataprocessing.exception;

/**
 * This excepeiton is thrown when there occures problem in Data processing
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class DataProcessingException extends RuntimeException {
    
    /**
     * Constructor of <code>UtilsException</code>
     */
    public DataProcessingException() {
    }
    
    /**
     * Constructor of <code>UtilsException</code> with message.
     * 
     * @param message message of exception
     */
    public DataProcessingException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>UtilsException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}