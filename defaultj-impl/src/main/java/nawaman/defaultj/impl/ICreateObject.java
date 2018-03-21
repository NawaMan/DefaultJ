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
package nawaman.defaultj.impl;

import nawaman.defaultj.api.IProvideObject;

/**
 * Classes implementing this interface can create an object.
 * 
 * @param <TYPE> the type the factory can provide.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface ICreateObject<TYPE> {
    
    /**
     * Create the object.
     * 
     * @param objectProvider  the object provider.
     * @return  the newly created object.
     */
    public TYPE create(IProvideObject objectProvider);
    
}
