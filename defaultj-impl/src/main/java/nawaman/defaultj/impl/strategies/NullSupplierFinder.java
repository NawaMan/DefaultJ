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
package nawaman.defaultj.impl.strategies;

import static nawaman.defaultj.impl.strategies.common.NullSupplier;

import lombok.experimental.ExtensionMethod;
import nawaman.defaultj.annotations.DefaultToNull;
import nawaman.defaultj.api.IProvideObject;
import nawaman.defaultj.impl.utils.AnnotationUtils;
import nawaman.failable.Failable.Supplier;

/**
 * This class will provider null if the given class is annotated with @DefaultToNull.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({ AnnotationUtils.class })
public class NullSupplierFinder implements IFindSupplier {
    
    /** The name of the DefaultToNull annotation */
    public static final String ANNOTATION_NAME = DefaultToNull.class.getSimpleName();
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>    theGivenClass,
            IProvideObject objectProvider) {
        return theGivenClass.getAnnotations().has(ANNOTATION_NAME)
                ? (Supplier<TYPE, THROWABLE>)NullSupplier
                : null;
    }
    
}
