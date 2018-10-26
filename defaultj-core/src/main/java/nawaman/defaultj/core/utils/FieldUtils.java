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
package nawaman.defaultj.core.utils;

import static nawaman.defaultj.core.utils.AnnotationUtils.has;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Utility involving fields.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class FieldUtils {
    
    /** The predicate to check if a field is public. */
    public static Predicate<Field> ifPublicField = field -> _isPublic(field);
    
    /** The predicate to check if a field is static. */
    public static Predicate<Field> ifStaticField = field -> _isStatic(field);
    
    /**
     * Checks if the field is public (null-safe).
     * 
     * @param field  the field.
     * @return {@code true} if the field is public.
     */
    public static boolean _isPublic(Field field) {
        if (field == null)
            return false;
        
        return Modifier.isPublic(field.getModifiers());
    }
    
    /**
     * Checks if the field is static (null-safe).
     * 
     * @param field  the field.
     * @return {@code true} if the field is static.
     */
    public static boolean _isStatic(Field field) {
        if (field == null)
            return false;
        
        return Modifier.isStatic(field.getModifiers());
    }
    
    /**
     * Returns the predicate that check if the field has and annotation with the given names.
     * 
     * @param names  the annotation names.
     * @return  the predicate.
     */
    public static Predicate<Field> annotatedWith(String ... names) {
        return field->has(field.getAnnotations(), names);
    }
    
}
