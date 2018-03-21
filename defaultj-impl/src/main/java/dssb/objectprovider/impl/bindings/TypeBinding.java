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
import lombok.val;

/**
 * Bind to a type.
 *
 * @param <TYPE> the type the factory can provide.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
public class TypeBinding<TYPE> implements IBind<TYPE> {
    
    private final Class<? extends TYPE> referedType;
    
    /**
     * Constructs a type binding.
     * 
     * @param referedType  the refered type.
     */
    public TypeBinding(Class<? extends TYPE> referedType) {
        this.referedType = referedType;
    }
    
    @Override
    public TYPE get(IProvideObject objectProvider) {
        val value = (TYPE)objectProvider.get(referedType);
        return value;
    }
    
}
