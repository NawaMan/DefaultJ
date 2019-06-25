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
package nawaman.defaultj.core.exception;

import static java.lang.String.format;

/**
 * This exception is thrown when the specified implementation cannot be found.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ImplementedClassNotExistException extends DefaultCreationException {
    
    private static final long serialVersionUID = 8158915955662317452L;
    
    private final String implementationClassName;
    
    /**
     * Constructs the exception without the root cause.
     * 
     * @param clazz                    the class.
     * @param implementationClassName  the implementation class.
     */
    public ImplementedClassNotExistException(Class<?> clazz, String implementationClassName) {
        this(clazz, implementationClassName, null);
    }
    
    /**
     * Constructs the exception with the root cause.
     * 
     * @param clazz                    the class.
     * @param implementationClassName  the implementation class.
     * @param cause                    the root cause.
     */
    public ImplementedClassNotExistException(Class<?> clazz, String implementationClassName, Throwable cause) {
        super(format("Unable to find or initialize the implementation ('%s') for '%s'",
                implementationClassName,
                clazz.getName()),
            clazz,
            cause);
        this.implementationClassName = implementationClassName;
    }
    
    /**
     * Returns the implementation class name.
     * 
     * @return the implementation class name.
     */
    public String getImplementationClassName() {
        return this.implementationClassName;
    }
    
}
