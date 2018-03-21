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
package nawaman.defaultj.impl.utils;

import static nawaman.defaultj.impl.utils.AnnotationUtils.has;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

/**
 * Utility involving methods
 * 
 * @author NawaMan -- nawaman@dssb.io
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class MethodUtils {
    
    /** The predicate to check if a method is public. */
    public static Predicate<Method> ifPublicMethod = method -> _isPublic(method);
    
    /** The predicate to check if a method is static. */
    public static Predicate<Method> ifStaticMethod = method -> _isStatic(method);
    
    /**
     * Checks if the method is public (null-safe).
     * 
     * @param method  the method.
     * @return {@code true} if the method is public.
     */
    public static boolean _isPublic(Method method) {
        if (method._isNull())
            return false;
        
        return Modifier.isPublic(method.getModifiers());
    }
    
    /**
     * Checks if the method is static (null-safe).
     * 
     * @param method  the method.
     * @return {@code true} if the method is static.
     */
    public static boolean _isStatic(Method method) {
        if (method._isNull())
            return false;
        
        return Modifier.isStatic(method.getModifiers());
    }
    
    /**
     * Returns the predicate that check if the method has and annotation with the given names.
     * 
     * @param names  the annotation names.
     * @return  the predicate.
     */
    public static Predicate<Method> annotatedWith(String ... names) {
        return method->has(method.getAnnotations(), names);
    }
    
}
