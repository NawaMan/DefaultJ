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
package defaultj.core.strategies;

import static defaultj.core.utils.ConstructorUtils._isPublic;
import static defaultj.core.utils.ConstructorUtils.findConstructorWithAnnotation;
import static defaultj.core.utils.ConstructorUtils.sensibleDefaultConstructorOf;
import static defaultj.core.utils.MethodSupplierFinderUtils.prepareParameters;
import static nullablej.NullableJ._orGet;

import java.lang.reflect.Constructor;

import defaultj.annotations.Default;
import defaultj.annotations.PostConstruct;
import defaultj.api.IProvideDefault;
import defaultj.core.utils.failable.Failable.Supplier;
import defaultj.core.utils.failable.Failables;

/**
 * This class get a default by invoking a constructor.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class ConstructorSupplierFinder implements IFindSupplier {
    
    private static final String ANNOTATION_NAME = Default.class.getSimpleName();
    
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE>
            find(Class<TYPE> theGivenClass, IProvideDefault defaultProvider) {
        Constructor<TYPE> constructor
                = _orGet(findConstructorWithAnnotation(theGivenClass, ANNOTATION_NAME), 
                		sensibleDefaultConstructorOf(theGivenClass));
        
        if (!_isPublic(constructor))
            return null;
        
        @SuppressWarnings("unchecked")
        var supplier = (Supplier<TYPE, THROWABLE>)Failables.of(()->
                callConstructor(constructor, defaultProvider));
        return supplier;
    }
    
    private <TYPE> TYPE callConstructor(Constructor<TYPE> constructor, IProvideDefault defaultProvider)
            throws ReflectiveOperationException {
        // TODO - Change to use method handle.
        var paramValues = prepareParameters(constructor, defaultProvider);
        var instance    = constructor.newInstance(paramValues);
        
        // TODO - Do the inherited methods too. - be careful duplicate when done with default methods
        var methods = instance.getClass().getDeclaredMethods();
        for(var method : methods) {
            for(var annotation : method.getAnnotations()) {
                var annotationName = annotation.annotationType().getSimpleName();
                var isPostContruct = PostConstruct.class.getSimpleName().equals(annotationName);
                if (!isPostContruct)
                    continue;
                
                var isAccessible = method.canAccess(instance);
                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                } finally {
                    method.setAccessible(isAccessible);
                }
            }
        }
        
        return (TYPE)instance;
    }
    
}
