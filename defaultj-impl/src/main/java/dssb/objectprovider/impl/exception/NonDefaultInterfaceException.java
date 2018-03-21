package dssb.objectprovider.impl.exception;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * This exception is thrown when a method is called when a default interface
 *   as asked but there is at least one method that is not .
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class NonDefaultInterfaceException extends AbstractClassCreationException {
    
    private static final long serialVersionUID = -8000862544457872776L;
    
    private final Map<String, String> methods;
    
    /**
     * Constructor 
     * 
     * @param clzz  the class.
     * @param methods the methods,
     **/
    public NonDefaultInterfaceException(Class<?> clzz, Map<String, String> methods) {
        super("Non-default method found in '" + clzz + "': " + methods, clzz);
        this.methods = unmodifiableMap((methods != null) ? methods : new HashMap<String, String>());
    }
    
    /** @return the method with the problem. */
    public Map<String, String> getMethod() {
        return methods;
    }
}
