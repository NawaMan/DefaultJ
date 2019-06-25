//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
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
package nawaman.failable;

/**
 * This exception wraps an exception from a failable.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class FailableException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructor 
     * 
     * @param cause  the cause exception of this exception.
     **/
    public FailableException(Throwable cause) {
        super(cause);
    }
    
}
