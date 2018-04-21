package fi.muni.cz.reliability.tool.dataprovider.exception;

/**
 * This excepeiton is thrown when there occures problem
 * during reading authentication file
 * 
 * @author Radoslav Micko <445611@muni.cz>
 */
public class AuthenticationFileErrorException extends AuthenticationException {

    /**
     * Constructor of <code>AuthenticationFileErrorException</code>
     */
    public AuthenticationFileErrorException() {
    }
    
    /**
     * Constructor of <code>AuthenticationFileErrorException</code> with message.
     * 
     * @param message message of exception
     */
    public AuthenticationFileErrorException(String message) {
        super(message);
    }
    
    /**
     * Constructor of <code>AuthenticationFileErrorException</code> with message and cause.
     * 
     * @param message message of exception
     * @param cause Exception caused the problem
     */
    public AuthenticationFileErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
