//  MIT License
//  
//  Copyright (c) 2017-2023 Nawa Manusitthipol
//  
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to deal
//  in the Software without restriction, including without limitation the rights
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//  copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  
//  The above copyright notice and this permission notice shall be included in all
//  copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//  SOFTWARE.
package defaultj.core.utils;

import static nullablej.NullableJ._hasAll;
import static nullablej.NullableJ._hasSome;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Utility class for Annotations.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class AnnotationUtils {

    /**
     * Check if the an annotation is has the simple name as the one given 
     * @param name  the name expected.
     * @return  the predicate to check if annotation is with the given name.
     **/
    public static Predicate<? super Annotation> withNamed(String name) {
        return annotation->{
            var toString = annotation.toString();
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
        return Stream.of(names).anyMatch(
                name -> _hasSome(annotations, withNamed(name)));
    }
    
    /**
     * Check to see if the given array of annotation has all of the specified names.
     * 
     * @param annotations  an array of annotations.
     * @param names        the names expected.
     * @return  {@code true}  if the annotations has at lease one of the names.
     */
    public static boolean hasAllOf(Annotation[] annotations, String ... names) {
        return Stream.of(names).allMatch(
                name -> _hasAll(annotations, withNamed(name)));
    }
    
}
