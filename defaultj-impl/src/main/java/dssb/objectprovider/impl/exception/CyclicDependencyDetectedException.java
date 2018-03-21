//  ========================================================================
//  Copyright (c) 2017 Direct Solution Software Builders (DSSB).
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
package dssb.objectprovider.impl.exception;

import dssb.objectprovider.api.ProvideObjectException;

/**
 * This exception is thrown when creating an object fail.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class CyclicDependencyDetectedException extends ProvideObjectException {
    
    private static final long serialVersionUID = -7821227248195126756L;
    
    private final Class<?> clazz;
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that this fail creation is attempted too.
     **/
    public CyclicDependencyDetectedException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Construct an exception
     * 
     * @param clazz  the class that causes this cyclic.
     * @param cause  the cause exception.
     **/
    public CyclicDependencyDetectedException(Class<?> clazz, Throwable cause) {
        super(clazz, cause);
        this.clazz = clazz;
    }
    
    /** @return the target class with the problem. */
    public Class<?> getTargetClass() {
        return clazz;
    }
}
