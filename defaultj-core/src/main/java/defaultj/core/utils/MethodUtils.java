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

import static defaultj.core.utils.AnnotationUtils.has;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Utility involving methods
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
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
        if (method == null)
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
        if (method == null)
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
