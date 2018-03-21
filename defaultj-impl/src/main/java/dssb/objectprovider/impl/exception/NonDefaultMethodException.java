package dssb.objectprovider.impl.exception;

import java.lang.reflect.Method;

/**
 * This exception is thrown when a method is called from a default interface
 *   but found to have no default.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class NonDefaultMethodException extends RuntimeException {
    
    private static final long serialVersionUID = 5270138287435896641L;
    
    private final Method method;
    
    /**
     * Constructor 
     * 
     * @param method  the method.
     **/
    public NonDefaultMethodException(Method method) {
        super("Non-default method found: " + method);
        this.method = method;
    }
    
    /** @return the method with the problem. */
    public Method getMethod() {
        return method;
    }
}
