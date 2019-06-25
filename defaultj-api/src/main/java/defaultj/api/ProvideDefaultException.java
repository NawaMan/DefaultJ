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
package defaultj.api;

/**
 * Exception thrown if there is a problem while trying to provide a default.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ProvideDefaultException extends RuntimeException {
    
    private static final long serialVersionUID = -1503175854525324555L;
    
    private final Class<?> clazz;
    
    /**
     * Constructor the exception with the class that was trying to provide.
     * 
     * @param clazz
     *          the class that this fail provision is attempted too.
     **/
    public ProvideDefaultException(Class<?> clazz) {
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
    public ProvideDefaultException(Class<?> clazz, Throwable cause) {
        super(clazz.getCanonicalName(), cause);
        this.clazz = clazz;
    }
    /**
     * Constructor 
     * 
     * @param msg    the error message.
     * @param clazz  the class that this fail creation is attempted too.
     * @param cause  the root cause of the problem.
     **/
    protected ProvideDefaultException(String msg, Class<?> clazz, Throwable cause) {
        super(msg, cause);
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
