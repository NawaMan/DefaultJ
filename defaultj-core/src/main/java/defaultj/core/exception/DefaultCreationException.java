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

import defaultj.api.ProvideDefaultException;

/**
 * This exception is thrown when creating a default fail.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultCreationException extends ProvideDefaultException {
    
    private static final long serialVersionUID = 5414890542605369904L;
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that this fail creation is attempted too.
     **/
    public DefaultCreationException(Class<?> clazz) {
        this(clazz, null);
    }
    
    /**
     * Constructor 
     * 
     * @param clazz  the class that cause this exception.
     * @param cause  the cause exception of this exception.
     **/
    public DefaultCreationException(Class<?> clazz, Throwable cause) {
        super(clazz, cause);
    }
    
    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     * @param cause  the root cause of the problem.
     **/
    protected DefaultCreationException(String msg, Class<?> clazz, Throwable cause) {
        super(msg, clazz, cause);
    }
    
}
