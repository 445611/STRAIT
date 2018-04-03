package fi.muni.cz.reliability.tool.dataprovider.exception;

/**
 * This excpeiton is thrown when there occures problem
 * during authetication
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class AuthenticationException extends RuntimeException {

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
     * @param cause 
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
