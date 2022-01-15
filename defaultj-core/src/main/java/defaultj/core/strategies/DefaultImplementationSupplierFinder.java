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
package defaultj.core.strategies;

import static defaultj.core.utils.AnnotationUtils.has;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import defaultj.annotations.DefaultImplementation;
import defaultj.api.IProvideDefault;
import defaultj.core.utils.failable.Failable.Supplier;
import defaultj.core.utils.failable.Failables;

/**
 * This class get a default that is a default implementation of the target class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class DefaultImplementationSupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = DefaultImplementation.class.getSimpleName();
    
    private static final Function<String, String> extractValue = toString -> {
        return toString.replaceAll("^(.*\\(value=\")(.*)(\"\\))$", "$2");
    };
    
    private static final Predicate<? super Annotation> isDefaultImplementation = annotation -> {
        return ANNOTATION_NAME.equals(annotation.annotationType().getSimpleName());
    };
    
    private static final Function<Object, String> toString = Object::toString;
    private static final Predicate<Object>        notNull  = Objects::nonNull;
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        if (!has(theGivenClass.getAnnotations(), ANNOTATION_NAME))
            return null;
        
        var defaultImplementationClass = findDefaultImplementation(theGivenClass);
        if (defaultImplementationClass == null)
            return null;
        
        return Failables.of(()->{ 
            return (TYPE)defaultProvider.get(defaultImplementationClass);
        });
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Class<T> findDefaultImplementation(Class<T> theGivenClass) {
        Class<?> implementedClass
                = Stream.of(theGivenClass.getAnnotations())
                .filter(isDefaultImplementation)
                .map   (toString)
                .map   (extractValue)
                .map   (findClass())
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
