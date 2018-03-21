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
