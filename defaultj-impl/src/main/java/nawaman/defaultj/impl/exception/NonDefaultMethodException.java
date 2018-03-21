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
package nawaman.defaultj.impl.exception;

import java.lang.reflect.Method;

/**
 * This exception is thrown when a method is called from a default interface
 *   but found to have no default.
 * 
 * @author NawaMan -- nawa@nawaman.net
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
