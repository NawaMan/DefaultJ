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

import static defaultj.core.strategies.common.NullSupplier;
import static defaultj.core.utils.AnnotationUtils.has;
import static nullablej.NullableJ._isNull;
import static nullablej.NullableJ._stream$;

import java.util.function.Function;

import defaultj.annotations.ImplementedBy;
import defaultj.api.IProvideDefault;
import defaultj.core.exception.ImplementedClassNotCompatibleExistException;
import defaultj.core.exception.ImplementedClassNotExistException;
import defaultj.core.utils.failable.Failables;
import defaultj.core.utils.failable.Failable.Supplier;
import lombok.val;

/**
 * This class get a default that is a default implementation of the target class.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ImplementedBySupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = ImplementedBy.class.getSimpleName();

    private static final Function<String, String> extractValue = toString->
                toString.replaceAll("^(.*\\(value=class )(.*)(\\))$", "$2");
    
    private static final Function<Object, String> toString = Object::toString;
    
    @SuppressWarnings("unchecked")
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        if (!has(theGivenClass.getAnnotations(), ANNOTATION_NAME))
            return null;
        
        val defaultImplementationClass = findDefaultImplementation(theGivenClass);
        if (_isNull(defaultImplementationClass))
            return NullSupplier;
        
        return Failables.of(()->{ 
            return (TYPE)defaultProvider.get(defaultImplementationClass);
        });
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Class<T> findDefaultImplementation(Class<T> theGivenClass) {
        Class<?> implementedClass
                = _stream$(theGivenClass.getAnnotations())
                .map(toString)
                .map(extractValue)
                .map(findClass(theGivenClass))
                .findAny()
                .get();
        if (!theGivenClass.isAssignableFrom(implementedClass))
            throw new ImplementedClassNotCompatibleExistException(theGivenClass, implementedClass.getName());
        
        return (Class<T>)implementedClass;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Function<String, Class<T>> findClass(Class<?> theGivenClass) {
        return name -> {
            try {
                return (Class<T>)Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new ImplementedClassNotExistException(theGivenClass, name, e);
            }
        };
    }
    
}
