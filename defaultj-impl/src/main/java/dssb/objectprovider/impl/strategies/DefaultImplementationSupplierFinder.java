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
package dssb.objectprovider.impl.strategies;

import static dssb.objectprovider.impl.strategies.common.NullSupplier;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import dssb.objectprovider.api.IProvideObject;
import dssb.objectprovider.impl.utils.AnnotationUtils;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.failable.Failable.Supplier;
import nawaman.failable.Failables;
import nawaman.nullablej.NullableJ;

/**
 * This class get an object that is a default implementation of the target class.
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({ NullableJ.class, AnnotationUtils.class })
public class DefaultImplementationSupplierFinder implements IFindSupplier {
    
    private static final Function<String, String> extractValue
            = toString->toString.replaceAll("^(.*\\(value=)(.*)(\\))$", "$2");
    
    private static final Function<Object, String> toString = Object::toString;
    private static final Predicate<Object>        notNull  = Objects::nonNull;
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>    theGivenClass,
            IProvideObject objectProvider) {
        if (!theGivenClass.getAnnotations().has("DefaultImplementation"))
            return null;
        
        val defaultImplementationClass = findDefaultImplementation(theGivenClass);
        if (defaultImplementationClass._isNull())
            return NullSupplier;
        
        return Failables.of(()->{ 
            return (TYPE)objectProvider.get(defaultImplementationClass);
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
    static <T> Function<String, Class<T>> findClass() {
        return name -> {
            try {
                return (Class<T>)Class.forName(name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        };
    }
    
    static <T> Predicate<Class<T>> isAssignableTo(Class<?> theGivenClass) {
        return theGivenClass::isAssignableFrom;
    }
    
}
