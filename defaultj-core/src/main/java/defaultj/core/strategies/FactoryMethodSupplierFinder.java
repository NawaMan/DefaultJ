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

import static defaultj.core.utils.MethodSupplierFinderUtils.prepareParameters;
import static defaultj.core.utils.MethodUtils.annotatedWith;
import static defaultj.core.utils.MethodUtils.ifPublicMethod;
import static defaultj.core.utils.MethodUtils.ifStaticMethod;
import static nullablej.NullableJ._stream$;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Predicate;

import defaultj.annotations.Default;
import defaultj.api.IProvideDefault;
import defaultj.core.utils.failable.Failable.Supplier;
import nullablej.nullable.Nullable;

/**
 * This class returns object resulting from a factory method.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
public class FactoryMethodSupplierFinder implements IFindSupplier {
    
    private static final String            DEFAULT              = Default.class.getSimpleName();
    private static final Predicate<Method> annotatedWithDefault = annotatedWith(DEFAULT);
    
    @SuppressWarnings({ "unchecked" })
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        var methodValue = findValueFromFactoryMethod(theGivenClass, defaultProvider);
        return (Supplier<TYPE, THROWABLE>)methodValue;
    }
    
    @SuppressWarnings("unchecked")
    private <T> Supplier<T, ? extends Throwable> findValueFromFactoryMethod(Class<T> theGivenClass, IProvideDefault defaultProvider) {
        var helper   = new Helper<T>(theGivenClass, defaultProvider);
        var supplier = (Supplier<T, ? extends Throwable>)
                _stream$(theGivenClass.getDeclaredMethods())
                .filter(ifStaticMethod)
                .filter(ifPublicMethod)
                .filter(annotatedWithDefault)
                .map(helper::findValue)
                .findAny()
                .orElse(null);
        return supplier;
    }
    
    static class Helper<T> {
        private Class<T> theGivenClass;
        private IProvideDefault defaultProvider;
        
        public Helper(Class<T> theGivenClass, IProvideDefault defaultProvider) {
            this.theGivenClass = theGivenClass;
            this.defaultProvider = defaultProvider;
        }
        
        @SuppressWarnings({ "rawtypes" })
        private Supplier findValue(Method method) {
            var type = method.getReturnType();
            if (theGivenClass.isAssignableFrom(type))
                return (Supplier)(()->basicFactoryMethodCall(method));
            
            var fromNullable = findNullableOrOptional(method, type);
            if (fromNullable != null)
                return fromNullable;
            
            var fromSupplier = findSupplier(method, type);
            if (fromSupplier != null)
                return fromSupplier;
            
            return null;
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findSupplier(Method method, final java.lang.Class<?> type) {
            if (!java.util.function.Supplier.class.isAssignableFrom(type)) 
                return null;
            
            var parameterizedType = (ParameterizedType)method.getGenericReturnType();
            var actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            var getMethod = getGetMethod();
             return (Supplier)(()->supplierFactoryMethodCall(method, getMethod));
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findNullableOrOptional(Method method, Class<?> type) {
            var isOptional = Optional.class.isAssignableFrom(type);
            var isNullable = !isOptional && Nullable.class.isAssignableFrom(type);
            if (!isOptional && !isNullable)
                return null;
            
            var parameterizedType = (ParameterizedType)method.getGenericReturnType();
            var actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            return (Supplier)(()->getNullableOrOptionalValue(method, isNullable));
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Object getNullableOrOptionalValue(Method method, final boolean isNullable)
                throws IllegalAccessException, InvocationTargetException {
            var params   = prepareParameters(method, defaultProvider);
            var nullable = method.invoke(theGivenClass, params);
            var value = isNullable
                    ? ((Nullable)nullable).orElse(null)
                    : ((Optional)nullable).orElse(null);
            return value;
        }
        
        private static Method getGetMethod() {
            try {
                // TODO - Change to use MethodHandler.
                return java.util.function.Supplier.class.getMethod("get", new Class[0]);
            } catch (NoSuchMethodException | SecurityException e) {
                // I am sure it is there.
                throw new RuntimeException(e);
            }
        }
        
        private Object supplierFactoryMethodCall(
                Method method,
                Method getMethod) 
                        throws IllegalAccessException, InvocationTargetException {
            var params = prepareParameters(method, defaultProvider);
            var result = method.invoke(theGivenClass, params);
            var value  = getMethod.invoke(result);
            return value;
        }
        
        private Object basicFactoryMethodCall(Method method)
                throws IllegalAccessException, InvocationTargetException {
            var params = prepareParameters(method, defaultProvider);
            var value  = method.invoke(theGivenClass, params);
            return value;
        }
    }
    
}
