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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import defaultj.core.exception.DefaultCreationException;
import lombok.val;

/**
 * Utility involving constructors
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ConstructorUtils {
    
    /**
     * Checks if the constructor is public (null-safe).
     * 
     * @param constructor  the constructor.
     * @return {@code true} if the constructor is public.
     */
    public static boolean _isPublic(Constructor<?> constructor) {
        if (constructor == null)
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Constructor<T> findConstructorWithAnnotation(Class<T> clzz, String ... annotationNames) {
        Constructor foundConstructor = null;
        for (val constructor : clzz.getConstructors()) {
            if (!Modifier.isPublic(constructor.getModifiers()))
                continue;
            
            if (has(constructor.getAnnotations(), annotationNames)) {
                if (foundConstructor != null)
                    return null;
                
                foundConstructor = constructor;
            }
        }
        return foundConstructor;
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
            throw new DefaultCreationException(clzz);
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
