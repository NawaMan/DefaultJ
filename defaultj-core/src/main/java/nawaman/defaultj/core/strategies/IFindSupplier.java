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
package nawaman.defaultj.core.strategies;

import nawaman.defaultj.api.IProvideDefault;
import nawaman.failable.Failable.Supplier;

/**
 * Classes implementing this interface know how to find supplier for the default of the given class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface IFindSupplier {
    
    /**
     * Find a supplier to supplier value
     * 
     * @param <TYPE>           the data type.
     * @param <THROWABLE>      the exception.
     * @param theGivenClass    the data class.
     * @param defaultProvider  the default provider.
     * @return  the supplier for the value.
     */
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider);
    
}
