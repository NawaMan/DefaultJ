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

import static nawaman.defaultj.core.utils.AnnotationUtils.has;
import static nawaman.utils.reflection.UProxy.createDefaultProxy;
import static nawaman.utils.reflection.UProxy.getNonDefaultMethods;

import lombok.val;
import nawaman.defaultj.annotations.DefaultInterface;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.exception.NonDefaultInterfaceException;
import nawaman.failable.Failable.Supplier;

/**
 * This class find default of an interface with all default methods.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultInterfaceSupplierFinder implements IFindSupplier {

    private static final String DEFAULT_INTERFACE = DefaultInterface.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        boolean isDefaultInterface
                =  theGivenClass.isInterface()
                && has(theGivenClass.getAnnotations(), DEFAULT_INTERFACE);
        
        if (!isDefaultInterface)
            return null;
        
        val nonDefaults = getNonDefaultMethods(theGivenClass);
        if (!nonDefaults.isEmpty()) {
            throw new NonDefaultInterfaceException(theGivenClass, nonDefaults);
        }
        
        val theProxy = createDefaultProxy(theGivenClass);
        return () -> theProxy;
    }
    
}
