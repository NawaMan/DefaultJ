//  ========================================================================
//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package defaultj.core.exception;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * This exception is thrown when a method is called when a default interface
 *   as asked but there is at least one method that is not .
 * 
 * @author NawaMan -- nawa@nawaman.net
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
