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

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.nullablej.NullableJ;

/**
 * Utility class for Annotations.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class
})
public class AnnotationUtils {

    /**
     * Check if the an annotation is has the simple name as the one given 
     * @param name  the name expected.
     * @return  the predicate to check if annotation is with the given name.
     **/
    public static Predicate<? super Annotation> withNamed(String name) {
        return annotation->{
            val toString = annotation.toString();
            return toString.matches("^@.*(\\.|\\$)" + name + "\\(.*$");
        };
    }
    
    /**
     * Check to see if the given array of annotation has at least one of the given name.
     * 
     * @param annotations  an array of annotations.
     * @param names        the names expected.
     * @return  {@code true}  if the annotations has at lease one of the names.
     */
    public static boolean has(Annotation[] annotations, String ... names) {
        return names._stream$().anyMatch(
                name -> annotations._hasSome(withNamed(name)));
    }
    
    /**
     * Check to see if the given array of annotation has all of the specified names.
     * 
     * @param annotations  an array of annotations.
     * @param names        the names expected.
     * @return  {@code true}  if the annotations has at lease one of the names.
     */
    public static boolean hasAllOf(Annotation[] annotations, String ... names) {
        return names._stream$().allMatch(
                name -> annotations._hasAll(withNamed(name)));
    }
    
}