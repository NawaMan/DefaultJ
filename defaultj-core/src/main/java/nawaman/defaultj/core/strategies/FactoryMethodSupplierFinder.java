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
package nawaman.defaultj.core.strategies;

import static nawaman.defaultj.core.utils.MethodSupplierFinderUtils.prepareParameters;
import static nawaman.defaultj.core.utils.MethodUtils.annotatedWith;
import static nawaman.defaultj.core.utils.MethodUtils.ifPublicMethod;
import static nawaman.defaultj.core.utils.MethodUtils.ifStaticMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Predicate;

import lombok.Value;
import lombok.val;
import lombok.experimental.ExtensionMethod;
import nawaman.defaultj.annotations.Default;
import nawaman.defaultj.api.IProvideDefault;
import nawaman.defaultj.core.utils.AnnotationUtils;
import nawaman.failable.Failable.Supplier;
import nawaman.nullablej.NullableJ;
import nawaman.nullablej.nullable.Nullable;

/**
 * This class returns object resulting from a factory method.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@ExtensionMethod({
    NullableJ.class,
    AnnotationUtils.class
})
public class FactoryMethodSupplierFinder implements IFindSupplier {
    
    private static final String            DEFAULT              = Default.class.getSimpleName();
    private static final Predicate<Method> annotatedWithDefault = annotatedWith(DEFAULT);
    
    @SuppressWarnings({ "unchecked" })
    @Override
    public <TYPE, THROWABLE extends Throwable> Supplier<TYPE, THROWABLE> find(
            Class<TYPE>     theGivenClass,
            IProvideDefault defaultProvider) {
        val methodValue = findValueFromFactoryMethod(theGivenClass, defaultProvider);
        return (Supplier<TYPE, THROWABLE>)methodValue;
    }
    
    @SuppressWarnings("unchecked")
    private <T> Supplier<T, ? extends Throwable> findValueFromFactoryMethod(Class<T> theGivenClass, IProvideDefault defaultProvider) {
        val helper   = new Helper<T>(theGivenClass, defaultProvider);
        val supplier = (Supplier<T, ? extends Throwable>)
                theGivenClass.getDeclaredMethods()._stream$()
                .filter(ifStaticMethod)
                .filter(ifPublicMethod)
                .filter(annotatedWithDefault)
                .map(helper::findValue)
                .findAny()
                .orElse(null);
        return supplier;
    }
    
    @Value
    static class Helper<T> {
        private Class<T> theGivenClass;
        private IProvideDefault defaultProvider;
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Supplier findValue(Method method) {
            val type = method.getReturnType();
            if (theGivenClass.isAssignableFrom(type))
                return (Supplier)(()->basicFactoryMethodCall(method));
            
            val fromNullable = findNullableOrOptional(method, type);
            if (fromNullable._isNotNull())
                return fromNullable;
            
            val fromSupplier = findSupplier(method, type);
            if (fromSupplier._isNotNull())
                return fromSupplier;
            
            return null;
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findSupplier(Method method, final java.lang.Class<?> type) {
            if (!java.util.function.Supplier.class.isAssignableFrom(type)) 
                return null;
            
            val parameterizedType = (ParameterizedType)method.getGenericReturnType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            val getMethod = getGetMethod();
             return (Supplier)(()->supplierFactoryMethodCall(method, getMethod));
        }
        
        @SuppressWarnings("rawtypes")
        private Supplier findNullableOrOptional(Method method, Class<?> type) {
            val isOptional = Optional.class.isAssignableFrom(type);
            val isNullable = !isOptional && Nullable.class.isAssignableFrom(type);
            if (!isOptional && !isNullable)
                return null;
            
            val parameterizedType = (ParameterizedType)method.getGenericReturnType();
            val actualType        = (Class)parameterizedType.getActualTypeArguments()[0];
            
            if (!theGivenClass.isAssignableFrom(actualType))
                return null;
            
            return (Supplier)(()->getNullableOrOptionalValue(method, isNullable));
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Object getNullableOrOptionalValue(Method method, final boolean isNullable)
                throws IllegalAccessException, InvocationTargetException {
            val params   = prepareParameters(method, defaultProvider);
            val nullable = method.invoke(theGivenClass, params);
            val value = isNullable
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
            val params = prepareParameters(method, defaultProvider);
            val result = method.invoke(theGivenClass, params);
            val value  = getMethod.invoke(result);
            return value;
        }
        
        private Object basicFactoryMethodCall(Method method)
                throws IllegalAccessException, InvocationTargetException {
            val params = prepareParameters(method, defaultProvider);
            val value  = method.invoke(theGivenClass, params);
            return value;
        }
    }
    
}
