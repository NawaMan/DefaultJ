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
package dssb.objectprovider.impl.bindings;

import dssb.objectprovider.api.IProvideObject;
import dssb.objectprovider.impl.IBind;
import dssb.objectprovider.impl.ICreateObject;
import lombok.val;

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
