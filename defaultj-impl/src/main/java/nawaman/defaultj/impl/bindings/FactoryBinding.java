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
package nawaman.defaultj.impl.bindings;

import lombok.val;
import nawaman.defaultj.api.IProvideObject;
import nawaman.defaultj.impl.IBind;
import nawaman.defaultj.impl.ICreateObject;

/**
 * Binding to a factory.
 *
 * @param <TYPE> the type the factory can provide.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class FactoryBinding<TYPE> implements IBind<TYPE> {
    
    private final ICreateObject<TYPE> factory;
    
    /**
     * Constructs a factory binding using the the factory.
     * 
     * @param factory  the factory to create the object.
     */
    public FactoryBinding(ICreateObject<TYPE> factory) {
        this.factory = factory;
    }
    
    @Override
    public TYPE get(IProvideObject objectProvider) {
        val value = this.factory.create(objectProvider);
        return value;
    }
    
}
