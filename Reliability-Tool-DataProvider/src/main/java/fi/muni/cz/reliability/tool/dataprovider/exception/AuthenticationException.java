package fi.muni.cz.reliability.tool.dataprovider.exception;

/**
 * This excepeiton is thrown when there occures problem
 * during authetication
 * 
 * @author Radoslav Micko, 445611@muni.cz
 */
public class AuthenticationException extends DataProviderException {

    /**
     * Constructor of <code>AuthenticationException</code>
     */
    public AuthenticationException() {
    }
    
    /**
     * Constructor of <code>AuthenticationException</code> with message.
     * 
     * @param message message of exception
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>AuthenticationException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
