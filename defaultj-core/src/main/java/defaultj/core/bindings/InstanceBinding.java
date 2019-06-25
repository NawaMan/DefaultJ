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
package defaultj.core.bindings;

import defaultj.api.IProvideDefault;
import defaultj.core.IBind;

/**
 * Bind to a value.
 * 
 * @param <TYPE> the type the factory can provide.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class InstanceBinding<TYPE> implements IBind<TYPE> {
    
    private final TYPE instance;
    
    /**
     * Constructs an instant binding.
     * 
     * @param instance  the instant to bind.
     */
    public InstanceBinding(TYPE instance) {
        this.instance = instance;
    }
    
    @Override
    public TYPE get(IProvideDefault defaultProvider) {
        return this.instance;
    }
    
}
