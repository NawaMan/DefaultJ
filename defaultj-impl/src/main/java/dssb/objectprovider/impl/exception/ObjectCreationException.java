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
public class ObjectCreationException extends ProvideObjectException {
    
    private static final long serialVersionUID = 5414890542605369904L;
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that this fail creation is attempted too.
     **/
    public ObjectCreationException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that cause this exception.
     * @param cause  the cause exception of this exception.
     **/
    public ObjectCreationException(Class<?> clazz, Throwable cause) {
        super(clazz, cause);
    }
    
}
