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
package dssb.objectprovider.impl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import dssb.objectprovider.impl.exception.ObjectCreationException;
import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

/**
 * Utility involving constructors
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class ConstructorUtils {
    
    /**
     * Checks if the constructor is public (null-safe).
     * 
     * @param constructor  the constructor.
     * @return {@code true} if the constructor is public.
     */
    public static boolean _isPublic(Constructor<?> constructor) {
        if (constructor._isNull())
            return false;
        
        return Modifier.isPublic(constructor.getModifiers());
    }
    
    /**
     * Returns a supplier of a sensible default constructor for the given class.
     * 
     * If there is only one constructor, return that one.
     * If there is more than one constructor, return the one without arguments.
     * Otherwise, return null.
     * 
     * @param <T>   the data type.
     * @param clzz  the data class.
     * @return  the sensible default constructor.
     */
    public static <T> Supplier<Constructor<T>> sensibleDefaultConstructorOf(Class<T> clzz) {
        return ()->
                hasOnlyOneConsructor(clzz)
                ? getOnlyConstructor(clzz)
                : getNoArgConstructor(clzz);
    }
    
    /**
     * Find a constructor with the annotation.
     * 
     * @param <T>              the data type the given class represent.
     * @param clzz             the data class.
     * @param annotationNames  the annotation name.
     * @return  the constructor found.
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> findConstructorWithAnnotation(Class<T> clzz, String ... annotationNames) {
        for(Constructor<?> constructor : clzz.getConstructors()) {
            if (!Modifier.isPublic(constructor.getModifiers()))
                continue;
            
            if (constructor.getAnnotations().has(annotationNames))
                return (Constructor<T>)constructor;
        }
        return null;
    }
    
    /**
     * Check if there is only one constructor.
     * 
     * @param <T>   the data type that the class represent.
     * @param clzz  the data class.
     * @return  {@code true} if there is only one consturctor.
     */
    public static <T> boolean hasOnlyOneConsructor(final Class<T> clzz) {
        return clzz.getConstructors().length == 1;
    }
    
    /**
     * Find the constructor with no arguments.
     * 
     * @param <T>   the dada type that the clzz represents..
     * @param clzz  the data class.
     * @return  the constructor.
     */
    public static <T> Constructor<T> getNoArgConstructor(Class<T> clzz) {
        try {
            return clzz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return null;
        } catch (SecurityException e) {
            throw new ObjectCreationException(clzz);
        }
    }
    
    /**
     * Find the only constructor of the given class.
     * 
     * @param <T>   the data type that the clzz represent.
     * @param clzz  the clzz.
     * @return  the  constructor found.
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getOnlyConstructor(Class<T> clzz) {
        return (Constructor<T>) clzz.getConstructors()[0];
    }
    
}
