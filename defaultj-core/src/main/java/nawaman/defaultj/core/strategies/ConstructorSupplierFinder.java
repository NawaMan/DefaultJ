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

import static nawaman.defaultj.core.utils.ConstructorUtils._isPublic;
import static nawaman.defaultj.core.utils.ConstructorUtils.findConstructorWithAnnotation;
import static nawaman.defaultj.core.utils.ConstructorUtils.sensibleDefaultConstructorOf;
import static nawaman.defaultj.core.utils.MethodSupplierFinderUtils.prepareParameters;
import static nawaman.nullablej.NullableJ._orGet;

import java.lang.reflect.Constructor;

import lombok.val;
import nawaman.defaultj.annotations.Default;
import nawaman.defaultj.annotations.PostConstruct;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.failable.Failable.Supplier;
import nawaman.failable.Failables;

/**
 * This class get a default by invoking a constructor.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ConstructorSupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = Default.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE>
            find(Class<TYPE> theGivenClass, IProvideDefault defaultProvider) {
        Constructor<TYPE> constructor
                = _orGet(findConstructorWithAnnotation(theGivenClass, ANNOTATION_NAME), 
                		sensibleDefaultConstructorOf(theGivenClass));
        
        if (!_isPublic(constructor))
            return null;
        
        @SuppressWarnings("unchecked")
        val supplier = (Supplier<TYPE, THROWABLE>)Failables.of(()->
                callConstructor(constructor, defaultProvider));
        return supplier;
    }
    
    private <TYPE> TYPE callConstructor(Constructor<TYPE> constructor, IProvideDefault defaultProvider)
            throws ReflectiveOperationException {
        // TODO - Change to use method handle.
        val paramValues = prepareParameters(constructor, defaultProvider);
        val instance    = constructor.newInstance(paramValues);
        
        // TODO - Do the inherited methods too. - be careful duplicate when done with default methods
        val methods = instance.getClass().getDeclaredMethods();
        for(val method : methods) {
            for(val annotation : method.getAnnotations()) {
                val annotationName = annotation.annotationType().getSimpleName();
                val isPostContruct = PostConstruct.class.getSimpleName().equals(annotationName);
                if (!isPostContruct)
                    continue;
                
                val isAccessible = method.isAccessible();
                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                } finally {
                    method.setAccessible(isAccessible);
                }
            }
        }
        
        return (TYPE)instance;
    }
    
}
