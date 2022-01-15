//  MIT License
//  
//  Copyright (c) 2017-2019 Nawa Manusitthipol
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

import static defaultj.core.utils.AnnotationUtils.has;

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
