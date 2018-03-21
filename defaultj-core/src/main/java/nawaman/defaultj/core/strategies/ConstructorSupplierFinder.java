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

import static nawaman.defaultj.core.utils.ConstructorUtils.sensibleDefaultConstructorOf;
import static nawaman.defaultj.core.utils.MethodSupplierFinderUtils.prepareParameters;

import java.lang.reflect.Constructor;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.defaultj.annotations.Default;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.utils.AnnotationUtils;
import nawaman.defaultj.core.utils.ConstructorUtils;
import nawaman.failable.Failables;
import nawaman.nullablej.NullableJ;

/**
 * This class get a default by invoking a constructor.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class,
    ConstructorUtils.class,
    AnnotationUtils.class
})
public class ConstructorSupplierFinder implements IFindSupplier {
    
    /** The name of the Inject annotation */
    public static final String ANNOTATION_NAME = Default.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE>
            find(Class<TYPE> theGivenClass, IProvideDefault defaultProvider) {
        Constructor<TYPE> constructor
                = theGivenClass.findConstructorWithAnnotation(ANNOTATION_NAME)
                ._orGet(sensibleDefaultConstructorOf(theGivenClass));
        
        if (!constructor._isPublic())
            return null;
        
        @SuppressWarnings("unchecked")
        val supplier = (Supplier<TYPE, THROWABLE>)Failables.of(()->
                callConstructor(constructor, defaultProvider));
        return supplier;
    }
    
    static <TYPE> TYPE callConstructor(Constructor<TYPE> constructor, IProvideDefault defaultProvider)
            throws ReflectiveOperationException {
        // TODO - Change to use method handle.
        val paramValues = prepareParameters(constructor, defaultProvider);
        val instance    = constructor.newInstance(paramValues);
        return (TYPE)instance;
    }
    
}
