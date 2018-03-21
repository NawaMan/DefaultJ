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
package dssb.objectprovider.api;

/**
 * Exception thrown if there is a problem while trying to provide an object.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class ProvideObjectException extends RuntimeException {
    
    private static final long serialVersionUID = -1503175854525324555L;
    
    private final Class<?> clazz;
    
    /**
     * Constructor the exception with the class that was trying to provide.
     * 
     * @param clazz
     *          the class that this fail provision is attempted too.
     **/
    public ProvideObjectException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Constructor the exception with the class that was trying to provide and the cause of the problem.
     * 
     * @param clazz 
     *          the class that this fail provision is attempted too.
     * @param cause
     *          the cause of the problem.
     **/
    public ProvideObjectException(Class<?> clazz, Throwable cause) {
        super(clazz.getCanonicalName(), cause);
        this.clazz = clazz;
    }
    
    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     **/
    protected ProvideObjectException(String msg, Class<?> clazz) {
        super(msg);
        this.clazz = clazz;
    }
    
    /**
     * Returns the target class.
     * 
     * @return the target class with the problem.
     **/
    public Class<?> getTargetClass() {
        return clazz;
    }
    
}
