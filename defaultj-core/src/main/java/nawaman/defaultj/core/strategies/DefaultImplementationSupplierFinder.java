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

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.defaultj.annotations.DefaultImplementation;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.utils.AnnotationUtils;
import nawaman.failable.Failables;
import nawaman.nullablej.NullableJ;

/**
 * This class get a default that is a default implementation of the target class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class DefaultImplementationSupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = DefaultImplementation.class.getSimpleName();

    private static final Function<String, String> extractValue
            = toString->toString.replaceAll("^(.*\\(value=)(.*)(\\))$", "$2");
    
    private static final Function<Object, String> toString = Object::toString;
    private static final Predicate<Object>        notNull  = Objects::nonNull;
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        if (!theGivenClass.getAnnotations().has(ANNOTATION_NAME))
            return null;
        
        val defaultImplementationClass = findDefaultImplementation(theGivenClass);
        if (defaultImplementationClass._isNull())
            return NullSupplier;
        
        return Failables.of(()->{ 
            return (TYPE)defaultProvider.get(defaultImplementationClass);
        });
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Class<T> findDefaultImplementation(Class<T> theGivenClass) {
        Class<?> implementedClass
                = theGivenClass.getAnnotations()._stream$()
                .map(toString)
                .map(extractValue)
                .map(findClass())
                .filter(notNull)
                .filter(isAssignableTo(theGivenClass))
                .findAny()
                .orElse(null);
        return (Class<T>)implementedClass;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Function<String, Class<T>> findClass() {
        return name -> {
            try {
                return (Class<T>)Class.forName(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        };
    }
    
    private static <T> Predicate<Class<T>> isAssignableTo(Class<?> theGivenClass) {
        return theGivenClass::isAssignableFrom;
    }
    
}
