package fi.muni.cz.reliability.tool.dataprovider.exception;

/**
 * This excepeiton is thrown when there occures problem
 * during in <code>DataProvider</code>
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class DataProviderException extends RuntimeException {

    /**
     * Constructor of <code>DataProviderException</code>
     */
    public DataProviderException() {
    }
    
    /**
     * Constructor of <code>DataProviderException</code> with message.
     * 
     * @param message message of exception
     */
    public DataProviderException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>DataProviderException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public DataProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
