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

import static nawaman.defaultj.core.strategies.common.NullSupplier;
import static nawaman.defaultj.core.utils.AnnotationUtils.has;
import static nullablej.NullableJ._isNull;
import static nullablej.NullableJ._stream$;

import java.util.function.Function;

import lombok.val;
import nawaman.defaultj.annotations.ImplementedBy;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.exception.ImplementedClassNotCompatibleExistException;
import nawaman.defaultj.core.exception.ImplementedClassNotExistException;
import nawaman.failable.Failable.Supplier;
import nawaman.failable.Failables;

/**
 * This class get a default that is a default implementation of the target class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ImplementedBySupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = ImplementedBy.class.getSimpleName();

    private static final Function<String, String> extractValue = toString->
                toString.replaceAll("^(.*\\(value=class )(.*)(\\))$", "$2");
    
    private static final Function<Object, String> toString = Object::toString;
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        if (!has(theGivenClass.getAnnotations(), ANNOTATION_NAME))
            return null;
        
        val defaultImplementationClass = findDefaultImplementation(theGivenClass);
        if (_isNull(defaultImplementationClass))
            return NullSupplier;
        
        return Failables.of(()->{ 
            return (TYPE)defaultProvider.get(defaultImplementationClass);
        });
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Class<T> findDefaultImplementation(Class<T> theGivenClass) {
        Class<?> implementedClass
                = _stream$(theGivenClass.getAnnotations())
                .map(toString)
                .map(extractValue)
                .map(findClass(theGivenClass))
                .findAny()
                .get();
        if (!theGivenClass.isAssignableFrom(implementedClass))
            throw new ImplementedClassNotCompatibleExistException(theGivenClass, implementedClass.getName());
        
        return (Class<T>)implementedClass;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Function<String, Class<T>> findClass(Class<?> theGivenClass) {
        return name -> {
            try {
                return (Class<T>)Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new ImplementedClassNotExistException(theGivenClass, name, e);
            }
        };
    }
    
}
